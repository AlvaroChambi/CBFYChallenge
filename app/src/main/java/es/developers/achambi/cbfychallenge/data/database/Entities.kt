package es.developers.achambi.cbfychallenge.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductEntity(@PrimaryKey val code: String,
                         @ColumnInfo(name = "name") val name: String,
                         @ColumnInfo(name = "price") val price: Float)

@Entity
data class CartProductEntity(@PrimaryKey(autoGenerate = true) val id: Long,
                             @ColumnInfo(name = "product") val productCode: String,
                             @ColumnInfo(name = "quantity") val quantity: Int)

@Entity
data class DiscountEntity(@PrimaryKey val code: String,
                          @ColumnInfo(name="product") val productCode: String)