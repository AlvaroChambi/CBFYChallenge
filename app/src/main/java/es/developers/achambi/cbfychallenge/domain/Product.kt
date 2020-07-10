package es.developers.achambi.cbfychallenge.domain

import android.os.Parcel
import android.os.Parcelable
import es.developers.achambi.cbfychallenge.presentation.ParcelableHelper
import java.math.BigDecimal

enum class Discount {
    TWO_FOR_ONE,
    THREE_MORE,
    EMPTY
}

data class Product( val code: String,
                    val name: String,
                    val price: BigDecimal,
                    var discount: Discount = Discount.EMPTY): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        BigDecimal(parcel.readString()),
        ParcelableHelper.readEnumFromParcel(parcel, Discount::class.java)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeString(name)
        parcel.writeString(price.toString())
        ParcelableHelper.writeEnumToParcel(parcel, discount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }

}

data class CartProduct( val product: Product,
                        val quantity: Int)