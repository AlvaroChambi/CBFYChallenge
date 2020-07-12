package es.developers.achambi.cbfychallenge.presentation.cart

import androidx.lifecycle.Lifecycle
import es.developers.achambi.cbfychallenge.domain.CartProducts
import es.developers.achambi.cbfychallenge.domain.CartUseCase
import es.developers.achambi.cbfychallenge.presentation.Executor
import es.developers.achambi.cbfychallenge.presentation.Presenter
import es.developers.achambi.cbfychallenge.presentation.Request
import es.developers.achambi.cbfychallenge.presentation.SuccessHandler

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
}

