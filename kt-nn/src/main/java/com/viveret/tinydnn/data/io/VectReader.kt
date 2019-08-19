package com.viveret.tinydnn.data.io

import com.viveret.tinydnn.basis.AttributeResolver
import com.viveret.tinydnn.basis.Vect

interface VectReader : SimpleReader<Vect>, AttributeResolver {
}