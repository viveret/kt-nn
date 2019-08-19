package com.viveret.tinydnn.data.io

import com.viveret.tinydnn.basis.*
import java.io.File

class InputSelection() : HashMap<DataRole, BetterInputStream>(), AttributeResolver {
    override fun getInt(attr: DataAttr): Int? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getString(attr: DataAttr): String? {
        for (inputStream in this.values) {
            val attrVal = inputStream.getString(attr)
            if (attrVal != null) {
                return attrVal
            }
        }
        throw java.lang.Exception()
    }

    override fun getBoolean(attr: DataAttr): Boolean? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    constructor(files: Map<Stream, File>): this() {
        for (f in files) {
            this[f.key.role] = FileInputStream(f.value, f.key)
        }
    }

    constructor(files: Map<Stream, File>, dataSource: DataSource): this() {
        for (f in files) {
            val target = File(f.key.sourcePath(dataSource)).canonicalPath
            if (target == f.value.canonicalPath) {
                this[f.key.role] = FileInputStream(f.value, f.key)
            } else {
                throw Exception("Invalid stream ${f.key} destination to ${f.value.canonicalPath}")
            }
        }
    }

    companion object {
        fun FileItem(fileStream: Stream, source: DataSource): BetterInputStream =
            FileInputStream(File(fileStream.sourcePath(source)), fileStream)
    }
}