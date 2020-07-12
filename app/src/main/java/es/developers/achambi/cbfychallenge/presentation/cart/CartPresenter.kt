package es.developers.achambi.cbfychallenge.presentation.cart

import androidx.lifecycle.Lifecycle
import es.developers.achambi.cbfychallenge.domain.CartProducts
import es.developers.achambi.cbfychallenge.domain.CartUseCase
import es.developers.achambi.cbfychallenge.presentation.*

class CartPresenter(screen: CartScreen, lifecycle: Lifecycle, executor: Executor,
                    private val useCase: CartUseCase,
                    private val builder: CartPresentationBuilder)
    : Presenter<CartScreen>(screen, lifecycle, executor) {

    fun onViewCreated() {
        perform( object : Request<CartProducts>{
            override fun perform(): CartProducts {
                return useCase.fetchCartItems()
            }

        }, object : SuccessHandler<CartProducts> {
            override fun onSuccess(response: CartProducts) {
                screen.showCartItems( builder.build(response) )
            }

        })
    }

    fun increaseSelected(id: Long) {
        perform( object : Request<CartProducts> {
            override fun perform(): CartProducts {
                return useCase.increaseProductCount(id)
            }
        }, object : SuccessHandler<CartProducts> {
            override fun onSuccess(response: CartProducts) {
                screen.showCartItems(builder.build(response))
            }
        }, object : ErrorHandler {
            override fun onError() {
                screen.showUpdateError()
            }
        })
    }

    fun decreaseSelected(id: Long) {
        perform( object : Request<CartProducts> {
            override fun perform(): CartProducts {
                return useCase.decreaseProductCount(id)
            }
        }, object : SuccessHandler<CartProducts> {
            override fun onSuccess(response: CartProducts) {
                screen.showCartItems(builder.build(response))
            }
        }, object : ErrorHandler {
            override fun onError() {
                screen.showUpdateError()
            }
        })
    }

    fun removeItem(id: Long) {
        perform( object : Request<CartProducts> {
            override fun perform(): CartProducts {
                return useCase.remove(id)
            }
        }, object : SuccessHandler<CartProducts> {
            override fun onSuccess(response: CartProducts) {
                screen.showCartItems(builder.build(response))
            }
        }, object : ErrorHandler {
            override fun onError() {
                screen.showUpdateError()
            }
        })
    }
}

