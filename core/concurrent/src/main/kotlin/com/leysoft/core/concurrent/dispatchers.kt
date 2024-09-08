package com.leysoft.core.concurrent

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

val Virtual: CoroutineDispatcher =
    Executors
        .newVirtualThreadPerTaskExecutor()
        .asCoroutineDispatcher()

val IO: CoroutineDispatcher = Dispatchers.IO

@OptIn(ExperimentalCoroutinesApi::class)
val IOWithLimit: (Int) -> CoroutineDispatcher =
    { limit -> Dispatchers.IO.limitedParallelism(limit) }

val SequentialIO: CoroutineDispatcher = IOWithLimit(1)

val Default: CoroutineDispatcher = Dispatchers.Default
