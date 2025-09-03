package com.example.liftoff.storage.filesystem

import com.example.liftoff.error.{StorageDirectoryCreationError, StorageError, StorageFileCopyError}
import com.example.liftoff.storage.Storage

import java.io.{IOException, InputStream}
import java.nio.file.{Files, Path, Paths, StandardCopyOption}

class FileStorage(rootDirectory: Path) extends Storage {
  override def storeFile(file: InputStream, fileName: String, fileDirectoryName: Option[String]): Option[StorageError] = {
    val directoryPath = Paths.get(this.rootDirectory.toString, fileDirectoryName.getOrElse(""))
    if (fileDirectoryName.isDefined && !Files.exists(directoryPath))
      try
        Files.createDirectories(directoryPath)
      catch {
        case ex: IOException => return Some(StorageDirectoryCreationError(ex.getMessage))
      }
    try
      Files.copy(file, Paths.get(directoryPath.toString, fileName), StandardCopyOption.REPLACE_EXISTING)
    catch {
      case ex: IOException => return Some(StorageFileCopyError(ex.getMessage))
    }
    None
  }
}
