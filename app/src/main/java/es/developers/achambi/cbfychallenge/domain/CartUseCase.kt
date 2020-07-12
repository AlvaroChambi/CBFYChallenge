package es.developers.achambi.cbfychallenge.domain

import es.developers.achambi.cbfychallenge.data.Repository
import java.math.BigDecimal
import javax.inject.Inject

class CartUseCase@Inject constructor(private val repository: Repository) {
    //TODO cleanup and test
    //TODO discounts should be modeled as list, this solution won't scale well
    fun fetchCartItems(): CartProducts {
        val items =  repository.fetchCartProducts()
        val result = ArrayList<CartProduct>()
        var baseTotal = BigDecimal(0)
        var totalDiscount = BigDecimal(0)
        var canApplyTwoForOne = false
        var canApplyThreeOrMore = false
        var twoForOnePrice = BigDecimal(0)
        var threeOrMorePrice = BigDecimal(0)
        items.forEach {
            val price = BigDecimal(it.price)
            val discount = Discount.valueOf(it.code)
            val baseProduct = Product(it.product, it.name, price,
                discount)
            val detailedPrice = price.multiply(BigDecimal(it.quantity))
            var currentTwoPrice = BigDecimal(0)
            var currentThreePrice = BigDecimal(0)
            when(discount) {
                Discount.TWOFORONE -> {
                    //rounded down towards zero
                    val multiplier: Int = (it.quantity/2)
                    canApplyTwoForOne = true
                    if(multiplier >= 1) {
                        currentTwoPrice = BigDecimal(multiplier).multiply(price)
                    }
                }
                Discount.THREEORMORE -> {
                    canApplyThreeOrMore = true
                    if(it.quantity >= 3) {
                        currentThreePrice = BigDecimal("1").multiply(BigDecimal(it.quantity))
                    }
                }
                Discount.NONE -> BigDecimal(0)
            }
            baseTotal = baseTotal.add(detailedPrice)
            twoForOnePrice = twoForOnePrice.add(currentTwoPrice)
            threeOrMorePrice = threeOrMorePrice.add(currentThreePrice)
            result.add(CartProduct(baseProduct, it.quantity, detailedPrice))
        }
        totalDiscount = totalDiscount.add(twoForOnePrice).add(threeOrMorePrice)
        return CartProducts(result, baseTotal, baseTotal.subtract(totalDiscount), canApplyTwoForOne,
        canApplyThreeOrMore, twoForOnePrice, threeOrMorePrice)
    }
}