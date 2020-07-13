package es.developers.achambi.cbfychallenge.domain

import es.developers.achambi.cbfychallenge.data.Repository
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsUseCase @Inject constructor(private val repository: Repository) {
    private val products= ArrayList<Product>()

    //Fetch products list and map to domain model
    fun fetchProducts(): List<Product> {
        if(products.isEmpty()) {
            repository.fetchProducts().forEach { product ->
                products.add( Product( product.code, product.name, BigDecimal(product.price.toString()),
                    Discount.valueOf(product.discount)) )
            }
        }

        return products
    }

    fun fetchProduct(code: String): Product {
        if(products.isEmpty()) {
            fetchProducts()
        }
        return products.find { code == it.code }!!
    }
}