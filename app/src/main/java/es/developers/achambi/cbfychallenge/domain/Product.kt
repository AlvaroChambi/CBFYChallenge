package es.developers.achambi.cbfychallenge.domain

import android.os.Parcel
import android.os.Parcelable
import es.developers.achambi.cbfychallenge.presentation.ParcelableHelper
import java.math.BigDecimal

enum class Discount {
    TWOFORONE,
    THREEORMORE,
    NONE
}

data class Product( val code: String,
                    val name: String,
                    val price: BigDecimal,
                    var discount: Discount = Discount.NONE)

data class CartProduct( val id: Long,
                        val product: Product,
                        val quantity: Int,
                        val totalPrice: BigDecimal)

data class CartProducts(val cartProducts: List<CartProduct>,
                        val baseTotal: BigDecimal,
                        val total: BigDecimal,
                        val canApplyTwoForOne: Boolean,
                        val canApplyThreeOrMore: Boolean,
                        val twoForOnePrice: BigDecimal,
                        val threeOrMorePrice: BigDecimal,
                        val totalQuantity: Int)