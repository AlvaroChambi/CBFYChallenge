package es.developers.achambi.cbfychallenge.presentation.product

import android.content.Context
import androidx.lifecycle.Lifecycle
import es.developers.achambi.cbfychallenge.R
import es.developers.achambi.cbfychallenge.domain.Discount
import es.developers.achambi.cbfychallenge.domain.Product
import es.developers.achambi.cbfychallenge.presentation.Executor
import es.developers.achambi.cbfychallenge.presentation.Presenter
import es.developers.achambi.cbfychallenge.presentation.products.PresentationBuilder
import javax.inject.Inject

class ProductDetailPresenter(screen: ProductDetailScreen,
                             lifecycle: Lifecycle,
                             executor: Executor,
                             private val presentationBuilder: DetailsPresentationBuilder)
    : Presenter<ProductDetailScreen>(screen, lifecycle, executor) {

    private lateinit var product: Product

    fun onViewCreated(product: Product) {
        this.product = product
        screen.showProduct(presentationBuilder.build(product))
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