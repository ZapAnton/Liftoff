package com.example.liftoff.storage.web

import com.example.liftoff.error.StorageError
import com.example.liftoff.storage.Storage
import zio.{IO, ULayer, ZLayer}

import java.io.InputStream

class OneDriveStorage extends Storage {

  override def storeFile(file: InputStream, fileName: String, fileDirectoryName: Option[String]): IO[StorageError, Unit] = ???
}

object OneDriveStorage {
  val layer: ULayer[Storage] = ZLayer.succeed(new OneDriveStorage)
}
