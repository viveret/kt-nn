package com.viveret.tinydnn.optimizer

class RMSpropOptimizer : Optimizer(staticConstructor()) {
    companion object {
        @JvmStatic
        external fun staticConstructor(): Long
    }
}