package es.developers.achambi.cbfychallenge.domain

import es.developers.achambi.cbfychallenge.data.Repository
import javax.inject.Inject

class DiscountsUseCase@Inject constructor(private val repository: Repository) {
    fun getDiscounts(): List<Pair<String, Discount>> {
        val discounts = repository.fetchDiscounts()
        val result = ArrayList<Pair<String, Discount>>()
        discounts.forEach {
            result.add(Pair(it.productCode, Discount.valueOf(it.code)))
        }
        return result
    }
}