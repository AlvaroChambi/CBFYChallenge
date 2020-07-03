package es.developers.achambi.cbfychallenge.data

import retrofit2.Call
import retrofit2.http.GET

interface ProductsService {
    @GET("Products.json")
    fun listProducts(): Call<ProductsData>
}