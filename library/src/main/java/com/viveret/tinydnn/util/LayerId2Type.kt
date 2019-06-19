package com.viveret.tinydnn.util

import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2
import com.viveret.tinydnn.tinydnn.layer.*

class LayerId2Type {
    companion object {
        val LAYER_TYPES = mapOf(
                R2.string.layer_asinh to AsinhLayer::class.java,
                R2.string.layer_avg_pool to AveragePoolingLayer::class.java,
                R2.string.layer_avg_unpool to AverageUnpoolingLayer::class.java,
                R2.string.layer_batch_norm to BatchNormLayer::class.java,
                R2.string.layer_conv to ConvolutionalLayer::class.java,
                R2.string.layer_deconv to DeconvolutionalLayer::class.java,
                R2.string.layer_dropout to DropoutLayer::class.java,
                R2.string.layer_fc to FullyConnectedLayer::class.java,
                R2.string.layer_linear to LinearLayer::class.java,
                R2.string.layer_lrn to LrnLayer::class.java,
                R2.string.layer_max_pool to MaxPoolingLayer::class.java,
                R2.string.layer_max_unpool to MaxUnpoolingLayer::class.java,
                R2.string.layer_power to PowerLayer::class.java,
                R2.string.layer_recurrent to RecurrentLayer::class.java,
                R2.string.layer_relu to ReLuLayer::class.java,
                R2.string.layer_sigmoid to SigmoidLayer::class.java,
                R2.string.layer_slice to SliceLayer::class.java,
                R2.string.layer_soft_max to SoftMaxLayer::class.java,
                R2.string.layer_tanh to TanhLayer::class.java)

        val LAYER_IDS = arrayOf(R2.string.layer_avg_pool, R2.string.layer_avg_unpool, R2.string.layer_asinh,
                R2.string.layer_batch_norm, R2.string.layer_conv, R2.string.layer_deconv, R2.string.layer_dropout,
                R2.string.layer_elu, R2.string.layer_fc, R2.string.layer_leaky_relu, R2.string.layer_linear,
                R2.string.layer_lrn, R2.string.layer_max_pool, R2.string.layer_power, R2.string.layer_recurrent,
                R2.string.layer_relu, R2.string.layer_selu, R2.string.layer_sigmoid, R2.string.layer_slice,
                R2.string.layer_soft_max, R2.string.layer_soft_plus, R2.string.layer_soft_sign,
                R2.string.layer_tanh, R2.string.layer_tanh_p1m2)

        val SUPPORTED_LAYERS = LAYER_IDS.intersect(LayerId2Type.LAYER_TYPES.keys)
    }
}