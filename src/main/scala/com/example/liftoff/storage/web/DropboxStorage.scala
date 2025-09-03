package com.example.liftoff.storage.web

import com.example.liftoff.storage.{Storage, StorageError}

import java.io.InputStream

class DropboxStorage extends Storage {
  override def storeFile(file: InputStream, fileName: String, fileDirectoryName: Option[String]): Option[StorageError] = ???
}
