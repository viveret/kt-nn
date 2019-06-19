package com.viveret.tinydnn.tinydnn.model

interface IModel<T> {
    fun sameModelAs(other: T): Boolean
}