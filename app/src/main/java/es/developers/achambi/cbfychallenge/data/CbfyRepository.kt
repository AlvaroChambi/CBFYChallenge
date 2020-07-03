package es.developers.achambi.cbfychallenge.data

import java.lang.Exception
import javax.inject.Inject

class CbfyRepository @Inject constructor(private val productsService: ProductsService):
    Repository {

    override fun fetchProducts(): ProductsData {
        val response = productsService.listProducts().execute()
        if(response.isSuccessful) {
            return response.body()!!
        }
        throw Exception()
    }
}