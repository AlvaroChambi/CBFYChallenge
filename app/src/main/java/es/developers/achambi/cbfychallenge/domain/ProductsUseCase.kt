package es.developers.achambi.cbfychallenge.domain

import es.developers.achambi.cbfychallenge.data.Repository
import javax.inject.Inject

class ProductsUseCase @Inject constructor(private val repository: Repository) {
    fun fetchProducts(): List<Product> {
        val products = ArrayList<Product>()
        repository.fetchProducts().products.forEach {
            products.add( Product(it.code, it.name, it.price.toString()) )
        }

        return products
    }
}