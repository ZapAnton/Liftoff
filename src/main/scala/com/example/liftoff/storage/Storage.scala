package com.example.liftoff.storage

import com.example.liftoff.error.StorageError

import java.io.InputStream

trait Storage {
  def storeFile(file: InputStream, fileName: String, fileDirectoryName: Option[String]): Option[StorageError]
}
