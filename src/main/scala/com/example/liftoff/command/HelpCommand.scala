package com.example.liftoff.command

import zio.{Console, UIO, ZIO}

class HelpCommand extends Command {
  private val usage =
    """Liftoff
An application that downloads email attachments from the provided email address.
Available commands:
    help - prints this message
    pull <your email address> <your gmail application password> [destination directory for the downloaded attachments] - download email ettachments
Examples:
    sbt "run pull my_email@example.com \"some application password\"" - will download all of the attachments to the default directory "./email_attachments_downloads"
    sbt "run pull my_email@example.com \"some application password\" /path/to/download/directory" - will download all of the attachments to the provided directory  """

  override def isValid: UIO[Boolean] = ZIO.succeed(true)

  override def execute: UIO[Unit] = Console.printLine(this.usage).orElse(ZIO.succeed())
}
