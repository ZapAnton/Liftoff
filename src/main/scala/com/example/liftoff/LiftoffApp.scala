package com.example.liftoff

import com.example.liftoff.command.CommandManager

/** Application main entry point.
 * Constructs a [[com.example.liftoff.command.Command]] object from the provided cli arguments.
 * If there are no logical error in the arguments provided run the built command.
 */
object LiftoffApp extends App {
  private def run(): Unit = {
    try {
      val command = CommandManager.buildFromCliArguments(args)
      if (!command.isValid) {
        return
      }
      command.execute()
    } catch {
      case ex: IllegalArgumentException => Console.err.println(ex.getMessage)
    }
  }

  run()
}