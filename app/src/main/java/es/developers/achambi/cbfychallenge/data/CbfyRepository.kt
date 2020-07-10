package es.developers.achambi.cbfychallenge.data

import es.developers.achambi.cbfychallenge.data.database.AppDatabase
import es.developers.achambi.cbfychallenge.data.database.DiscountEntity
import java.lang.Exception
import javax.inject.Inject

class CbfyRepository @Inject constructor(private val productsService: ProductsService,
                                         private val database: AppDatabase):
    Repository {

    override fun fetchProducts(): ProductsData {
        val response = productsService.listProducts().execute()
        if(response.isSuccessful) {
            return response.body()!!
        }
        throw Exception()
    }

    override fun fetchDiscounts(): List<DiscountEntity> {
        return database.discountsDao().fetchDiscounts()
    }
}