package es.developers.achambi.cbfychallenge.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class, CartProductEntity::class, DiscountEntity::class],
    version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productsDao(): ProductsDao
    abstract fun cartDao(): CartDao
    abstract fun discountsDao(): DiscountsDao
}