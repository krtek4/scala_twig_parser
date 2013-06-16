package org.apache.sling.scripting.twig

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.OneInstancePerTest
import org.scalatest.FunSpec

@RunWith(classOf[JUnitRunner])
class TwigScripEngineSuite extends FunSpec with OneInstancePerTest {
  val factory = new TwigScriptEngineFactory
  val engine = factory.getScriptEngine

  describe("An Engine") {
    it("should be able to parse an empty string") {
      val entry = ""
      val result = engine.eval("").asInstanceOf[String]
      assert(entry == result)
    }

    it("should be able to parse pure HTML") {
      val entry = "<html><head><title>Foo</title></head><body></body></html>"
      val result = engine.eval(entry).asInstanceOf[String]
      assert(entry == result)
    }
  }
}