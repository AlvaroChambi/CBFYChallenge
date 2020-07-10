package es.developers.achambi.cbfychallenge.domain

import es.developers.achambi.cbfychallenge.data.Repository
import java.math.BigDecimal
import javax.inject.Inject

class ProductsUseCase @Inject constructor(private val repository: Repository,
                                          private val discountsUseCase: DiscountsUseCase) {
    fun fetchProducts(): List<Product> {
        val products = ArrayList<Product>()
        //fetch discount to link with our product
        val discounts = discountsUseCase.getDiscounts()
        repository.fetchProducts().products.forEach { product ->
           val discount = discounts.find { it.first == product.code }
           if(discount != null) {
               products.add(Product(product.code, product.name, BigDecimal(product.price.toString()),
                       discount.second) )
           } else {
               products.add(Product(product.code, product.name, BigDecimal(product.price.toString()),
                   Discount.NONE) )
           }
        }

        return products
    }
}