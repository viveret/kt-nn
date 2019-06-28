package com.viveret.tinydnn.enums

enum class grad_check_mode {
    GRAD_CHECK_ALL, ///< check all elements of Weights
    GRAD_CHECK_RANDOM  ///< check 10 randomly selected Weights
}
