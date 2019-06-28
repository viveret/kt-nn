package com.viveret.tinydnn.model

interface IModel<T> {
    fun sameModelAs(other: T): Boolean
}