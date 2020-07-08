package es.developers.achambi.cbfychallenge.presentation.products

import android.content.Context
import androidx.lifecycle.Lifecycle
import es.developers.achambi.cbfychallenge.R
import es.developers.achambi.cbfychallenge.domain.Product
import es.developers.achambi.cbfychallenge.domain.ProductsUseCase
import es.developers.achambi.cbfychallenge.presentation.BaseExecutor
import es.developers.achambi.cbfychallenge.presentation.Presenter
import es.developers.achambi.cbfychallenge.presentation.Request
import es.developers.achambi.cbfychallenge.presentation.SuccessHandler
import javax.inject.Inject

class ProductsPresenter (screen: ProductsScreen, lifecycle: Lifecycle,
                         executor: BaseExecutor,
                         private val useCase: ProductsUseCase,
                         private val presentationBuilder: PresentationBuilder
)
    : Presenter<ProductsScreen>(screen, lifecycle, executor) {
    private lateinit var products: List<Product>

    fun onViewCreated() {
        perform(object :
            Request<List<Product>> {
            override fun perform(): List<Product> {
                return useCase.fetchProducts()
            }
        }, object :
            SuccessHandler<List<Product>> {
            override fun onSuccess(response: List<Product>) {
                products = response
                screen.showProducts(presentationBuilder.build(response))
            }
        })
    }

    fun productSelected(code: String) {
        //TODO check if this make sense
        screen.navigateToDetails(products.find { it.code == code }!!)
    }
}



class PresentationBuilder@Inject constructor(private val context: Context) {
    fun build(products: List<Product>): List<ProductPresentation> {
        val list = ArrayList<ProductPresentation>()
        products.forEach { list.add(build(it)) }
        return list
    }

    fun build(product: Product): ProductPresentation {
        return ProductPresentation(
            product.code,
            product.name,
            context.getString(R.string.price_format, product.price.toString())
        )
    }
}

class ProductPresentation(val code: String,
                          val name: String,
                          val price: String)

