package com.viveret.tinydnn.util

import com.viveret.tinydnn.R
import com.viveret.tinydnn.R2
import com.viveret.tinydnn.tinydnn.optimizer.*

class OptimizerId2Type {
    companion object {
        val OPTIMIZER_TYPES = mapOf(
                R2.string.optimizer_adamax to AdamaxOptimizer::class.java,
                R2.string.optimizer_adam to AdamOptimizer::class.java,
                R2.string.optimizer_adaptive_gradient to AdaptiveGradientOptimizer::class.java,
                R2.string.optimizer_gradient_descent to GradientDescentOptimizer::class.java,
                R2.string.optimizer_momentum to MomentumOptimizer::class.java,
                R2.string.optimizer_nesterov_momentum to NesterovMomentumOptimizer::class.java,
                R2.string.optimizer_rmsprop to RMSpropOptimizer::class.java)

        val OPTIMIZER_IDS = arrayOf(
                R2.string.optimizer_adaptive_gradient,
                R2.string.optimizer_rmsprop,
                R2.string.optimizer_adam,
                R2.string.optimizer_adamax,
                R2.string.optimizer_gradient_descent,
                R2.string.optimizer_momentum,
                R2.string.optimizer_nesterov_momentum)
    }
}