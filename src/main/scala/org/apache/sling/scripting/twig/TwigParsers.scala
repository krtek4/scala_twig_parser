package org.apache.sling.scripting.twig

import scala.util.parsing.combinator.RegexParsers

class TwigParsers extends RegexParsers {
  def identifier = """[a-zA-Z][a-zA-Z0-9]*""".r
  def number: Parser[Double] = """/[0-9]+(?:\.[0-9]+)?/A""".r ^^ { _.toDouble }
  def string = """/"([^#"\\\\]*(?:\\\\.[^#"\\\\]*)*)"|\'([^\'\\\\]*(?:\\\\.[^\'\\\\]*)*)\'/As""".r
  def punctuation = """()[]{}?:.,|""".r

  def whitespace_trim = "-"
  def apply_filter = "|"

  def tag_start_block = "{%" ~ whitespace_trim.?
  def tag_end_block = whitespace_trim.? ~ "%}"
  def tag_start_variable = "{{" ~ whitespace_trim.?
  def tag_end_variable = whitespace_trim.? ~ "}}"
  def tag_start_comment = "{#" ~ whitespace_trim.?
  def tag_end_comment = whitespace_trim.? ~ "#}"

  def tags = "autoescape" | "block" | "do" | "embed" |
    "extends" | "filter" | "flush" | "for" | "from" | "if" |
    "import" | "include" | "macro" | "sandbox" | "set" | "spaceless" |
    "use" | "verbatim"

  def simple_filters: Parser[Filter] = ("abs" | "capitalize" | "escape" | "first" | "keys" | "last"
    | "length" | "lower" | "nl2br" | "raw" | "reverse" | "sort" | "striptags" | "title" | "trim"
    | "upper" | "url_encode" | "number_format" | "json_encode()") ^^ { name => Filter(name) }

  def parametered_filters: Parser[ParameteredFilter] = ("batch" | "convert_encoding" | "date_modify"
    | "date" | "default" | "format" | "join" | "merge" | "number_format" | "replace" | "slice"
    | "split") ~ ("(" ~> printable.* <~ ")") ^^ { case name ~ parameters => ParameteredFilter(name, parameters) }

  def filters: Parser[Filter] = parametered_filters | simple_filters

  def functions = "attribute" | "block" | "constant" | "cycle" | "date" |
    "dump" | "include" | "parent" | "random" | "range" | "template_from_string"

  def tests = "constant" | "defined" | "divisibleby" | "empty" |
    "even" | "iterable" | "null" | "odd" | "sameas"

  def math_operators = "+" | "-" | "/" | "%" | "**" | "*"
  def logical_operators = "and" | "or" | "not" | "(" | ")" | "b-and" | "b-xor" | "b-or"
  def comparison_operators = "===" | "==" | "!=" | "<=" | "<" | ">=" | ">"
  def operators = "in" | "is" | math_operators | logical_operators | comparison_operators | ".." | "?:"

  def simple_printable = identifier ~ ("." ~> identifier) | identifier ~ ("['" ~> identifier <~ "']") | identifier
  def printable = simple_printable ~ ("~" ~> simple_printable).*
  def variable = tag_start_variable ~> (printable ~ (apply_filter ~> filters).*) <~ tag_end_variable

  def apply(input: String): String = input
}

