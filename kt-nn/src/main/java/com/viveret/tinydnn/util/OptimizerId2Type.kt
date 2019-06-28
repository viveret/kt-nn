package com.viveret.tinydnn.util

import com.viveret.tinydnn.R
import com.viveret.tinydnn.optimizer.*

class OptimizerId2Type {
    companion object {
        val OPTIMIZER_TYPES = mapOf(
                R.string.optimizer_adamax to AdamaxOptimizer::class.java,
                R.string.optimizer_adam to AdamOptimizer::class.java,
                R.string.optimizer_adaptive_gradient to AdaptiveGradientOptimizer::class.java,
                R.string.optimizer_gradient_descent to GradientDescentOptimizer::class.java,
                R.string.optimizer_momentum to MomentumOptimizer::class.java,
                R.string.optimizer_nesterov_momentum to NesterovMomentumOptimizer::class.java,
                R.string.optimizer_rmsprop to RMSpropOptimizer::class.java)

        val OPTIMIZER_IDS = arrayOf(
                R.string.optimizer_adaptive_gradient,
                R.string.optimizer_rmsprop,
                R.string.optimizer_adam,
                R.string.optimizer_adamax,
                R.string.optimizer_gradient_descent,
                R.string.optimizer_momentum,
                R.string.optimizer_nesterov_momentum)
    }
}