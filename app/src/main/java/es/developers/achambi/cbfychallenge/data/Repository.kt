package es.developers.achambi.cbfychallenge.data

import es.developers.achambi.cbfychallenge.data.database.*

class ProductsData(var products: List<ProductData>)

class ProductData(var code: String = "",
                  var name: String = "",
                  var price: Float = 0f)

interface Repository {
    fun fetchProducts(): List<DetailedProductEntity>
    fun fetchDiscounts(): List<DiscountEntity>
    fun fetchCartProducts(): List<DetailedCartEntity>
}