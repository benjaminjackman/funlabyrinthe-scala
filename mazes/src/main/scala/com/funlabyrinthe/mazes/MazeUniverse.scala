package com.funlabyrinthe
package mazes

import core._

trait MazeUniverse extends Universe {
  val mazes = new Mazes

  override def initialize() {
    super.initialize()
    mazes.initialize()
  }
}
