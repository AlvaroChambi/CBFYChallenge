package es.developers.achambi.cbfychallenge.data

class ProductsData(var products: List<ProductData>)

class ProductData(var code: String = "",
                  var name: String = "",
                  var price: Float = 0f)

interface Repository {
    fun fetchProducts(): ProductsData
}