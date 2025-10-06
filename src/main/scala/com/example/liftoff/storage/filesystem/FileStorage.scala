package com.example.liftoff.storage.filesystem

import com.example.liftoff.error.{StorageDirectoryCreationError, StorageError, StorageFileCopyError}
import com.example.liftoff.storage.Storage
import zio.stream.{ZSink, ZStream}
import zio.{IO, URLayer, ZIO, ZLayer}

import java.io.InputStream
import java.nio.file.{Files, Path, Paths}

class FileStorage(rootDirectory: Path) extends Storage {
  override def storeFile(fileStream: InputStream, fileName: String, fileDirectoryName: Option[String]): IO[StorageError, Unit] = {
    for {
      directoryPath <- ZIO.succeed(Paths.get(this.rootDirectory.toString, fileDirectoryName.getOrElse("")))
      fileDirectoryWasCreated = fileDirectoryName.isEmpty || Files.exists(directoryPath)
      _ <- ZIO.when(!fileDirectoryWasCreated)(
        ZIO.attemptBlocking(Files.createDirectories(directoryPath))
          .mapError(error => StorageDirectoryCreationError(error.getMessage))
      )
      filePath = Paths.get(directoryPath.toString, fileName)
      _ <- ZStream
        .fromInputStream(fileStream)
        .run(ZSink.fromPath(filePath))
        .mapError(error => StorageFileCopyError(error.getMessage))
    } yield ()
  }
}

object FileStorage {
  val layer: URLayer[Path, Storage] = ZLayer.fromFunction(new FileStorage(_))
}
