package es.developers.achambi.cbfychallenge.presentation.product

import android.content.Context
import androidx.lifecycle.Lifecycle
import es.developers.achambi.cbfychallenge.R
import es.developers.achambi.cbfychallenge.domain.CartUseCase
import es.developers.achambi.cbfychallenge.domain.Discount
import es.developers.achambi.cbfychallenge.domain.Product
import es.developers.achambi.cbfychallenge.domain.ProductsUseCase
import es.developers.achambi.cbfychallenge.presentation.*
import es.developers.achambi.cbfychallenge.presentation.products.PresentationBuilder
import javax.inject.Inject

class ProductDetailPresenter(screen: ProductDetailScreen,
                             lifecycle: Lifecycle,
                             executor: Executor,
                             private val presentationBuilder: DetailsPresentationBuilder,
                             private val productsUseCase: ProductsUseCase,
                             private val cartUseCase: CartUseCase)
    : Presenter<ProductDetailScreen>(screen, lifecycle, executor) {

    fun onViewCreated(product: String) {
        perform(object : Request<Product> {
            override fun perform(): Product {
                return productsUseCase.fetchProduct(product)
            }
        }, object : SuccessHandler<Product> {
            override fun onSuccess(response: Product) {
                screen.showProduct(presentationBuilder.build(response))
            }
        }, object : ErrorHandler {
            override fun onError() {
                screen.showError()
            }
        })
    }

    fun onAddToCart(productCode: String, quantity: Int) {
        perform( object : Request<Any> {
            override fun perform(): Any {
                return cartUseCase.addtoCart(productCode, quantity)
            }
        }, object : SuccessHandler<Any> {
            override fun onSuccess(response: Any) {
                screen.showAddToCartSuccess()
                getCartItemsNumber()
            }
        }, object : ErrorHandler {
            override fun onError() {
                screen.showAddToCartError()
            }
        })
    }

    private fun getCartItemsNumber() {
        perform( object :Request<Int> {
            override fun perform(): Int {
                return cartUseCase.getCartItemsCount()
            }
        }, object : SuccessHandler<Int> {
            override fun onSuccess(response: Int) {
                screen.showCartItemQuantity(response)
            }
        })
    }

    fun onOptionsMenuCreated() {
        getCartItemsNumber()
    }
}

class DetailsPresentationBuilder @Inject constructor(private val productBuilder: PresentationBuilder,
                                                     private val context: Context) {
    fun build(product: Product): ProductDetailPresentation {
        var icon = 0
        var description = ""
        when(product.discount) {
            Discount.NONE -> {}
            Discount.TWOFORONE -> {
                icon = R.drawable.discount_2_for_1_image
                description = context.getString(R.string.discount_two_for_one_text)
            }
            Discount.THREEORMORE ->  {
                icon = R.drawable.discount_3_more_image
                description = context.getString(R.string.discount_three_or_more_text)
            }
        }
        val basePresentation = productBuilder.build(product)

        return ProductDetailPresentation( basePresentation.name, basePresentation.price,
            icon, description)
    }
}