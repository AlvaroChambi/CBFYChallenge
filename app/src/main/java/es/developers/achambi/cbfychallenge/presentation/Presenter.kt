package es.developers.achambi.cbfychallenge.presentation

import androidx.lifecycle.Lifecycle

interface Screen

abstract class Presenter<T: Screen>(protected val screen: T,
                                                                                                        private val lifecycle: Lifecycle,
                                                                                                        private val executor: BaseExecutor
) {

    protected fun <D>perform(request: Request<D>, successHandler: SuccessHandler<D>? = null,
                             errorHandler: ErrorHandler? = null) {
        executor.executeRequest(request,
            SuccessLifecycleHandler(
                lifecycle,
                successHandler
            ),
            ErrorLifecycleHandler(
                lifecycle,
                errorHandler
            )
        )
    }
}

//TODO: why?
abstract class SuccessHandlerDecorator<T>(private val successHandler: SuccessHandler<T>?)
    : SuccessHandler<T> {
    override fun onSuccess(response: T) {
        successHandler?.onSuccess(response)
    }
}

abstract class ErrorHandlerDecorator(private val errorHandler: ErrorHandler?):
    ErrorHandler {
    override fun onError() {
        errorHandler?.onError()
    }
}

class SuccessLifecycleHandler<T>(private val lifecycle: Lifecycle, successHandler: SuccessHandler<T>?)
    : SuccessHandlerDecorator<T>(successHandler) {
    override fun onSuccess(response: T) {
        if(lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            super.onSuccess(response)
        }
    }
}

class ErrorLifecycleHandler(private val lifecycle: Lifecycle, errorHandler: ErrorHandler?)
    : ErrorHandlerDecorator(errorHandler){
    override fun onError() {
        if(lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            super.onError()
        }
    }
}