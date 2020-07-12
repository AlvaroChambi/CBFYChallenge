package es.developers.achambi.cbfychallenge.domain

import es.developers.achambi.cbfychallenge.data.Repository
import java.math.BigDecimal
import javax.inject.Inject

class ProductsUseCase @Inject constructor(private val repository: Repository) {
    //Fetch products list and map to domain model
    fun fetchProducts(): List<Product> {
        val products = ArrayList<Product>()
        repository.fetchProducts().forEach { product ->
            products.add( Product( product.code, product.name, BigDecimal(product.price.toString()),
            Discount.valueOf(product.discount)) )
        }

        return products
    }
}