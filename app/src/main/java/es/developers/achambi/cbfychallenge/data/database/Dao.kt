package es.developers.achambi.cbfychallenge.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.ABORT
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update

//TODO: fetch everything with a join query?

@Dao
interface ProductsDao {
    @Query("SELECT * from productentity")
    fun fetchProducts(): List<ProductEntity>
}

@Dao
interface CartDao {
    //TODO triple join? should
    @Query("SELECT * from cartproductentity")
    fun fetchCartItems(): List<CartProductEntity>

    //TODO adding new products
    @Insert(onConflict = REPLACE)
    fun insert(product: ProductEntity)

    @Update
    fun update(product: ProductEntity)
}

@Dao
interface DiscountsDao {
    @Query("SELECT * from discountentity")
    fun fetchDiscounts(): List<DiscountEntity>

    @Insert
    fun insert(discountEntity: DiscountEntity)
}