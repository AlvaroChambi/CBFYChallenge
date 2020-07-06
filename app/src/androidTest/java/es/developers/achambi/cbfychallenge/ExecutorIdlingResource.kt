package es.developers.achambi.cbfychallenge

import androidx.test.espresso.IdlingResource
import es.developers.achambi.cbfychallenge.presentation.Executor

class ExecutorIdlingResource(private val executor: Executor): IdlingResource {
    var isIdle = false
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String? {
        return ExecutorIdlingResource::class.java.name
    }

    override fun isIdleNow(): Boolean {
        isIdle = executor.activeCount == 0
        if (isIdle) {
            resourceCallback?.onTransitionToIdle()
        }
        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        resourceCallback = callback
    }
}