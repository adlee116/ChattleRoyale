package com.squadsandshots_android.core.utils

import kotlinx.coroutines.*

abstract class EmptyParamsUseCase<out Type> {
    var enableTesting: Boolean = false
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main

    abstract suspend fun run(): Type

    open operator fun invoke(
        scope: CoroutineScope,
        onResult: (Type) -> Unit = {}
    ) {
        scope.launch {
            val job =
                if (enableTesting) withContext(scope.coroutineContext) { run() }
                else withContext(ioDispatcher) { run() }
            withContext(scope.coroutineContext) { onResult(job) }
        }
    }
}
