package es.developers.achambi.cbfychallenge.presentation

import android.content.Context
import androidx.lifecycle.Lifecycle
import es.developers.achambi.cbfychallenge.R
import es.developers.achambi.cbfychallenge.domain.Product
import es.developers.achambi.cbfychallenge.domain.ProductsUseCase
import javax.inject.Inject

class ProductsPresenter (screen: ProductsScreen, lifecycle: Lifecycle,
                                            executor: BaseExecutor,
                                            private val useCase: ProductsUseCase,
                         private val presentationBuilder: PresentationBuilder)
    : Presenter<ProductsScreen>(screen, lifecycle, executor) {

    fun onViewCreated() {
        perform(object : Request<List<Product>> {
            override fun perform(): List<Product> {
                return useCase.fetchProducts()
            }
        }, object : SuccessHandler<List<Product>> {
            override fun onSuccess(response: List<Product>) {
                screen.doSomething(presentationBuilder.build(response))
            }
        })
    }
}



class PresentationBuilder@Inject constructor(private val context: Context) {
    fun build(products: List<Product>): List<ProductPresentation> {
        val list = ArrayList<ProductPresentation>()
        products.forEach { list.add(build(it)) }
        return list
    }

    fun build(product: Product): ProductPresentation {
        return ProductPresentation (product.name,
            context.getString(R.string.price_format, product.price.toString()))
    }
}

class ProductPresentation(val name: String,
                          val price: String)

