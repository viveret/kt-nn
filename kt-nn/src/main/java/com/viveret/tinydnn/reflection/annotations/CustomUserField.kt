package com.viveret.tinydnn.reflection.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class CustomUserField(val name: String, val hint: String, val inputType: Int)