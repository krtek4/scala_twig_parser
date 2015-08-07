package org.apache.sling.scripting.twig.elements

import scala.Array.canBuildFrom

object Filter {
  def pkg = "org.apache.sling.scripting.twig.elements."
  def className(name: String) = name.split("_").map(_.capitalize).mkString("").replace("()", "")
  def apply(name: String): Filter = Class.forName(pkg + className(name)).newInstance.asInstanceOf[Filter]
}

object ParameteredFilter extends Filter {
  def apply(name: String, parameters: Any): ParameteredFilter =
    Class.forName(Filter.pkg + Filter.className(name)).newInstance.asInstanceOf[ParameteredFilter]
}

abstract class Filter extends Token {
  def filter(value: Any): Any = value
}
case class Abs extends Filter {
  override def filter(value: Any) = value match {
    case d: Double => math.abs(d)
    case s: String => filter(s.toDouble)
    case _ => value
  }
}
case class Capitalize extends Filter {
  override def filter(value: Any) = value match {
    case s: String => s.capitalize
    case _ => value
  }
}
case class Escape extends Filter
case class First extends Filter {
  override def filter(value: Any) = value match {
    case l: List[_] => l.head
    case _ => value
  }
}
case class Keys extends Filter
case class Last extends Filter
case class Length extends Filter
case class Lower extends Filter
case class Nl2br extends Filter
case class Raw extends Filter
case class Reverse extends Filter
case class Sort extends Filter
case class Striptags extends Filter
case class Title extends Filter
case class Trim extends Filter
case class Upper extends Filter
case class UrlEncode extends Filter
case class JsonEncode extends Filter

abstract class ParameteredFilter extends Filter
case class Batch extends ParameteredFilter
case class ConvertEncoding extends ParameteredFilter
case class Date extends ParameteredFilter
case class DateModify extends ParameteredFilter
case class Default extends ParameteredFilter
case class Format extends ParameteredFilter
case class Join extends ParameteredFilter
case class Merge extends ParameteredFilter
case class NumberFormat extends ParameteredFilter
case class Replace extends ParameteredFilter
case class Slice extends ParameteredFilter
case class Split extends ParameteredFilter
