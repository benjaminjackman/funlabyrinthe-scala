package com.funlabyrinthe.editor.pickling

import com.funlabyrinthe.editor.reflect._

import scala.reflect.runtime.universe._

class TempReadWriteData(val name: String, val tpe: Type,
    reprForErrorMessage: String => String) extends InspectedData {

  def this(name: String, tpe: Type, initialValue: Any) = {
    this(name, tpe, (_: String) => "") // callback will never be called
    myValue = Some(initialValue)
  }

  override val isReadOnly = false

  private var myValue: Option[Any] = None

  def value_=(v: Any): Unit = myValue = Some(v)

  def value: Any = myValue getOrElse {
    throw new UnsupportedOperationException(
        s"Ouch! Value for ${reprForErrorMessage(name)} has not yet been set")
  }
}
