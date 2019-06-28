package com.viveret.tinydnn.enums

import com.viveret.tinydnn.R

enum class ContentType(val stringResId: Int) {
    Weights(R.string.weights), ///< save/load the Weights
    Model(R.string.model), ///< save/load the network architecture
    WeightsAndModel(R.string.weights_and_model)  ///< save/load both the Weights and the architecture
}
