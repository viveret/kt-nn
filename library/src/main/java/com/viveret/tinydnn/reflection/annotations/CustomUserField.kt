package com.viveret.tinydnn.reflection.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class CustomUserField(val name: Int, val hint: Int, val inputType: Int) {
}