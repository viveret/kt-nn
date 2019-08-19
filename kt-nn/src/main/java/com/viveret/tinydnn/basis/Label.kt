package com.viveret.tinydnn.basis

class Label(val value: Vect, val name: String) {
    override fun equals(other: Any?): Boolean =
        other is Label && (other.name == name || other.value.percentDifference(value) < 0.0001)

    override fun hashCode(): Int = name.hashCode()

    override fun toString(): String = name
}