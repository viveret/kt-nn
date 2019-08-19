package com.viveret.tinydnn.data.transform

import com.viveret.tinydnn.basis.DataSlice
import com.viveret.tinydnn.basis.Vect
import com.viveret.tinydnn.data.io.OutputSelection
import com.viveret.tinydnn.model.NetworkModelFilter
import com.viveret.tinydnn.project.actions.GeneratePackageAction
import java.io.File

interface PackageGenerator: NetworkModelFilter {
    val name: Int
    fun filter(item: File): Boolean
    fun begin(options: GeneratePackageAction): OutputSelection
    fun append(data: DataSlice, output: OutputSelection, options: GeneratePackageAction)
    fun generate(data: List<Vect>, fitTo: List<Vect>?, options: GeneratePackageAction): OutputSelection
}