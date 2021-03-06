package com.funlabyrinthe
package mazes

import core._

import scala.collection.mutable

class ItemDef(implicit override val universe: MazeUniverse,
    originalID: ComponentID) extends NamedComponent {

  def this(id: ComponentID)(implicit universe: MazeUniverse) =
    this()(universe, id)

  import universe._

  object count extends Player.mutable.SimplePerPlayerData[Int](0)

  category = ComponentCategory("items", "Items")

  def shouldDisplay(player: Player): Boolean = true

  def displayText(player: Player): String =
    s"$name: ${count(player)}"

  def perform(player: Player): Player#Perform = PartialFunction.empty
}

object ItemDef {
  def all(implicit universe: Universe): IndexedSeq[ItemDef] =
    universe.components[ItemDef]
}
