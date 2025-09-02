package com.example.liftoff

import com.example.liftoff.command.Command

/** Application main entry point.
 * Constructs a [[com.example.liftoff.command.Command]] object from the provided cli arguments.
 * If there are no logical error in the arguments provided run the built command.
 */
object LiftoffApp extends App {
  private def run(): Unit = {
    val result = Command.fromCLIArguments(args)
    result.fold(
      commandError => Console.err.println(commandError.message),
      command => if (command.isValid) {
        command.execute()
      }
    )
  }

  run()
}