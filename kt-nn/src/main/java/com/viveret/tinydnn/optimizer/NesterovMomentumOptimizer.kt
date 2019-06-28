package com.viveret.tinydnn.optimizer

class NesterovMomentumOptimizer : Optimizer(staticConstructor()) {
    companion object {
        @JvmStatic
        external fun staticConstructor(): Long
    }
}