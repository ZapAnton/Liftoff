package com.example.liftoff.storage.filesystem

import com.example.liftoff.error.{StorageDirectoryCreationError, StorageError, StorageFileCopyError}
import com.example.liftoff.storage.Storage
import zio.{IO, ZIO}

import java.io.InputStream
import java.nio.file.{Files, Path, Paths, StandardCopyOption}

class FileStorage(rootDirectory: Path) extends Storage {
  override def storeFile(file: InputStream, fileName: String, fileDirectoryName: Option[String]): IO[StorageError, Unit] = {
    for {
      directoryPath <- ZIO.succeed(Paths.get(this.rootDirectory.toString, fileDirectoryName.getOrElse("")))
      _ <- ZIO.when(fileDirectoryName.isDefined && !Files.exists(directoryPath))(ZIO.attemptBlocking(Files.createDirectories(directoryPath)).mapError(error => StorageDirectoryCreationError(error.getMessage)))
      _ <- ZIO.attemptBlocking(Files.copy(file, Paths.get(directoryPath.toString, fileName), StandardCopyOption.REPLACE_EXISTING)).mapError(error => StorageFileCopyError(error.getMessage))
    } yield ()
  }
}
