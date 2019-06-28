package com.viveret.tinydnn.optimizer

class GradientDescentOptimizer : Optimizer(staticConstructor()) {
    companion object {
        @JvmStatic
        external fun staticConstructor(): Long
    }
}