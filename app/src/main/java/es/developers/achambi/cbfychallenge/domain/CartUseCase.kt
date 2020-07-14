package es.developers.achambi.cbfychallenge.domain

import es.developers.achambi.cbfychallenge.data.Repository
import es.developers.achambi.cbfychallenge.data.database.CartProductEntity
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartUseCase@Inject constructor(private val repository: Repository) {
    private var cartProducts: CartProducts? = null
    fun getCartItems(): CartProducts {
        if(cartProducts == null) {
            return fetchCartItems()
        } else {
            return cartProducts!!
        }
    }
    //TODO unit test
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
        var totalQuantity = 0
        items.forEach {
            val price = BigDecimal(it.price)
            val discount = Discount.valueOf(it.code)
            val baseProduct = Product(it.product, it.name, price,
                discount)
            val detailedPrice = price.multiply(BigDecimal(it.quantity))
            var currentTwoPrice = BigDecimal(0)
            var currentThreePrice = BigDecimal(0)
            totalQuantity += it.quantity
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
            result.add(CartProduct(it.id, baseProduct, it.quantity, detailedPrice))
        }
        totalDiscount = totalDiscount.add(twoForOnePrice).add(threeOrMorePrice)
        cartProducts = CartProducts(result, baseTotal, baseTotal.subtract(totalDiscount), canApplyTwoForOne,
        canApplyThreeOrMore, twoForOnePrice, threeOrMorePrice, totalQuantity)
        return cartProducts!!
    }

    fun getCartItemsCount(): Int {
        val products = getCartItems()
        return products.totalQuantity
    }

    @Throws
    fun findProduct(id: Long): CartProduct? {
        if(cartProducts == null) {
            fetchCartItems()
        }
        return cartProducts!!.cartProducts.find { id == it.id }
    }
    @Throws
    fun findProduct(productCode: String): CartProduct? {
        if(cartProducts == null) {
            fetchCartItems()
        }
        return cartProducts!!.cartProducts.find { productCode == it.product.code }
    }

    fun addtoCart(productCode: String ,quantity: Int) {
        val product = findProduct(productCode)
        if(product == null) {
            val productEntity = CartProductEntity(0, productCode, quantity)
            repository.addToCart(productEntity)
        } else {
            updateItem(product, product.quantity + quantity)
        }

    }

    fun increaseProductCount(id: Long): CartProducts {
        val product = findProduct(id)!!
        return updateItem( product, product.quantity+1 )
    }

    fun decreaseProductCount(id: Long): CartProducts {
        val product = findProduct(id)!!
        return updateItem( product, product.quantity-1 )
    }

    fun updateItem(product: CartProduct, quantity: Int): CartProducts {
        repository.updateCartItem(CartProductEntity(product.id, product.product.code, quantity))
        return fetchCartItems()
    }

    fun remove(id: Long): CartProducts {
        val product = findProduct(id)!!
        repository.deleteCartItem(CartProductEntity(product.id, product.product.code, product.quantity))
        return fetchCartItems()
    }
}