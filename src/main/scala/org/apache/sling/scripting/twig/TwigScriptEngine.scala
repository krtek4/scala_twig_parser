package org.apache.sling.scripting.twig

import javax.script.AbstractScriptEngine
import javax.script.ScriptContext
import java.io.Reader
import javax.script.ScriptEngineFactory
import javax.script.Bindings
import javax.script.SimpleBindings
import java.io.BufferedReader
import java.io.IOException
import javax.script.ScriptException

object TwigScriptEngine {
  private val NL = System.getProperty("line.separator");
}

class TwigScriptEngine(val factory: TwigScriptEngineFactory) extends AbstractScriptEngine {
  import TwigScriptEngine._

  val parser = new TwigParsers

  def getFactory: ScriptEngineFactory = factory

  def createBindings: Bindings = new SimpleBindings

  def eval(reader: Reader, context: ScriptContext): Object = {
    val script = new StringBuilder;
    try {
      val bufferedScript = new BufferedReader(reader);
      var nextLine = bufferedScript.readLine
      while (nextLine != null) {
        script.append(nextLine)
        script.append(NL)
        nextLine = bufferedScript.readLine
      }
    } catch {
      case e: IOException => throw new ScriptException(e)
    }

    eval(script.toString, context)
  }

  def eval(script: String, context: ScriptContext): Object = parser(script)
}