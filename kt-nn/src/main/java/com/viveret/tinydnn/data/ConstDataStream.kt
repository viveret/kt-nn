package com.viveret.tinydnn.data

import android.content.Context
import com.viveret.tinydnn.basis.*
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class ConstDataStream(val context: Context, val dataProvider: InputStreamProvider, override val name: String, override val mime: String, override val extension: String, override val namespace: String, override val role: DataRole) : Stream {
    override fun getInt(attr: DataAttr): Int? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getString(attr: DataAttr): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBoolean(attr: DataAttr): Boolean? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun size(source: DataSource): Long {
        throw NotImplementedError()
    }

    override val host: Host?
        get() = throw NotImplementedError()

    override fun sourcePath(source: DataSource): String = throw NotImplementedError()

    override fun destinationStream(source: DataSource): OutputStream {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sourceStream(source: DataSource): BetterInputStream =
            dataProvider.open(context)

    override fun isAvailable(source: DataSource): Boolean = true

    override fun delete(source: DataSource) = throw Exception("Not supported")

    override val id: UUID = UUID.randomUUID()
}