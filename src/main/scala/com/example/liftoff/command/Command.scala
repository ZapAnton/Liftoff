package com.example.liftoff.command

import com.example.liftoff.error.{CommandError, NoArgumentsError, PullCommandArgumentsError}
import zio.{IO, UIO, ZIO}

trait Command {
  def isValid: UIO[Boolean]

  def execute: UIO[Unit]
}

object Command {
  private def parseCommand(commandStr: String, rest: Array[String]): IO[CommandError, Command] = commandStr match {
    case "help" => ZIO.succeed(new HelpCommand())
    case "pull" =>
      if (rest.length < 2) {
        ZIO.fail(PullCommandArgumentsError)
      } else {
        val pullCommand = new PullCommand(
          email = rest(0),
          credentialPath = rest(1),
          rootDirectory = rest.lift(2),
        )
        ZIO.succeed(pullCommand)
      }
    case _ => ZIO.succeed(new UnknownCommand())
  }

  def fromCLIArguments(cliArguments: Array[String]): IO[CommandError, Command] = {
    for {
      firstArgument <- ZIO.fromOption(cliArguments.headOption).orElse(ZIO.fail(NoArgumentsError))
      command <- parseCommand(firstArgument, cliArguments.drop(1))
    } yield command
  }
}