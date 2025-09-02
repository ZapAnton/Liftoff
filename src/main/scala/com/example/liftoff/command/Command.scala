package com.example.liftoff.command

trait Command {
  def isValid: Boolean

  def execute(): Unit
}

object Command {
  private def parseCommand(commandStr: String, rest: Array[String]): Either[CommandError, Command] = commandStr match {
    case "help" => Right(new HelpCommand())
    case "pull" =>
      if (rest.length < 2) {
        Left(PullCommandArgumentsError)
      } else {
        val pullCommand = new PullCommand(
          email = rest(0),
          userToken = rest(1),
          rootDirectory = rest.lift(2),
        )
        Right(pullCommand)
      }
    case _ => Right(new UnknownCommand())
  }


  def fromCLIArguments(cliArguments: Array[String]): Either[CommandError, Command] = {
    cliArguments.headOption
      .map(parseCommand(_, cliArguments.drop(1)))
      .getOrElse(Left(NoArgumentsError))
  }
}