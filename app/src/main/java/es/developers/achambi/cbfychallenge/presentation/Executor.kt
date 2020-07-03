package es.developers.achambi.cbfychallenge.presentation

import android.os.Handler
import android.os.Looper
import java.lang.Exception
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

interface Request<T> {
    fun perform(): T
}

interface SuccessHandler<T> {
    fun onSuccess(response: T)
}

interface ErrorHandler {
    fun onError()
}

interface BaseExecutor {
    fun <T> executeRequest(request: Request<T>, successHandler: SuccessHandler<T>?,
                           errorHandler: ErrorHandler?)
}

class Executor private constructor(corePoolSize: Int, maximumPoolSize: Int, keepAliveTime: Long,
                                   unit: TimeUnit, workQueue: LinkedBlockingQueue<Runnable>)
    : ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue),
    BaseExecutor {

    companion object {
        //TODO why
        private const val KEEP_ALIVE_TIME: Long = 0
        private const val THREAD_NUMBERS = 4
        private val MAIN_HANDLER = Handler(Looper.getMainLooper())

        fun buildExecutor(): Executor {
            return Executor(
                THREAD_NUMBERS,
                THREAD_NUMBERS,
                KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                LinkedBlockingQueue()
            )
        }
    }

    override fun <T> executeRequest(
        request: Request<T>,
        successHandler: SuccessHandler<T>?,
        errorHandler: ErrorHandler?
    ) {
        execute {
            try {
                val response = request.perform()
                MAIN_HANDLER.post{successHandler?.onSuccess(response)}
            }catch (e: Exception) {
                MAIN_HANDLER.post { errorHandler?.onError() }
            }
        }
    }

}