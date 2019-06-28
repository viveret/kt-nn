package com.viveret.tinydnn.reflection.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CONSTRUCTOR)
annotation class UserConstructor(val name: String, val description: String)