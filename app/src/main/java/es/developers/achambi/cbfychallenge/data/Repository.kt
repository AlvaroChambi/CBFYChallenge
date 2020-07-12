package es.developers.achambi.cbfychallenge.data

import es.developers.achambi.cbfychallenge.data.database.*

class ProductsData(var products: List<ProductData>)

class ProductData(var code: String = "",
                  var name: String = "",
                  var price: Float = 0f)

interface Repository {
    fun fetchProducts(): List<DetailedProductEntity>
    fun fetchCartProducts(): List<DetailedCartEntity>
    fun addToCart(item: CartProductEntity)
    fun updateCartItem(item: CartProductEntity)
    fun deleteCartItem(item: CartProductEntity)
}