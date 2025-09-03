package com.example.liftoff.storage

sealed trait StorageError {
  def message: String
}

sealed case class StorageDirectoryCreationError(errorText: String) extends StorageError {
  override def message: String = s"Failed to create a directory to store file: $errorText"
}

sealed case class StorageFileCopyError(errorText: String) extends StorageError {
  override def message: String = s"Failed to copy file to the filesystem: $errorText"
}
