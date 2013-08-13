package com.funlabyrinthe.core

import scala.collection.mutable

import graphics._

abstract class Component(implicit val universe: Universe) {
  import universe._

  private var _id: String = computeDefaultID()
  private var _category: ComponentCategory = universe.DefaultCategory

  var icon: Painter = EmptyPainter

  universe.componentAdded(this)

  final def id: String = _id
  final def id_=(value: String) {
    if (value != _id) {
      require(Component.isValidIDOpt(value),
          s"'${value}' is not a valid component identifier")

      require(!universe.componentIDExists(value),
          s"Duplicate component identifier '${value}'")

      val old = _id
      _id = value

      universe.componentIDChanged(this, old, value)

      onIDChanged(old, _id)
    }
  }

  final def category: ComponentCategory = _category
  final protected def category_=(value: ComponentCategory) {
    _category = value
  }

  def onIDChanged(oldID: String, newID: String): Unit = ()

  override def toString() = id

  def drawIcon(context: DrawContext) {
    if (icon != EmptyPainter)
      icon.drawTo(context)
    else
      DefaultIconPainter.drawTo(context)
  }

  protected[this] def computeDefaultID(): String = {
    val (base, tryWithoutSuffix) = computeDefaultIDBase()

    if (base.isEmpty()) base
    else if (tryWithoutSuffix && !universe.componentIDExists(base)) base
    else {
      var suffix = 1
      while (universe.componentIDExists(base+suffix))
        suffix += 1
      base+suffix
    }
  }

  protected[this] def computeDefaultIDBase(): (String, Boolean) = {
    val simpleName = scalaFriendlyClassSimpleName(getClass)

    if (simpleName.isEmpty()) ("", false)
    else if (simpleName.last == '$') (simpleName.init, true)
    else (simpleName, false)
  }

  private def scalaFriendlyClassSimpleName(cls: Class[_]): String = {
    def isAsciiDigit(c: Char) = '0' <= c && c <= '9'

    val enclosingCls = cls.getEnclosingClass
    val clsName = cls.getName

    if (enclosingCls eq null) {
      // Strip the package name
      clsName.substring(clsName.lastIndexOf('.')+1)
    } else {
      // Strip the enclosing class name and any leading "\$?[0-9]*"
      val length = clsName.length
      var start = enclosingCls.getName.length
      if (start < length && clsName.charAt(start) == '$')
        start += 1
      while (start < length && isAsciiDigit(clsName.charAt(start)))
        start += 1
      clsName.substring(start) // will be "" for an anonymous class
    }
  }
}

object Component {
  val IconWidth = 48
  val IconHeight = 48

  def isValidID(id: String): Boolean = {
    !id.isEmpty
    //(!id.isEmpty() && isIDStart(id.charAt(0)) && id.forall(isIDPart))
  }

  def isValidIDOpt(id: String): Boolean =
    true
    //id.isEmpty() || isValidID(id)

  //def isIDStart(c: Char) = c.isUnicodeIdentifierStart
  //def isIDPart(c: Char) = c.isUnicodeIdentifierPart || c == '#'
}
