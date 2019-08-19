package com.viveret.tinydnn.error

class UserPropagatedException(val originalException: Exception): Exception(originalException)