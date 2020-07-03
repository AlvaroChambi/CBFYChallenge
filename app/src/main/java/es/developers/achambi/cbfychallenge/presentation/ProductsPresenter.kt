package es.developers.achambi.cbfychallenge.presentation

import androidx.lifecycle.Lifecycle
import es.developers.achambi.cbfychallenge.domain.Product
import es.developers.achambi.cbfychallenge.domain.ProductsUseCase
import javax.inject.Inject

class ProductsPresenter (screen: ProductsScreen, lifecycle: Lifecycle,
                                            executor: BaseExecutor,
                                            private val useCase: ProductsUseCase)
    : Presenter<ProductsScreen>(screen, lifecycle, executor) {

    fun onViewCreated() {
        perform(object : Request<List<Product>> {
            override fun perform(): List<Product> {
                return useCase.fetchProducts()
            }
        }, object : SuccessHandler<List<Product>> {
            override fun onSuccess(response: List<Product>) {
                screen.doSomething(response.size.toString())
            }
        })
    }
}