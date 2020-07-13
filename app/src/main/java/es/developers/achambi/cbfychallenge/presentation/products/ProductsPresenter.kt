package es.developers.achambi.cbfychallenge.presentation.products

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.Lifecycle
import es.developers.achambi.cbfychallenge.R
import es.developers.achambi.cbfychallenge.domain.CartUseCase
import es.developers.achambi.cbfychallenge.domain.Discount
import es.developers.achambi.cbfychallenge.domain.Product
import es.developers.achambi.cbfychallenge.domain.ProductsUseCase
import es.developers.achambi.cbfychallenge.presentation.*
import javax.inject.Inject

class ProductsPresenter (screen: ProductsScreen, lifecycle: Lifecycle,
                         executor: BaseExecutor,
                         private val useCase: ProductsUseCase,
                         private val cartUseCase: CartUseCase,
                         private val presentationBuilder: PresentationBuilder
)
    : Presenter<ProductsScreen>(screen, lifecycle, executor) {

    fun onDataSetup() {
        perform(object :
            Request<List<Product>> {
            override fun perform(): List<Product> {
                return useCase.fetchProducts()
            }
        }, object :
            SuccessHandler<List<Product>> {
            override fun onSuccess(response: List<Product>) {
                screen.showProducts(presentationBuilder.build(response))
            }
        }, object :
            ErrorHandler {
            override fun onError() {
                screen.showError()
            }
        })
    }

    fun onOptionsMenuCreated() {
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

    fun productSelected(code: String) {
        screen.navigateToDetails(code)
    }
}

class PresentationBuilder@Inject constructor(private val context: Context) {
    fun build(products: List<Product>): List<ProductPresentation> {
        val list = ArrayList<ProductPresentation>()
        products.forEach { list.add(build(it)) }
        return list
    }

    fun build(product: Product): ProductPresentation {
        val discountResource = when(product.discount) {
            Discount.TWOFORONE -> R.drawable.discount_2_for_1_image
            Discount.THREEORMORE -> R.drawable.discount_3_more_image
            Discount.NONE -> 0
        }
        return ProductPresentation(
            product.code,
            product.name,
            context.getString(R.string.price_format, product.price.toString()),
            discountResource
        )
    }
}

data class ProductPresentation(val code: String,
                          val name: String,
                          val price: String,
                          val discountImage: Int): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeString(name)
        parcel.writeString(price)
        parcel.writeInt(discountImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductPresentation> {
        override fun createFromParcel(parcel: Parcel): ProductPresentation {
            return ProductPresentation(parcel)
        }

        override fun newArray(size: Int): Array<ProductPresentation?> {
            return arrayOfNulls(size)
        }
    }
}

