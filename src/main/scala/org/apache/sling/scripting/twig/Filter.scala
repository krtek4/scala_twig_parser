package org.apache.sling.scripting.twig

object Filter {
  def className(name: String) = name.split("_").map(_.capitalize).mkString("").replace("()", "")
  def apply(name: String): Filter = Class.forName("org.apache.sling.scripting.twig." + className(name)).newInstance.asInstanceOf[Filter]
}

object ParameteredFilter extends Filter {
  def apply(name: String, parameters: Any): ParameteredFilter =
    Class.forName("org.apache.sling.scripting.twig." + Filter.className(name)).newInstance.asInstanceOf[ParameteredFilter]
}

abstract class Filter
case class Abs extends Filter
case class Capitalize extends Filter
case class Escape extends Filter
case class First extends Filter
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
