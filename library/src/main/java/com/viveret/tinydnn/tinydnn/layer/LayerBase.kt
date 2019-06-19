package com.viveret.tinydnn.tinydnn.layer

import android.graphics.Canvas
import android.graphics.Paint
import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2
import com.viveret.tinydnn.tinydnn.*
import com.viveret.tinydnn.tinydnn.model.IModel
import com.viveret.tinydnn.tinydnn.util.LayerImage
import com.viveret.tinydnn.tinydnn.util.LayerVizualization
import java.io.InputStream
import java.io.OutputStream
import java.util.*

open class LayerBase(nativeObjectHandle: Long) : Layer<Any>, IModel<Layer<Any>> {
    override var nativeObjectHandle: Long = 0
        protected set

    override var color: Int = 0

    override val prevNodes: List<Node>
        get() = emptyList() // TODO: implement

    override val nextNodes: List<Node>
        get() = emptyList() // TODO: implement

    override val next: List<Edge>
        get() = emptyList() // TODO: implement

    override val prev: List<Edge>
        get() = emptyList() // TODO: implement

    init {
        this.nativeObjectHandle = nativeObjectHandle
    }

    override fun getVisualizations(): List<LayerVizualization> {
        val ret = ArrayList<LayerVizualization>()
        val imgHandle = jniOutputToImage(nativeObjectHandle)
        if (imgHandle != 0L) {
            val image = LayerImage.attach(imgHandle)
            ret.add(LayerVizualization(R2.string.outputs_visualization, image))
        }
        return ret
    }

    override fun set_parallelize(parallelize: Boolean) {
        jniSetParallelize(nativeObjectHandle, parallelize)
    }

    override fun parallelize(): Boolean = jniGetParallelize(nativeObjectHandle)

    override fun in_channels(): Long = jniGetInChannels(nativeObjectHandle)

    override fun out_channels(): Long = jniGetOutChannels(nativeObjectHandle)

    override fun in_data_size(): Long = jniGetInDataSize(nativeObjectHandle)

    override fun out_data_size(): Long = jniGetOutDataSize(nativeObjectHandle)

    override fun in_data_shape(): List<Index3D> = jniGetInDataShape(nativeObjectHandle).map { x -> Index3D.attach(x) }

    override fun out_data_shape(): List<Index3D> = jniGetOutDataShape(nativeObjectHandle).map { x -> Index3D.attach(x) }

    override fun weights(): List<Vect> = jniGetWeights(nativeObjectHandle).map { x -> Vect.attach(x) }

    override fun weights_grads(): List<Tensor> = throw NotImplementedError()//jniGetWeightGrads(nativeObjectHandle).map { x -> Tensor.attach(x) }

    override fun inputs(): List<Any> = throw NotImplementedError()// jniGetInputs(nativeObjectHandle)

    override fun outputs(): List<Any> = throw NotImplementedError()//jniGetOutputs(nativeObjectHandle)

    override fun in_types(): List<vector_type> = jniGetInTypes(nativeObjectHandle).map { x -> vector_type.values().find { y -> y.`val` == x }!! }

    override fun out_types(): List<vector_type> = jniGetOutTypes(nativeObjectHandle).map { x -> vector_type.values().find { y -> y.`val` == x }!! }

    override fun set_trainable(trainable: Boolean) {
        jniSetTrainable(nativeObjectHandle, trainable)
    }

    override fun trainable(): Boolean = jniGetTrainable(nativeObjectHandle)

    override fun in_shape(): List<Index3D> = jniGetInShape(nativeObjectHandle).map { x -> Index3D.attach(x) }

    override fun out_shape(): List<Index3D> = jniGetOutShape(nativeObjectHandle).map { x -> Index3D.attach(x) }

    override fun layer_type(): String = jniGetLayerType(nativeObjectHandle)

    override fun fan_in_size(): Long = jniGetFanInSize(nativeObjectHandle)

    override fun fan_out_size(): Long = jniGetFanOutSize(nativeObjectHandle)

    override fun weight_init(f: Vect): Layer<*> = this

    override fun bias_init(f: Vect): Layer<*> = this

    override fun save(os: OutputStream, precision: Int) {

    }

    override fun load(`is`: InputStream, precision: Int) {

    }

    override fun setup(reset_weight: Boolean) = jniSetup(nativeObjectHandle, reset_weight)

    override fun init_weight() = jniInitWeight(nativeObjectHandle)

    override fun clear_grads() = jniClearGrads(nativeObjectHandle)

    override fun update_weight(o: Optimizer) =
            jniUpdateWeight(nativeObjectHandle, o.nativeObjectHandle)

    override fun set_sample_count(sample_count: Long) =
            jniSetSampleCount(nativeObjectHandle, sample_count)

    override fun draw(canvas: Canvas, position: Int) {
        var nposition = position
        nposition *= 1000

        val p = Paint()
        p.textSize = 60f
        p.color = color

        val lines = toString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in lines.indices) {
            canvas.drawText(lines[i], nposition.toFloat(), p.fontSpacing * i, p)
        }
    }

    override fun back_propagation(in_data: List<Tensor>, out_data: List<Tensor>, out_grad: List<Tensor>, in_grad: List<Tensor>) {

    }

    override fun forward_propagation(in_data: List<Tensor>, out_data: List<Tensor>) {

    }

    override fun load(src: List<Float>, idx: Int) {

    }

    override fun output(out: List<Tensor>) {

    }

    override fun set_in_data(data: List<Vect>, cnt: Long) {

    }

    override fun set_out_grads(grad: List<Vect>, cnt: Long) {

    }

    override fun sameModelAs(other: Layer<Any>): Boolean = this.toString() == other.toString()

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(layer_type() + ": parallel=" + parallelize() + ", trainable=" + trainable() + "\n")
        sb.append("in_channels: " + in_channels() + "\n")
        sb.append("out_channels: " + out_channels() + "\n")
        sb.append("in_data_size: " + in_data_size() + "\n")
        sb.append("out_data_size: " + out_data_size() + "\n")
        sb.append("fan_in_size: " + fan_in_size() + "\n")
        sb.append("fan_out_size: " + fan_out_size() + "\n")
        return sb.toString()
    }

    companion object {
        @JvmStatic
        external fun jniGetInTypes(handle: Long): IntArray

        @JvmStatic
        external fun jniGetOutTypes(handle: Long): IntArray

        @JvmStatic
        external fun jniSetParallelize(handle: Long, `val`: Boolean)

        @JvmStatic
        external fun jniGetParallelize(handle: Long): Boolean

        @JvmStatic
        external fun jniGetInChannels(handle: Long): Long

        @JvmStatic
        external fun jniGetOutChannels(handle: Long): Long

        @JvmStatic
        external fun jniGetInDataSize(handle: Long): Long

        @JvmStatic
        external fun jniGetOutDataSize(handle: Long): Long

        @JvmStatic
        external fun jniGetInDataShape(handle: Long): LongArray

        @JvmStatic
        external fun jniGetOutDataShape(handle: Long): LongArray

        @JvmStatic
        external fun jniGetWeights(handle: Long): LongArray

        @JvmStatic
        external fun jniGetWeightGrads(handle: Long): LongArray

        @JvmStatic
        external fun jniGetInShape(handle: Long): LongArray

        @JvmStatic
        external fun jniGetOutShape(handle: Long): LongArray

        @JvmStatic
        external fun jniSetTrainable(handle: Long, `val`: Boolean)

        @JvmStatic
        external fun jniGetTrainable(handle: Long): Boolean

        @JvmStatic
        external fun jniGetLayerType(handle: Long): String

        @JvmStatic
        external fun jniGetFanInSize(handle: Long): Long

        @JvmStatic
        external fun jniGetFanOutSize(handle: Long): Long

        @JvmStatic
        external fun jniSetup(handle: Long, weight_reset: Boolean)

        @JvmStatic
        external fun jniInitWeight(handle: Long)

        @JvmStatic
        external fun jniClearGrads(handle: Long)

        @JvmStatic
        external fun jniUpdateWeight(handle: Long, optimizerHandle: Long)

        @JvmStatic
        external fun jniSetSampleCount(handle: Long, sampleCount: Long)

        @JvmStatic
        external fun jniOutputToImage(handle: Long): Long
    }
}