package com.funlabyrinthe.editor.reflect

import scala.reflect.runtime.universe._

class FieldIRData(instance: InstanceMirror, fir: FieldIR) extends InspectedData {
  override val name: String = fir.name
  override val tpe: Type = fir.tpe

  override val isReadOnly = !fir.hasSetter

  override def value: Any = {
    if (fir.hasGetter) instance.reflectMethod(fir.getter.get.asMethod)()
    else fieldMirror.get
  }

  override def value_=(v: Any): Unit = {
    if (fir.hasSetter) instance.reflectMethod(fir.setter.get.asMethod)(v)
    else fieldMirror.set(v)
  }

  private def fieldMirror = instance.reflectField(fir.field.get)
}
