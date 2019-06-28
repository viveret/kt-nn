package com.viveret.tinydnn.optimizer

class AdaptiveGradientOptimizer : Optimizer(staticConstructor()) {
    companion object {
        @JvmStatic
        external fun staticConstructor(): Long
    }
}
