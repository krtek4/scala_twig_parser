package org.apache.sling.scripting.twig

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.OneInstancePerTest
import org.scalatest.FunSpec

@RunWith(classOf[JUnitRunner])
class TwigParsersSuite extends FunSpec with OneInstancePerTest {
  object MyParser extends TwigParsers {
    def check[T](p: Parser[T], s: String): Boolean = parseAll(p, s) match {
      case Success(x, _) => true
      case x => println(x); false
    }
  }

  describe("A Parser") {
    it("should recognize start tags") {
      assert(MyParser.check(MyParser.tag_start_block, "{%"));
      assert(MyParser.check(MyParser.tag_start_comment, "{#"));
      assert(MyParser.check(MyParser.tag_start_variable, "{{"));

      assert(MyParser.check(MyParser.tag_start_block, "{%-"));
      assert(MyParser.check(MyParser.tag_start_comment, "{#-"));
      assert(MyParser.check(MyParser.tag_start_variable, "{{-"));
    }

    it("should recognize end tags") {
      assert(MyParser.check(MyParser.tag_end_block, "%}"));
      assert(MyParser.check(MyParser.tag_end_comment, "#}"));
      assert(MyParser.check(MyParser.tag_end_variable, "}}"));

      assert(MyParser.check(MyParser.tag_end_block, "-%}"));
      assert(MyParser.check(MyParser.tag_end_comment, "-#}"));
      assert(MyParser.check(MyParser.tag_end_variable, "-}}"));
    }

    it("should recognize tags") {
      val list = List("autoescape", "block", "do", "embed",
        "extends", "filter", "flush", "for", "from", "if",
        "import", "include", "macro", "sandbox", "set", "spaceless",
        "use", "verbatim")

      for (t <- list)
        assert(MyParser.check(MyParser.tags, t));
    }

    it("should recognize simple filters") {
      val list = List("abs", "capitalize", "escape", "first", "keys", "last",
        "length", "lower", "nl2br", "raw", "reverse", "sort", "striptags", "title", "trim",
        "upper", "url_encode", "number_format", "json_encode()")

      for (t <- list)
        assert(MyParser.check(MyParser.filters, t));
    }

    it("should recognize parametered filters") {
      val list = List("batch", "convert_encoding", "date_modify", "date", "default",
        "format", "join", "merge", "number_format", "replace", "slice", "split")

      for (t <- list)
        assert(MyParser.check(MyParser.filters, t + "(foo)"));
    }

    it("should recognize functions") {
      val list = List("attribute", "block", "constant", "cycle", "date",
        "dump", "include", "parent", "random", "range", "template_from_string")

      for (t <- list)
        assert(MyParser.check(MyParser.functions, t));
    }

    it("should recognize tests") {
      val list = List("constant", "defined", "divisibleby", "empty",
        "even", "iterable", "null", "odd", "sameas")

      for (t <- list)
        assert(MyParser.check(MyParser.tests, t));
    }

    it("should recognize operators") {
      val list = List("in", "is",
        "+", "-", "/", "%", "*", "**",
        "and", "or", "not", "(", ")", "b-and", "b-xor", "b-or",
        "==", "!=", "<", ">", ">=", "<=", "===",
        "?:")

      for (t <- list)
        assert(MyParser.check(MyParser.operators, t));
    }

    it("should parse simple variables") {
      assert(MyParser.check(MyParser.printable, "{{ foo }}"))
      assert(MyParser.check(MyParser.printable, "{{ bar }}"))
      assert(MyParser.check(MyParser.printable, "{{ baz }}"))
    }

    it("should parse simple variables with one filter") {
      assert(MyParser.check(MyParser.printable, "{{ foo | upper }}"))
      assert(MyParser.check(MyParser.printable, "{{ foo|upper}}"))
      assert(MyParser.check(MyParser.printable, "{{ foo |upper }}"))
      assert(MyParser.check(MyParser.printable, "{{ foo| upper }}"))
    }

    it("should parse simple variables with multiple filters") {
      assert(MyParser.check(MyParser.printable, "{{ foo | upper | json_encode() }}"))
      assert(MyParser.check(MyParser.printable, "{{ foo|abs|upper|trim}}"))
    }

    it("should parse variables with dot notation") {
      assert(MyParser.check(MyParser.printable, "{{ foo.bar }}"))
      assert(MyParser.check(MyParser.printable, "{{ foo.bar.baz }}"))
      assert(MyParser.check(MyParser.printable, "{{ foo.bar | upper }}"))
    }

    it("should parse variables with braquet notation") {
      assert(MyParser.check(MyParser.printable, "{{ foo['bar'] }}"))
      assert(MyParser.check(MyParser.printable, "{{ foo['bar']['baz'] }}"))
      assert(MyParser.check(MyParser.printable, "{{ foo['bar'] | upper }}"))
    }

    it("should parse variables with both dot and bracket notation") {
      assert(MyParser.check(MyParser.printable, "{{ foo['bar'].baz['toto'] }}"))
    }
  }
}