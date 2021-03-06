package com.funlabyrinthe.mazes
package std

import com.funlabyrinthe.core._

import scala.annotation.tailrec

trait Turnstile extends Effect {
  var pairingTurnstile: Turnstile = this

  def nextDirection(dir: Direction): Direction

  override def execute(context: MoveContext) = {
    import context._
    import player._

    if (!player.direction.isEmpty) {
      temporize()

      def loop(dir: Direction): Unit @control = {
        // Unfortunate duplicate of Player.move()
        // But then ... turnstiles are deeply interacting, so it's expected
        if (playState == Player.PlayState.Playing) {
          val dest = position.get +> dir
          val context = new MoveContext(player, Some(dest), keyEvent)

          direction = Some(dir)
          if (testMoveAllowed(context)) {
            if (position == context.src)
              moveTo(context)
          } else {
            // blocked over there, loop to next direction
            if (position == Some(pos))
              loop(nextDirection(dir))
          }
        }
      }

      loop(nextDirection(player.direction.get.opposite))
    }
  }

  override def exited(context: MoveContext) = {
    context.pos() += pairingTurnstile
  }
}

trait DirectTurnstile extends Turnstile {
  override def nextDirection(dir: Direction) = dir.left
}

trait IndirectTurnstile extends Turnstile {
  override def nextDirection(dir: Direction) = dir.right
}
