package com.viveret.tinydnn.tinydnn.enums

import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2

enum class ContentType(val stringResId: Int) {
    Weights(R2.string.weights), ///< save/load the Weights
    Model(R2.string.model), ///< save/load the network architecture
    WeightsAndModel(R2.string.weights_and_model)  ///< save/load both the Weights and the architecture
}
