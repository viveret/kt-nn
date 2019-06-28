package com.viveret.tinydnn.util

import com.viveret.tinydnn.R
import com.viveret.tinydnn.layer.*

class LayerId2Type {
    companion object {
        val LAYER_TYPES = mapOf(
                R.string.layer_asinh to AsinhLayer::class.java,
                R.string.layer_avg_pool to AveragePoolingLayer::class.java,
                R.string.layer_avg_unpool to AverageUnpoolingLayer::class.java,
                R.string.layer_batch_norm to BatchNormLayer::class.java,
                R.string.layer_conv to ConvolutionalLayer::class.java,
                R.string.layer_deconv to DeconvolutionalLayer::class.java,
                R.string.layer_dropout to DropoutLayer::class.java,
                R.string.layer_fc to FullyConnectedLayer::class.java,
                R.string.layer_linear to LinearLayer::class.java,
                R.string.layer_lrn to LrnLayer::class.java,
                R.string.layer_max_pool to MaxPoolingLayer::class.java,
                R.string.layer_max_unpool to MaxUnpoolingLayer::class.java,
                R.string.layer_power to PowerLayer::class.java,
                R.string.layer_recurrent to RecurrentLayer::class.java,
                R.string.layer_relu to ReLuLayer::class.java,
                R.string.layer_sigmoid to SigmoidLayer::class.java,
                R.string.layer_slice to SliceLayer::class.java,
                R.string.layer_soft_max to SoftMaxLayer::class.java,
                R.string.layer_tanh to TanhLayer::class.java)

        val LAYER_IDS = arrayOf(R.string.layer_avg_pool, R.string.layer_avg_unpool, R.string.layer_asinh,
                R.string.layer_batch_norm, R.string.layer_conv, R.string.layer_deconv, R.string.layer_dropout,
                R.string.layer_elu, R.string.layer_fc, R.string.layer_leaky_relu, R.string.layer_linear,
                R.string.layer_lrn, R.string.layer_max_pool, R.string.layer_power, R.string.layer_recurrent,
                R.string.layer_relu, R.string.layer_selu, R.string.layer_sigmoid, R.string.layer_slice,
                R.string.layer_soft_max, R.string.layer_soft_plus, R.string.layer_soft_sign,
                R.string.layer_tanh, R.string.layer_tanh_p1m2)

        val SUPPORTED_LAYERS = LAYER_IDS.intersect(LayerId2Type.LAYER_TYPES.keys)
    }
}