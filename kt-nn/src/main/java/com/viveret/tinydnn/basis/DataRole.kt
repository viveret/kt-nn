package com.viveret.tinydnn.basis

enum class DataRole(val altPurpose: DataRole) {
    NA(NA),
    Input(NA),
    FitTo(Input),
    InputLabels(NA),
    FitToLabels(InputLabels)
}