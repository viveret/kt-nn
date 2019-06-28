package com.viveret.tinydnn.data.repo

import com.viveret.tinydnn.basis.StreamPackage

interface Repo {
    val name: String
    val version: Int
    val packages: List<StreamPackage>
}