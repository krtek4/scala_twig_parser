package org.apache.sling.scripting.twig

import scala.util.parsing.combinator.RegexParsers

class TwigParsers extends RegexParsers {
  def identifier = """[a-zA-Z][a-zA-Z0-9]*""".r
  def number = """/[0-9]+(?:\.[0-9]+)?/A""".r
  def string = """/"([^#"\\\\]*(?:\\\\.[^#"\\\\]*)*)"|\'([^\'\\\\]*(?:\\\\.[^\'\\\\]*)*)\'/As""".r
  def punctuation = """()[]{}?:.,|""".r
  
  def whitespace_trim = "-"
  def apply_filter = "|"
    
  def tag_start_block = "{%" ~ whitespace_trim.?
  def tag_end_block = whitespace_trim.? ~"%}"
  def tag_start_variable = "{{" ~ whitespace_trim.?
  def tag_end_variable = whitespace_trim.? ~"}}"
  def tag_start_comment = "{#" ~ whitespace_trim.?
  def tag_end_comment = whitespace_trim.? ~ "#}"
  
  def tags = "autoescape" | "block" | "do" | "embed" |
	      "extends" | "filter" | "flush" | "for" | "from" | "if" |
	      "import" | "include" | "macro" | "sandbox" | "set" | "spaceless" | 
	      "use" | "verbatim"
	      
  def filters = "abs" | "batch" | "capitalize" | "convert_encoding" | 
	      "date_modify" | "date" | "default" | "escape" | "first" | "format" | 
	      "join" | "json_encode" | "keys" | "last" | "length" | "lower" |
	      "merge" | "nl2br" | "number_format" | "raw" | "replace" | "reverse" | 
	      "slice" | "sort" | "split" | "striptags" | "title" | "trim" | "upper" |
	      "url_encode"
	      
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
}