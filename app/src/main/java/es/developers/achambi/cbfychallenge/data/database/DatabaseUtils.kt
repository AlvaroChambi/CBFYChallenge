package es.developers.achambi.cbfychallenge.data.database

import android.content.Context
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


object DatabaseUtils {
    @Throws(IOException::class)
    fun copyDataBase(context: Context, dbName: String?) {
        val myInput = context.assets.open(dbName!!)
        val file = context.getDatabasePath(dbName)
        if (file.exists()) {
            return
        }
        if (!file.parentFile?.exists()!!) {
            file.parentFile?.mkdirs()
        }
        val myOutput: OutputStream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var length: Int
        while (myInput.read(buffer).also { length = it } > 0) {
            myOutput.write(buffer, 0, length)
        }
        myOutput.flush()
        myOutput.close()
        myInput.close()
    }
}
