package es.developers.achambi.cbfychallenge.data

import es.developers.achambi.cbfychallenge.data.database.DiscountEntity

class ProductsData(var products: List<ProductData>)

class ProductData(var code: String = "",
                  var name: String = "",
                  var price: Float = 0f)

interface Repository {
    fun fetchProducts(): ProductsData
    fun fetchDiscounts(): List<DiscountEntity>
}