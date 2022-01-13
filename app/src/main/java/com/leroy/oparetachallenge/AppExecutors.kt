package com.leroy.oparetachallenge

import android.os.Handler
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors private constructor() {

    val diskIO: Executor
    val networkIO: Executor
    val mainThread: Executor

    init {
        Executors.newSingleThreadExecutor()
        diskIO = Executors.newSingleThreadExecutor()
        networkIO = Executors.newFixedThreadPool(3)
        mainThread = MainThreadExecutor()
    }

    private inner class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(App.handler.looper)
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    companion object {
        private val instance = AppExecutors()
        @JvmStatic
        fun get(): AppExecutors {
            return instance
        }
    }

}