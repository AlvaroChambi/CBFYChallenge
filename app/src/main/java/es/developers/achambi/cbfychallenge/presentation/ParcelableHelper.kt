package es.developers.achambi.cbfychallenge.presentation

import android.os.Parcel

class ParcelableHelper {
    companion object {
        fun writeEnumToParcel(inP: Parcel, e: Enum<*>) {
            inP.writeInt(e.ordinal)
        }

        fun <E> readEnumFromParcel(out: Parcel, clazz: Class<E>): E {
            val enumConstants = clazz.enumConstants
            return enumConstants!![out.readInt()]
        }
    }
}