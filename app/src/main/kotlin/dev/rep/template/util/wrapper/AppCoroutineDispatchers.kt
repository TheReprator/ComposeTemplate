package dev.rep.template.util.wrapper

import kotlinx.coroutines.CoroutineDispatcher

data class AppCoroutineDispatchers(
  val main: CoroutineDispatcher,
  val io: CoroutineDispatcher,
  val computation: CoroutineDispatcher,
  val singleThread: CoroutineDispatcher,
  val databaseRead: CoroutineDispatcher
)