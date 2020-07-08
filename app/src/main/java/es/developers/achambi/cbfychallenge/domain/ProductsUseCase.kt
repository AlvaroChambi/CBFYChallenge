package es.developers.achambi.cbfychallenge.domain

import es.developers.achambi.cbfychallenge.data.Repository
import java.math.BigDecimal
import javax.inject.Inject

class ProductsUseCase @Inject constructor(private val repository: Repository) {
    fun fetchProducts(): List<Product> {
        val products = ArrayList<Product>()
        //fetch discount to link with our product
        repository.fetchProducts().products.forEach {
            if(it.code == "VOUCHER") {
                products.add( Product(it.code, it.name, BigDecimal(it.price.toString()),
                    Discount.TWO_FOR_ONE) )
            } else if(it.code == "TSHIRT") {
                products.add( Product(it.code, it.name, BigDecimal(it.price.toString()),
                    Discount.THREE_MORE))
            } else {
                products.add( Product(it.code, it.name, BigDecimal(it.price.toString())))
            }

        }

        return products
    }
}