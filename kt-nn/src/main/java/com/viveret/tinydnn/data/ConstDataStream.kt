package com.viveret.tinydnn.data

import android.content.Context
import com.viveret.tinydnn.basis.*
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class ConstDataStream(val dataProvider: InputStreamProvider, override val name: String, override val mime: String, override val extension: String, override val namespace: String, override val role: DataRole) : Stream {
    override fun size(source: DataSource, context: Context): Long {throw NotImplementedError()
    }

    override val host: Host?
        get() = throw NotImplementedError()

    override fun sourcePath(source: DataSource, context: Context): String = throw NotImplementedError()

    override fun destinationStream(source: DataSource, context: Context): OutputStream {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sourceStream(source: DataSource, context: Context): InputStream =
            dataProvider.open(context)

    override fun isAvailable(source: DataSource, context: Context): Boolean = true

    override fun delete(source: DataSource, context: Context) = throw Exception("Not supported")

    override val id: UUID = UUID.randomUUID()
}