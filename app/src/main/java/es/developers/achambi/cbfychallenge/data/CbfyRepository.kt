package es.developers.achambi.cbfychallenge.data

import es.developers.achambi.cbfychallenge.data.database.*
import java.lang.Exception
import javax.inject.Inject

class CbfyRepository @Inject constructor(private val productsService: ProductsService,
                                         private val database: AppDatabase):
    Repository {

    //fetch data from database if available.
    //Remote data will be fetch if the database is empty
    override fun fetchProducts(): List<DetailedProductEntity> {
        try {
            val products = database.productsDao().fetchProducts()
            if(products.isEmpty()) {
                //Fetch remote data and map to database values in order to populate
                val response = productsService.listProducts().execute()
                if(response.isSuccessful) {
                    val response = response.body()!!
                    val dbItems = ArrayList<ProductEntity>()
                    response.products.forEach {
                        dbItems.add(ProductEntity(it.code, it.name, it.price) )
                    }
                    database.productsDao().insertAll(dbItems)
                }
            }
            return database.productsDao().fetchProducts()
        } catch (e: Exception) {
            throw Exception()
        }
    }

    override fun fetchDiscounts(): List<DiscountEntity> {
        return database.discountsDao().fetchDiscounts()
    }

    override fun fetchCartProducts(): List<DetailedCartEntity> {
        return database.cartDao().fetchCartItems()
    }
}