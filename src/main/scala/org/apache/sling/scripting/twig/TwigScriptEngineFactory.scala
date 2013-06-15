package org.apache.sling.scripting.twig

import java.util.Collections
import java.util.List

import javax.script.ScriptEngine
import javax.script.ScriptEngineFactory

object TwigScriptEngineFactory {
  private val NL = System.getProperty("line.separator");
  
  val ENGINE_NAME = "Twig Scripting Engine"
  val LANGUAGE_VERSION = "1.13.1"
  val ENGINE_VERSION = "0.1/scala " + LANGUAGE_VERSION
  val EXTENSIONS = Collections.singletonList("twig")
  val LANGUAGE_NAME = "Twig"
  val MIME_TYPES = Collections.singletonList("application/x-twig")
  val NAMES = Collections.singletonList("twig")
}

/**
 * JSR 223 compliant {@link ScriptEngineFactory} for Twig.
 */
class TwigScriptEngineFactory extends ScriptEngineFactory {
  import TwigScriptEngineFactory._
  
  def getEngineName: String =  ENGINE_NAME
  def getEngineVersion: String = ENGINE_VERSION
  def getExtensions: List[String] = EXTENSIONS
  def getLanguageName: String = LANGUAGE_NAME
  def getLanguageVersion: String = LANGUAGE_VERSION
  def getMimeTypes: List[String] = MIME_TYPES
  def getNames: List[String] = NAMES
  
  def getParameter(key: String): String = key.toUpperCase match {
    case ScriptEngine.ENGINE => getEngineName
    case ScriptEngine.ENGINE_VERSION => getEngineVersion
    case ScriptEngine.NAME => getNames.get(0)
    case ScriptEngine.LANGUAGE =>  getLanguageName
    case ScriptEngine.LANGUAGE_VERSION => getLanguageVersion
    case "threading" => "multithreaded" 
    case _ => null
  }
  
  def getMethodCallSyntax(obj: String, method: String, args: String*): String = obj + "." + method + "(" + args.mkString(",") + ")"

  def getOutputStatement(toDisplay: String): String = toDisplay

  def getProgram(statements: String*): String = statements.mkString(NL)

  def getScriptEngine: ScriptEngine = new TwigScriptEngine(this)
}