package com.example.liftoff.command

import com.example.liftoff.extractor.email.{EmailExtractor, GmailExtractor, ImapGmailExtractor, OutlookExtractor}
import com.example.liftoff.storage.Storage
import com.example.liftoff.storage.filesystem.FileStorage

import java.nio.file.{Files, Path, Paths}

class PullCommand(email: String, userToken: String, rootDirectory: Option[String]) extends Command {
  private val defaultDestinationDirectory = "email_attachments_downloads"

  override def isValid: Boolean = this.rootDirectory
    .forall(path => {
      val exists = Files.exists(Paths.get(path))
      if (!exists) Console.err.println(s"Provided destination path $path for the downloaded attachments does not exist.\nPlease make sure that the provided path is valid and exists in the file system")
      exists
    })

  private def chooseSpecificEmailExtractor(fileStorage: Storage): EmailExtractor = {
    var extractor: EmailExtractor = new ImapGmailExtractor(this.email, this.userToken, fileStorage)
    if (this.email.endsWith("@gmail.com")) {
      extractor = new GmailExtractor(this.email, this.userToken, fileStorage)
    } else if (email.endsWith("@outlook.com")) {
        extractor = new OutlookExtractor(this.email, this.userToken, fileStorage)
    }
    // TODO: Since only ImapGmailAPI is implemented, fallback to it as a default. Remove after implementing GmailExtractor and OutlookExtractor
    extractor = new ImapGmailExtractor(this.email, this.userToken, fileStorage)
    extractor
  }

  private def chooseStorage(rootPath: Path): Storage = {
    new FileStorage(rootPath.toAbsolutePath)
  }

  override def execute(): Unit = {
    val rootPath: Path = this.rootDirectory
      .map(Paths.get(_).toAbsolutePath)
      .getOrElse(Paths.get("", this.defaultDestinationDirectory))
    println(s"Saving extracted email attachments to: $rootPath")
    val storage = chooseStorage(rootPath)
    val emailExtractor = this.chooseSpecificEmailExtractor(storage)
    try {
      emailExtractor.authenticate()
      emailExtractor.extract()
      emailExtractor.close()
    } catch {
      case e: Exception => Console.err.println(e.getMessage)
    }
  }
}
