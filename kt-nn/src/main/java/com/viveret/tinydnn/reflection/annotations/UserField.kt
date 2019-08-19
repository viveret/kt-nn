package com.viveret.tinydnn.reflection.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class UserField(val type: UserFields)