package es.developers.achambi.cbfychallenge.presentation.cart

import androidx.lifecycle.Lifecycle
import es.developers.achambi.cbfychallenge.domain.CartProduct
import es.developers.achambi.cbfychallenge.domain.CartUseCase
import es.developers.achambi.cbfychallenge.presentation.Executor
import es.developers.achambi.cbfychallenge.presentation.Presenter
import es.developers.achambi.cbfychallenge.presentation.Request
import es.developers.achambi.cbfychallenge.presentation.SuccessHandler

class CartPresenter(screen: CartScreen, lifecycle: Lifecycle, executor: Executor,
                    private val useCase: CartUseCase, private val builder: CartItemBuilder)
    : Presenter<CartScreen>(screen, lifecycle, executor) {

    fun onViewCreated() {
        perform( object : Request<List<CartProduct>>{
            override fun perform(): List<CartProduct> {
                return useCase.fetchCartItems()
            }

        }, object : SuccessHandler<List<CartProduct>> {
            override fun onSuccess(response: List<CartProduct>) {
                screen.showCartItems( builder.build(response) )
            }

        })
    }
}

