package es.developers.achambi.cbfychallenge.domain

import java.math.BigDecimal
import javax.inject.Inject

class CartUseCase@Inject constructor() {
    fun fetchCartItems(): List<CartProduct> {
        val items = ArrayList<CartProduct>()
        items.add( CartProduct(
            Product("VOUCHER", "cabify voucher", BigDecimal("12"), Discount.TWO_FOR_ONE),
            4 ))
        items.add( CartProduct(
            Product("TSHIRT", "cabify t-shirt", BigDecimal("12"), Discount.THREE_MORE),
            2 ))
        return items
    }
}