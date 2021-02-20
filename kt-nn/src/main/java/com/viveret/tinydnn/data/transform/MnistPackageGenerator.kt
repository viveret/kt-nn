package com.viveret.tinydnn.data.transform

import android.content.Context
import com.viveret.tinydnn.R
import com.viveret.tinydnn.basis.*
import com.viveret.tinydnn.data.ConstDataStream
import com.viveret.tinydnn.data.io.OutputSelection
import com.viveret.tinydnn.model.CommonNetworkFilters
import com.viveret.tinydnn.model.INetworkModelWithWeights
import com.viveret.tinydnn.project.actions.GeneratePackageAction
import java.io.File
import java.util.*
import java.util.zip.GZIPOutputStream
import kotlin.math.min
import kotlin.math.roundToInt

class MnistPackageGenerator(val context: Context): PackageGenerator {
    override fun begin(options: GeneratePackageAction): OutputSelection {
        val ret = OutputSelection()
        val originalFile = File(options.path)
        val inputFile = File(originalFile.parent, "${originalFile.nameWithoutExtension}-images-idx3-ubyte.${originalFile.extension}")
        val inputFileId = UUID.randomUUID()
        ret[DataRole.Input] = OutputSelection.Item(ConstDataStream(context, FileStreamProvider(inputFile.path, inputFileId), inputFile.name, FileStream.extensionToMime(inputFile.extension), inputFile.extension, inputFile.parent!!, DataRole.Input), GZIPOutputStream(inputFile.outputStream()))
        writeMetadata(arrayOf(32, 32).toIntArray(), options.size, 2051, ret[DataRole.Input]!!)

        if (options.includeFitTo) {
            val fitToFile = File(originalFile.parent, "${originalFile.nameWithoutExtension}-labels-idx1-ubyte.${originalFile.extension}")
            val fitToFileId = UUID.randomUUID()
            ret[DataRole.FitTo] = OutputSelection.Item(ConstDataStream(context, FileStreamProvider(fitToFile.path, fitToFileId), fitToFile.name, FileStream.extensionToMime(fitToFile.extension), fitToFile.extension, fitToFile.parent!!, DataRole.FitTo), GZIPOutputStream(fitToFile.outputStream()))
            writeMetadata(emptyArray<Int>().toIntArray(), options.size, 2049, ret[DataRole.FitTo]!!)
        }
        return ret
    }

    private fun writeMetadata(dims: IntArray, elementCount: Int, magicNumber: Int, item: OutputSelection.Item) {
        val it = item.stream
        it.writeInt(magicNumber)
        it.writeInt(elementCount)
        for (d in dims) {
            it.writeInt(d)
        }
    }

    override fun append(data: DataSlice, output: OutputSelection, options: GeneratePackageAction) {
        val os = output[DataRole.Input]!!.stream
        for (v in data[DataRole.Input]!!.first.vals) {
            os.write((v * 255).roundToInt())
        }

        val fitTo = data[DataRole.FitTo]
        if (fitTo != null) {
            val fitToStream = output[DataRole.FitTo]!!.stream
            if (fitTo.first.vals.size > 1) {
                var maxVal = Float.MIN_VALUE
                var maxIndex = -1
                for (i in 0 until fitTo.first.vals.size) {
                    if (maxVal < fitTo.first.vals[i]) {
                        maxVal = fitTo.first.vals[i]
                        maxIndex = i
                    }
                }
                fitToStream.write(maxIndex)
            } else {
                for (v in fitTo.first.vals) {
                    fitToStream.write((v * 255).roundToInt())
                }
            }
        }

        output.elementCount++
    }

    override fun isMatch(project: INetworkModelWithWeights): Boolean = CommonNetworkFilters.MNIST.isMatch(project)

    override fun generate(data: List<Vect>, fitTo: List<Vect>?, options: GeneratePackageAction): OutputSelection {
        if (fitTo != null && data.size != fitTo.size) {
            throw IllegalArgumentException("Size mismatch (input is ${data.size} but output is ${fitTo.size}")
        }

        val output = this.begin(options)
        for (i in 0 until min(options.size, data.size)) {
            this.append(DataSlice(DataRole.Input to Pair(data[i], 0L), DataRole.FitTo to Pair(fitTo!![i], 0L)), output, options)
        }
        output.close()
        /*val images = from[DataRole.Input] ?: throw UserException("Missing input location for source")
        val outputFile = to[DataRole.Input] ?: throw UserException("Missing input location for destination")
        if (from.elementCount > 1 || to.elementCount > 1) {
            throw UserException("Only provide input")
        }

        DataOutputStream(outputFile.stream).use {
            it.writeInt(2051)
            it.writeInt(images.elementCount)
            it.writeInt(32)
            it.writeInt(32)
            for (img in images) {
                val original = BitmapFactory.decodeFile(img.file.absolutePath)
                val scaled = Bitmap.createScaledBitmap(original, 32, 32, true)
                for (y in 0 until scaled.height) {
                    for (x in 0 until scaled.width) {
                        val pixel = scaled.getPixel(x, y)
                        val avg = (Color.red(pixel) + Color.green(pixel) + Color.blue(pixel)) / 3
                        it.write(avg)
                    }
                }
            }
        }*/
        return output
    }

    private val extensions = arrayOf("jpeg", "jpg", "png", "bmp")
    override val name: Int = R.string.title_mnist_pkg_gen

    override fun filter(item: File): Boolean = extensions.contains(item.extension.toLowerCase())
}