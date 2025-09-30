package com.example.liftoff.command

import zio.{Console, UIO, ZIO}

class UnknownCommand extends Command {

  override def isValid: UIO[Boolean] = ZIO.succeed(true)

  override def execute: UIO[Unit] = Console
    .printError("Unknown command received. For usage help use the 'sbt \"run help\"' command")
    .orElse(ZIO.succeed())
}
