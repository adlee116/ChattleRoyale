package com.aleet.chattleroyale.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
