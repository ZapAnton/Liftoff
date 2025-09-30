package com.example.liftoff

import com.example.liftoff.command.Command
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

/** Application main entry point.
 * Constructs a [[com.example.liftoff.command.Command]] object from the provided cli arguments.
 * If there are no logical error in the arguments provided run the built command.
 */
object LiftoffApp extends ZIOAppDefault {
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    for {
      cliArguments <- this.getArgs
      command <- Command.fromCLIArguments(cliArguments.toArray).foldZIO(
        error => ZIO.fail(error.message),
        command => ZIO.succeed(command)
      )
      isValid <- command.isValid
      _ <- ZIO.when(isValid) (command.execute)
    } yield ()
  }
}