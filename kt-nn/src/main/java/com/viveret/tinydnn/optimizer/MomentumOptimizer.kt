package com.viveret.tinydnn.optimizer

class MomentumOptimizer : Optimizer(staticConstructor()) {
    companion object {
        @JvmStatic
        external fun staticConstructor(): Long
    }
}