package es.developers.achambi.cbfychallenge.data.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE


@Dao
interface ProductsDao {
    @Query("SELECT productentity.code, productentity.name, productentity.price, discountentity.code as discount " +
            "from productentity join discountentity on productentity.code==discountentity.product")
    fun fetchProducts(): List<DetailedProductEntity>
    @Insert
    fun insertAll(list: List<ProductEntity>)
}

@Dao
interface CartDao {
    @Query("SELECT cartproductentity.id, cartproductentity.product, cartproductentity.quantity, productentity.name, productentity.price, discountentity.code" +
            " from cartproductentity " +
            "join productentity on cartproductentity.product==productentity.code " +
            "join discountentity on cartproductentity.product==discountentity.product")
    fun fetchCartItems(): List<DetailedCartEntity>

    //TODO adding new products
    @Insert(onConflict = REPLACE)
    fun insert(product: CartProductEntity)

    @Update
    fun update(product: CartProductEntity)

    @Delete
    fun delete(product: CartProductEntity)
}

@Dao
interface DiscountsDao {
    @Query("SELECT * from discountentity")
    fun fetchDiscounts(): List<DiscountEntity>

    @Insert
    fun insert(discountEntity: DiscountEntity)
}