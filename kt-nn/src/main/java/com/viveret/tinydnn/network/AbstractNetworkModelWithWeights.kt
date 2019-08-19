package com.viveret.tinydnn.network

import com.viveret.tinydnn.basis.Tensor
import com.viveret.tinydnn.basis.Vect
import com.viveret.tinydnn.data.TestResult
import com.viveret.tinydnn.enums.*
import com.viveret.tinydnn.layer.ConvolutionalLayer
import com.viveret.tinydnn.layer.LayerBase
import com.viveret.tinydnn.model.INetworkModel
import com.viveret.tinydnn.model.INetworkModelWithWeights
import com.viveret.tinydnn.optimizer.Optimizer
import com.viveret.tinydnn.util.Iterator

open class AbstractNetworkModelWithWeights(override val nativeObjectHandle: Long) : INetworkModelWithWeights {
    override fun sameModelAs(other: INetworkModel): Boolean {
        if (this.layer_size() == other.layer_size()) {
            for (i in 0 until this.layer_size()) {
                val layer = this.layerAt(i)
                val otherLayer = other.layerAt(i)
                if (layer is LayerBase && otherLayer is LayerBase) {
                    if (!layer.sameModelAs(otherLayer)) {
                        return false
                    }
                } else {
                    return false
                }
            }
            return true
        } else {
            return false
        }
    }

    override var name: String
        get() = jniGetName(nativeObjectHandle)
        set(name) = jniSetName(nativeObjectHandle, name)

    override fun initWeight() = setup(true)

    override fun backward(first: List<Tensor>) {

    }

    override fun forward(first: List<Tensor>): List<Tensor> = emptyList() // TODO: implement

    override fun update_weights(opt: Optimizer) =
            jniUpdateWeights(nativeObjectHandle, opt.nativeObjectHandle)

    override fun setup(reset_weight: Boolean) = jniSetup(nativeObjectHandle, reset_weight)

    override fun clear_grads() = jniClearGrads(nativeObjectHandle)

    override fun size(): Long = jniGetSize(nativeObjectHandle)

    override fun begin(): Iterator = Iterator() // TODO: implement

    override fun end(): Iterator = Iterator() // TODO: implement

    override fun target_value_min(): Float = jniGetTargetValueMin(nativeObjectHandle)

    override fun target_value_min(out_channel: Int): Float = jniGetTargetValueMinChannel(nativeObjectHandle, out_channel)

    override fun target_value_max(): Float = jniGetTargetValueMax(nativeObjectHandle)

    override fun target_value_max(out_channel: Int): Float = jniGetTargetValueMaxChannel(nativeObjectHandle, out_channel)

    override fun load(vec: List<Float>) {

    }

    override fun label2vec(t: Long, num: Long, vec: List<Vect>) {

    }

    override fun label2vec(labels: List<Long>, vec: List<Vect>) {

    }

    override fun loadWeights(path: String, format: FileFormat) =
            jniLoadWeightsFromPath(nativeObjectHandle, path, format.ordinal)

    override fun predict(`in`: Vect): Vect = Vect.attach(jniPredict(nativeObjectHandle, `in`.nativeObjectHandle))

    override fun predict(`in`: Tensor): Tensor = Tensor() // TODO: implement

    override fun predict(`in`: List<Tensor>): List<Tensor> = emptyList() // TODO: implement

    override fun predict_max_value(`in`: Vect): Float = jniPredictMaxValue(nativeObjectHandle, `in`.nativeObjectHandle)

    override fun predict_label(`in`: Vect): Long = jniPredictLabel(nativeObjectHandle, `in`.nativeObjectHandle)

    override fun predict(`in`: FloatArray): Vect = Vect.attach(0) // TODO: implement

    override fun train(optimizer: Optimizer, inputs: Array<Vect>, class_labels: Array<Long>, batch_size: Long,
                       epoch: Int, on_batch_enumerate: () -> Unit,
                       on_epoch_enumerate: () -> Unit, reset_weights: Boolean,
                       n_threads: Int, t_cost: Array<Vect>): TrainResult {
        val inputsNative = LongArray(inputs.size)
        for (i in inputs.indices) {
            inputsNative[i] = inputs[i].nativeObjectHandle
        }

        val costsNative = LongArray(t_cost.size)
        for (i in t_cost.indices) {
            costsNative[i] = t_cost[i].nativeObjectHandle
        }

        this.on_epoch_enumerate = on_epoch_enumerate
        this.on_batch_enumerate = on_batch_enumerate

        try {
            return TrainResult.values()[jniTrain(this, nativeObjectHandle, optimizer.nativeObjectHandle, inputsNative,
                    batch_size, epoch, reset_weights, n_threads, costsNative)]
        } catch (e: Exception) {
            throw Exception("Could not fit", e)
        }
    }

    private lateinit var on_epoch_enumerate: () -> Unit

    private lateinit var on_batch_enumerate: () -> Unit

    override fun fit(optimizer: Optimizer, inputs: Array<Vect>, desired_outputs: Array<Vect>, batch_size: Long,
                     epoch: Int, on_batch_enumerate: () -> Unit,
                     on_epoch_enumerate: () -> Unit, reset_weights: Boolean,
                     n_threads: Int, t_cost: Array<Vect>): TrainResult {
        val inputsNative = LongArray(inputs.size) { inputs[it].nativeObjectHandle }
        val desired_outputsNative = LongArray(desired_outputs.size) { desired_outputs[it].nativeObjectHandle }
        val costsNative = LongArray(t_cost.size) { t_cost[it].nativeObjectHandle }

        this.on_epoch_enumerate = on_epoch_enumerate
        this.on_batch_enumerate = on_batch_enumerate

        try {
            return TrainResult.values()[jniFit(this, nativeObjectHandle, optimizer.nativeObjectHandle, inputsNative, desired_outputsNative,
                    batch_size, epoch, reset_weights, n_threads, costsNative)]
        } catch (e: Exception) {
            throw Exception("Could not fit", e)
        }
    }

    fun onEpochUpdate() = on_epoch_enumerate()

    override fun epochAt(): Int = jniGetEpochAt(nativeObjectHandle)

    fun onBatchUpdate() = on_batch_enumerate()

    override fun batchAt(): Long = jniGetBatchAt(nativeObjectHandle)

    override fun fit(optimizer: Optimizer, inputs: Array<Vect>, desired_outputs: Array<Vect>, batch_size: Long, epoch: Int): TrainResult {
        val inputsNative = LongArray(inputs.size) { inputs[it].nativeObjectHandle }
        val desired_outputsNative = LongArray(desired_outputs.size) { desired_outputs[it].nativeObjectHandle }

        return TrainResult.values()[jniFit(nativeObjectHandle, optimizer.nativeObjectHandle, inputsNative, desired_outputsNative, batch_size, epoch)]
    }

    override fun train(optimizer: Optimizer, inputs: Array<Vect>, class_labels: Array<Long>, batch_size: Long, epoch: Int): TrainResult = TrainResult.FAILURE

    override fun set_netphase(phase: net_phase) = jniSetNetPhase(nativeObjectHandle, phase.ordinal)

    override fun stop_ongoing_training() = jniStopOngoingTraining(nativeObjectHandle)

    override fun test(`in`: List<Vect>, t: List<Long>): TestResult = TestResult() // TODO: implement

    override fun test(`in`: List<Vect>): List<Vect> = emptyList() // TODO: implement

    override fun get_loss(`in`: List<Vect>, t: List<Long>): Float = 0f // TODO: implement

    override fun gradient_check(`in`: List<Tensor>, t: List<List<Long>>, eps: Float, mode: grad_check_mode): Boolean = false

    override fun layer_size(): Long = size()

    override fun layerAt(index: Long): Layer<*> {
        val layerHandle = jniGetLayerAt(nativeObjectHandle, index)
        val baseLayer = LayerBase(layerHandle)
        return when (baseLayer.layer_type()) {
            "conv" -> ConvolutionalLayer.wrap(layerHandle)
            else -> baseLayer
        }
    }

    override fun out_data_size(): Long = jniGetOutDataSize(nativeObjectHandle)

    override fun in_data_size(): Long = jniGetInDataSize(nativeObjectHandle)

    override fun weight_init(f: Vect): INetworkModelWithWeights {
        jniWeightInit(nativeObjectHandle, f.nativeObjectHandle)
        return this
    }

    override fun bias_init(f: Vect): INetworkModelWithWeights {
        jniBiasInit(nativeObjectHandle, f.nativeObjectHandle)
        return this
    }

    override fun has_same_weights(rhs: INetworkModelWithWeights, eps: Float): Boolean = jniHasSameWeights(nativeObjectHandle, rhs.nativeObjectHandle, eps)

    override fun load(filename: String): Boolean = load(filename, ContentType.WeightsAndModel)

    override fun load(filename: String, what: ContentType): Boolean =
            load(filename, what, if (filename.endsWith(".json")) FileFormat.Json else FileFormat.Binary)

    override fun load(filename: String, what: ContentType, format: FileFormat): Boolean =
            jniLoad(nativeObjectHandle, filename, what.ordinal, format.ordinal)

    override fun save(filename: String) = save(filename, ContentType.WeightsAndModel)

    override fun save(filename: String, what: ContentType) =
            save(filename, ContentType.WeightsAndModel, FileFormat.Binary)

    override fun save(filename: String, what: ContentType, format: FileFormat) =
            jniSave(nativeObjectHandle, filename, what.ordinal, format.ordinal)

    override fun to_json(what: ContentType): String = jniToJson(nativeObjectHandle, what.ordinal)

    override fun to_json(): String = to_json(ContentType.Model)

    override fun from_json(json_string: String) =
            from_json(json_string, ContentType.WeightsAndModel)

    override fun from_json(json_string: String, what: ContentType) =
            jniFromJson(nativeObjectHandle, json_string, what.ordinal)

    companion object {
        @JvmStatic
        external fun jniGetName(handle: Long): String

        @JvmStatic
        external fun jniSetName(handle: Long, name: String)

        @JvmStatic
        external fun jniGetLayerAt(handle: Long, index: Long): Long

        @JvmStatic
        external fun jniUpdateWeights(handle: Long, optimizerHandle: Long)

        @JvmStatic
        external fun jniGetTargetValueMinChannel(handle: Long, out_channel: Int): Float

        @JvmStatic
        external fun jniSetup(handle: Long, reset_weight: Boolean)

        @JvmStatic
        external fun jniClearGrads(handle: Long)

        @JvmStatic
        external fun jniGetSize(handle: Long): Long

        @JvmStatic
        external fun jniGetTargetValueMin(handle: Long): Float

        @JvmStatic
        external fun jniGetTargetValueMax(handle: Long): Float

        @JvmStatic
        external fun jniSetNetPhase(handle: Long, phase: Int)

        @JvmStatic
        external fun jniGetTargetValueMaxChannel(handle: Long, out_channel: Int): Float

        @JvmStatic
        external fun jniSaveModel(handle: Long, oaHandle: Long)

        @JvmStatic
        external fun jniLoadModel(handle: Long, iaHandle: Long)

        @JvmStatic
        external fun jniSaveWeights(handle: Long, oaHandle: Long)

        @JvmStatic
        external fun jniLoadWeights(handle: Long, iaHandle: Long)

        @JvmStatic
        external fun jniLoadWeightsFromPath(handle: Long, path: String, fmt: Int)

        @JvmStatic
        external fun jniStopOngoingTraining(handle: Long)

        @JvmStatic
        external fun jniGetOutDataSize(handle: Long): Long

        @JvmStatic
        external fun jniGetInDataSize(handle: Long): Long

        @JvmStatic
        external fun jniWeightInit(handle: Long, vectHandle: Long)

        @JvmStatic
        external fun jniBiasInit(handle: Long, vectHandle: Long)

        @JvmStatic
        external fun jniSave(handle: Long, filename: String, what: Int, format: Int)

        @JvmStatic
        external fun jniToJson(handle: Long, what: Int): String

        @JvmStatic
        external fun jniFromJson(handle: Long, json_string: String, what: Int)

        @JvmStatic
        external fun jniHasSameWeights(handle: Long, otherHandle: Long, eps: Float): Boolean

        @JvmStatic
        external fun jniToArchive(handle: Long, arHandle: Long, what: Int)

        @JvmStatic
        external fun jniFromArchive(handle: Long, arHandle: Long, what: Int)

        @JvmStatic
        external fun jniLoad(handle: Long, filename: String, what: Int, format: Int): Boolean

        @JvmStatic
        external fun jniPredictLabel(handle: Long, vectHandle: Long): Long

        @JvmStatic
        external fun jniPredictMaxValue(handle: Long, vectHandle: Long): Float

        @JvmStatic
        external fun jniFit(handle: Long, optimizerHandle: Long, inputs: LongArray,
                            desired_outputs: LongArray, batch_size: Long, epochs: Int): Int


        @JvmStatic
        external fun jniFit(thiz: AbstractNetworkModelWithWeights, handle: Long, optimizerHandle: Long, inputs: LongArray,
                            desired_outputs: LongArray, batch_size: Long, epochs: Int, reset_weights: Boolean,
                            n_threads: Int, costsNative: LongArray): Int


        @JvmStatic
        external fun jniTrain(thiz: AbstractNetworkModelWithWeights, handle: Long, optimizerHandle: Long, inputs: LongArray,
                              batch_size: Long, epochs: Int, reset_weights: Boolean,
                              n_threads: Int, costsNative: LongArray): Int

        @JvmStatic
        external fun jniPredict(handle: Long, vectHandle: Long): Long

        @JvmStatic
        external fun jniGetEpochAt(handle: Long): Int

        @JvmStatic
        external fun jniGetBatchAt(handle: Long): Long
    }
}
