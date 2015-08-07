package org.apache.sling.scripting.twig.elements

trait Token {

}

case class Literal(val value: String) extends Token