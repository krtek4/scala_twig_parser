package org.apache.sling.scripting.twig

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSpec
import org.scalatest.OneInstancePerTest

@RunWith(classOf[JUnitRunner])
class TwigScripEngineFactorySuite extends FunSpec with OneInstancePerTest {
  val factory = new TwigScriptEngineFactory

  describe("A Factory") {
    it("should return a TwigScriptEngine") {
      assert(factory.getScriptEngine.isInstanceOf[TwigScriptEngine])
    }
  }
}