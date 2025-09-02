package com.example.liftoff.command

class UnknownCommand extends Command {

  override def isValid: Boolean = true

  override def execute(): Unit = Console.err.println("Unknown command received. For usage help use the 'sbt \"run help\"' command")
}
