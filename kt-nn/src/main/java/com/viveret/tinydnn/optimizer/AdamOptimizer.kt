package com.viveret.tinydnn.optimizer

class AdamOptimizer : Optimizer(staticConstructor()) {
    companion object {
        @JvmStatic
        external fun staticConstructor(): Long
    }
}