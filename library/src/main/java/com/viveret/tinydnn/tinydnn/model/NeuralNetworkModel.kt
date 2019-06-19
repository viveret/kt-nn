package com.viveret.tinydnn.tinydnn.model

import com.viveret.tinydnn.tinydnn.Layer
import com.viveret.tinydnn.tinydnn.enums.ContentType
import com.viveret.tinydnn.tinydnn.enums.FileFormat
import com.viveret.tinydnn.tinydnn.layer.LayerBase

class NeuralNetworkModel: INetworkModel {
    override fun sameModelAs(other: INetworkModel): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val layerModels = ArrayList<LayerBase>()

    override var name: String = ""

    override fun size(): Long = this.layer_size()

    override fun load(filename: String): Boolean =
            this.load(filename, ContentType.Model, FileFormat.PortableBinary)

    override fun load(filename: String, what: ContentType): Boolean =
            this.load(filename,  what, FileFormat.PortableBinary)

    override fun load(filename: String, what: ContentType, format: FileFormat): Boolean {
        if (what == ContentType.Model) {
            return false// todo: implement
        } else {
            throw IllegalArgumentException("Content type $what not supported for NeuralNetworkModel")
        }
    }

    override fun save(filename: String) =
            this.save(filename, ContentType.Model, FileFormat.PortableBinary)

    override fun save(filename: String, what: ContentType) =
            this.save(filename, what, FileFormat.PortableBinary)

    override fun save(filename: String, what: ContentType, format: FileFormat) {
        if (what == ContentType.Model) {
            return// todo: implement
        } else {
            throw IllegalArgumentException("Content type $what not supported for NeuralNetworkModel")
        }
    }

    override fun to_json(what: ContentType): String {
        if (what == ContentType.Model) {
            return "" // todo: implement
        } else {
            throw IllegalArgumentException("Content type $what not supported for NeuralNetworkModel")
        }
    }

    override fun to_json(): String = this.to_json(ContentType.Model)

    override fun from_json(json_string: String) = this.from_json(json_string, ContentType.Model)

    override fun from_json(json_string: String, what: ContentType) {
        if (what == ContentType.Model) {
            return// todo: implement
        } else {
            throw IllegalArgumentException("Content type $what not supported for NeuralNetworkModel")
        }
    }

    override fun layer_size(): Long = layerModels.size.toLong()

    override fun layerAt(index: Long): Layer<*> = layerModels[index.toInt()]

    override fun out_data_size(): Long = layerModels.lastOrNull()?.out_data_size() ?: 0

    override fun in_data_size(): Long = layerModels.firstOrNull()?.in_data_size() ?: 0
}