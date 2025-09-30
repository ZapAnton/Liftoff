package com.example.liftoff.storage.web

import com.example.liftoff.error.StorageError
import com.example.liftoff.storage.Storage
import zio.IO

import java.io.InputStream

class DropboxStorage extends Storage {
  override def storeFile(file: InputStream, fileName: String, fileDirectoryName: Option[String]): IO[StorageError, Unit] = ???
}
