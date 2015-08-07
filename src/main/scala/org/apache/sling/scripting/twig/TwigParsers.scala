package org.apache.sling.scripting.twig

import scala.util.parsing.combinator.RegexParsers
import org.apache.sling.scripting.twig.elements.ParameteredFilter
import org.apache.sling.scripting.twig.elements.Filter
import org.apache.sling.scripting.twig.elements.Printable
import org.apache.sling.scripting.twig.elements.Variable
import org.apache.sling.scripting.twig.elements.Value
import org.apache.sling.scripting.twig.elements.Concatenation
import org.apache.sling.scripting.twig.elements.Token
import org.apache.sling.scripting.twig.elements.Literal
import org.apache.sling.scripting.twig.elements.SimpleVariable
import org.apache.sling.scripting.twig.elements.AccessorVariable
import org.apache.sling.scripting.twig.elements.ValueHolder
import org.apache.sling.scripting.twig.elements.Concatenation
import org.apache.sling.scripting.twig.elements.FilteredValueHolder

class TwigParsers extends RegexParsers {
  def identifier: Parser[String] = """[a-zA-Z][a-zA-Z0-9]*""".r ^^ { _.toString }
  def number: Parser[Double] = """/[0-9]+(?:\.[0-9]+)?/A""".r ^^ { _.toDouble }
  def string: Parser[String] = """/"([^#"\\\\]*(?:\\\\.[^#"\\\\]*)*)"|\'([^\'\\\\]*(?:\\\\.[^\'\\\\]*)*)\'/As""".r
  def punctuation = """()[]{}?:.,|""".r

  def whitespace_trim = "-"
  def apply_filter = "|"

  def tag_start_block = "{%" ~ whitespace_trim.?
  def tag_end_block = whitespace_trim.? ~ "%}"
  def tag_start_variable = "{{" ~ whitespace_trim.?
  def tag_end_variable = whitespace_trim.? ~ "}}"
  def tag_start_comment = "{#" ~ whitespace_trim.?
  def tag_end_comment = whitespace_trim.? ~ "#}"

  /* Language elements */
  def tags = "autoescape" | "block" | "do" | "embed" |
    "extends" | "filter" | "flush" | "for" | "from" | "if" |
    "import" | "include" | "macro" | "sandbox" | "set" | "spaceless" |
    "use" | "verbatim"

  def simple_filters: Parser[Filter] = ("abs" | "capitalize" | "escape" | "first" | "keys" | "last"
    | "length" | "lower" | "nl2br" | "raw" | "reverse" | "sort" | "striptags" | "title" | "trim"
    | "upper" | "url_encode" | "number_format" | "json_encode()") ^^ { name => Filter(name) }

  def parametered_filters: Parser[ParameteredFilter] = ("batch" | "convert_encoding" | "date_modify"
    | "date" | "default" | "format" | "join" | "merge" | "number_format" | "replace" | "slice"
    | "split") ~ ("(" ~> repsep(parameter, ",") <~ ")") ^^ { case name ~ parameters => ParameteredFilter(name, parameters) }

  def filters: Parser[Filter] = parametered_filters | simple_filters

  def functions = "attribute" | "block" | "constant" | "cycle" | "date" |
    "dump" | "include" | "parent" | "random" | "range" | "template_from_string"

  def tests = "constant" | "defined" | "divisibleby" | "empty" |
    "even" | "iterable" | "null" | "odd" | "sameas"

  def math_operators = "+" | "-" | "/" | "%" | "**" | "*"
  def logical_operators = "and" | "or" | "not" | "(" | ")" | "b-and" | "b-xor" | "b-or"
  def comparison_operators = "===" | "==" | "!=" | "<=" | "<" | ">=" | ">"
  def operators = "in" | "is" | math_operators | logical_operators | comparison_operators | ".." | "?:"
  
  /* Parameters */
  def variable: Parser[Variable] = identifier ~ (("." ~> identifier) | ("['" ~> identifier <~ "']")).* ^^ {
    case name ~ Nil => SimpleVariable(name)
    case name ~ children => AccessorVariable(name, children)
  }
  def string_value: Parser[Value] = string ^^ { value => Value(value)}
  def value_holder: Parser[ValueHolder] = variable | string_value
  def parameter: Parser[ValueHolder] = (variable ~ (apply_filter ~> filters).*) ^^ {
    case v ~ Nil => v
    case v ~ fs => FilteredValueHolder(v, fs)
  }
  def concatenation: Parser[ValueHolder] = parameter ~ ("~" ~> parameter).* ^^ {
    case v ~ Nil => v
    case v ~ vs => Concatenation(v :: vs)
  }
  
  /* Printable */
  def printable: Parser[Printable] = tag_start_variable ~> concatenation <~ tag_end_variable ^^ {
    case v => Printable(v)
  }
  
  def non_twig: Parser[Literal] = """[^]""".r ^^ { case value => Literal(value) }
  
  def script: Parser[List[Token]] = (printable | non_twig).*

  def apply(input: String): List[Token] = parseAll(script, input).get
}

