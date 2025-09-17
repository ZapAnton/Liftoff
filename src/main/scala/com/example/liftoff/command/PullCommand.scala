package com.example.liftoff.command

import com.example.liftoff.extractor.email.imap.ImapGmailExtractor
import com.example.liftoff.extractor.email.EmailExtractor
import com.example.liftoff.extractor.email.gmail.GmailExtractor
import com.example.liftoff.storage.Storage
import com.example.liftoff.storage.filesystem.FileStorage

import java.nio.file.{Files, Path, Paths}

class PullCommand(email: String, credentialPath: String, rootDirectory: Option[String]) extends Command {
  private val defaultDestinationDirectory = "email_attachments_downloads"

  override def isValid: Boolean = {
    val rootDirectoryExists = this.rootDirectory
      .forall(path => {
        val exists = Files.exists(Paths.get(path))
        if (!exists) Console.err.println(s"Provided destination path $path for the downloaded attachments does not exist.\nPlease make sure that the provided path is valid and exists in the file system")
        exists
      })
    val credentialFileExists = Files.exists(Paths.get(credentialPath))
    if (!credentialFileExists) {
      Console.err.println(s"Provided credential path $credentialPath does not exist.\nPlease make sure that the provided path is valid and exists in the file system")
    }
    rootDirectoryExists && credentialFileExists
  }

  private def chooseSpecificEmailExtractor(fileStorage: Storage): EmailExtractor = this.email.split("@").last match {
        case "gmail.com" => new GmailExtractor(this.email, this.credentialPath, fileStorage)
    //    case "outlook.com" => new OutlookExtractor(this.email, this.userToken, fileStorage)
//    case _ => new ImapGmailExtractor(this.email, this.userToken, fileStorage)
  }

  private def chooseStorage(rootPath: Path): Storage = {
    new FileStorage(rootPath.toAbsolutePath)
  }

  override def execute(): Unit = {
    val rootPath: Path = this.rootDirectory
      .map(Paths.get(_).toAbsolutePath)
      .getOrElse(Paths.get("", this.defaultDestinationDirectory))
    println(s"Saving extracted email attachments to: $rootPath")
    val storage = this.chooseStorage(rootPath)
    val emailExtractor = this.chooseSpecificEmailExtractor(storage)
    emailExtractor
      .authenticate()
      .orElse(emailExtractor.extract())
      .orElse(emailExtractor.close())
      .foreach(error => Console.err.println(error.message))
  }
}
