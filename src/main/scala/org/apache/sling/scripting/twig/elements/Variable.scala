package org.apache.sling.scripting.twig.elements

import javax.script.Bindings

sealed trait ValueHolder extends Token {
  def value(bindings: Bindings): Any;
}

case class Concatenation(val values: List[ValueHolder]) extends ValueHolder {
  def value(bindings: Bindings) = values.map(_.value(bindings)).mkString
}

case class Value(val value: String) extends ValueHolder {
  def value(bindings: Bindings) = value
}

abstract class Variable(val name: String) extends ValueHolder {
  def value(bindings: Bindings) = bindings.get(name).toString
}
case class SimpleVariable(override val name: String) extends Variable(name)
case class AccessorVariable(override val name: String, val accessors: List[String]) extends Variable(name)

case class FilteredValueHolder(val value: ValueHolder, val filters: List[Filter]) extends ValueHolder {
  def value(bindings: Bindings) = value.value(bindings)
}