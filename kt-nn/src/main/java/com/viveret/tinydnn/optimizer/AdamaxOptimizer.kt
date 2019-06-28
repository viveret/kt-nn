package com.viveret.tinydnn.optimizer

class AdamaxOptimizer : Optimizer(staticConstructor()) {
    companion object {
        @JvmStatic
        external fun staticConstructor(): Long
    }
}