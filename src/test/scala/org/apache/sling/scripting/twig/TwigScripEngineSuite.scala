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
	  engine.eval("")
	}
  }
}