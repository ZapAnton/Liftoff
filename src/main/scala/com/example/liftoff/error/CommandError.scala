package com.example.liftoff.error

sealed trait CommandError {
  def message: String
}

object NoArgumentsError extends CommandError {
  override def message: String = "Command argument expected. For usage help use the 'sbt \"run help\"' command"
}

object PullCommandArgumentsError extends CommandError {
  override def message: String = "The 'pull' command expects at least 2 arguments. For usage help use the 'sbt \"run help\"' command"
}