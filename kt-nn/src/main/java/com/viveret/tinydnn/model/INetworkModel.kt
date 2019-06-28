package com.viveret.tinydnn.model

import com.viveret.tinydnn.enums.ContentType
import com.viveret.tinydnn.enums.FileFormat
import com.viveret.tinydnn.network.Layer

interface INetworkModel: IModel<INetworkModel> {
    var name: String

    fun size(): Long

    fun load(filename: String): Boolean  // ContentType what  = WeightsAndModel, FileFormat format = FileFormat.Binary

    fun load(filename: String, what: ContentType): Boolean  // FileFormat format = FileFormat.Binary

    fun load(filename: String, what: ContentType, format: FileFormat): Boolean

    fun save(filename: String)  // ContentType what  = WeightsAndModel, FileFormat format = FileFormat.Binary

    fun save(filename: String, what: ContentType)  // FileFormat format = FileFormat.Binary

    fun save(filename: String, what: ContentType, format: FileFormat)

    /**
     * save the network architecture as Json string
     */
    fun to_json(what: ContentType): String

    /**
     * save the network architecture as Json string
     */
    fun to_json(): String // TestResult.ContentType what = TestResult.ContentType.Model

    /**
     * load the network architecture from Json string
     */
    fun from_json(json_string: String)  // TestResult.ContentType what = TestResult.ContentType.Model

    /**
     * load the network architecture from Json string
     */
    fun from_json(json_string: String, what: ContentType)

    /**
     * return number of layers
     */
    fun layer_size(): Long

    /**
     * return index-th layer as <T>
     * throw nn_error if index-th layer cannot be converted to T
    </T> */
    fun layerAt(index: Long): Layer<*>

    /**
     * return total number of elements of output data
     */
    fun out_data_size(): Long

    /**
     * return total number of elements of input data
     */
    fun in_data_size(): Long
}