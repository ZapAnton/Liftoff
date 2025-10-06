package com.example.liftoff.command

import com.example.liftoff.extractor.email.EmailExtractor
import com.example.liftoff.extractor.email.gmail.GmailExtractor
import com.example.liftoff.storage.Storage
import com.example.liftoff.storage.filesystem.FileStorage
import zio.{Console, UIO, ZIO, ZLayer}

import java.nio.file.{Files, Path, Paths}

class PullCommand(email: String, credentialPath: String, rootDirectory: Option[String]) extends Command {
  private val defaultDestinationDirectory = "email_attachments_downloads"

  private def rootDirectoryErrorMessage(path: String): String =
    s"Provided destination path $path for the downloaded attachments does not exist.\nPlease make sure that the provided path is valid and exists in the file system"

  private def credentialFileErrorMessage(path: String): String =
    s"Provided credential path $path does not exist.\nPlease make sure that the provided path is valid and exists in the file system"

  override def isValid: UIO[Boolean] = {
    for {
      rootDirectoryExists <- ZIO.fromOption(this.rootDirectory).foldZIO(
        _ => ZIO.succeed(false),
        path => ZIO.attempt(Files.exists(Paths.get(path))).foldZIO(
          _ => ZIO.succeed(false),
          exists => ZIO.succeed(exists)
        )
      )
      credentialFileExists <- ZIO.attempt(Files.exists(Paths.get(credentialPath))).foldZIO(
        _ => ZIO.succeed(false),
        exists => ZIO.succeed(exists)
      )
      _ <- ZIO.when(!rootDirectoryExists)(Console.printError(this.rootDirectoryErrorMessage(this.rootDirectory.getOrElse(""))).ignore)
      _ <- ZIO.when(!credentialFileExists)(Console.printError(this.credentialFileErrorMessage(credentialPath)).ignore)
      isValid <- ZIO.succeed(rootDirectoryExists && credentialFileExists)
    } yield isValid
  }

  private def chooseSpecificEmailExtractor(storage: Storage): UIO[EmailExtractor] = this.email.split("@").last match {
    case "gmail.com" => ZIO.succeed(new GmailExtractor(this.email, this.credentialPath, storage))
    //    case "outlook.com" => new OutlookExtractor(this.email, this.userToken, fileStorage)
    //    case _ => new ImapGmailExtractor(this.email, this.userToken, fileStorage)
  }

  private def chooseStorage(rootPath: Path): UIO[Storage] = {
    ZIO.succeed(new FileStorage(rootPath.toAbsolutePath))
  }

  override def execute: UIO[Unit] = {
    for {
      storagePath <- ZIO.fromOption(this.rootDirectory).foldZIO(
        _ => ZIO.succeed(Paths.get("", this.defaultDestinationDirectory)),
        path => ZIO.succeed(Paths.get(path).toAbsolutePath)
      )
      _ <- Console.printLine(s"Saving extracted email attachments to: $storagePath").ignore
      storage <- this.chooseStorage(storagePath)
      emailExtractor <- this.chooseSpecificEmailExtractor(storage)
      _ <- emailExtractor.authenticate().catchAll(error => Console.printError(error.message).ignore)
      _ <- emailExtractor.extract().catchAll(error => Console.printError(error.message).ignore)
      _ <- emailExtractor.close().catchAll(error => Console.printError(error.message).ignore)
    } yield ()
  }
}
