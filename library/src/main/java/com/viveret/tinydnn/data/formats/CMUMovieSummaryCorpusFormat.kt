package com.viveret.tinydnn.data.formats

import android.content.Context
import com.viveret.tinydnn.basis.Stream
import com.viveret.tinydnn.basis.StreamPackage
import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.data.io.DataSelection
import com.viveret.tinydnn.data.io.SeekRelativity
import com.viveret.tinydnn.data.io.TsvReader
import com.viveret.tinydnn.data.io.VectReader
import com.viveret.tinydnn.data.train.TrainingDataReader
import com.viveret.tinydnn.data.train.TrainingDataValues
import com.viveret.tinydnn.project.NeuralNetProject
import com.viveret.tinydnn.tinydnn.Vect
import java.io.File
import java.util.*

class CMUMovieSummaryCorpusFormat(context: Context) : TrainingDataReader {
    override fun getDataValues(tasks: Map<Stream, File>, dataSuite: StreamPackage, project: NeuralNetProject?): DataValues {
        val inputVects = readVects("input", tasks.getValue(dataSuite.inputFile), dataSuite.inputFile, this.inputVectReader, project!!.get().in_data_size())
        val inputLabels = if (dataSuite.labelFile != null && tasks.containsKey(dataSuite.labelFile!!)) readLabels("label", tasks.getValue(dataSuite.labelFile!!)) else ArrayList()
        val vals = DataValues(inputVects, inputLabels)
        vals.format = this
        return vals
    }

    override fun getTrainingData(tasks: Map<Stream, File>, dataSuite: StreamPackage, project: NeuralNetProject?): TrainingDataValues? {
        val inputVects = readVects("input", tasks.getValue(dataSuite.inputFile), dataSuite.inputFile, this.inputVectReader, project!!.get().in_data_size())
        val inputLabels = if (dataSuite.labelFile != null && tasks.containsKey(dataSuite.labelFile!!)) readLabels("label", tasks.getValue(dataSuite.labelFile!!)) else ArrayList()

        return if (dataSuite.fitToFile != null && tasks.containsKey(dataSuite.fitToFile!!)) {
            val fitToVects = readVects("fitTo", tasks.getValue(dataSuite.fitToFile!!), dataSuite.fitToFile!!, this.fitToVectReader, project.get().out_data_size())
            val fitToLabels = if (dataSuite.fitToLabelsFile != null && tasks.containsKey(dataSuite.fitToLabelsFile!!)) readLabels("fitToLabel", tasks.getValue(dataSuite.fitToLabelsFile!!)) else ArrayList()
            val vals = TrainingDataValues(inputVects, inputLabels, fitToVects, fitToLabels)
            vals.format = this
            vals.fitTo!!.format = this
            vals
        } else {
            val vals = TrainingDataValues(inputVects, inputLabels)
            vals.format = this
            vals
        }
    }

    fun readVects(purpose: String, file: File, metaInfo: Stream, vectReader: VectReader, maxLength: Long): ArrayList<Vect> {
        val dataSelection = DataSelection()
        dataSelection[purpose] = DataSelection.Item(metaInfo, file.inputStream())
        val vects = ArrayList<Vect>()
        vectReader.open(dataSelection)
        val bufVect = Array(10) { Vect.empty }
        var n = 1
        var bytesRead = 0L
        while (n > 0 && bytesRead < (1000 * 1000)) {
            n = vectReader.read(bufVect, maxLength)
            for (i in 0 until n) {
                vects.add(bufVect[i])
                bytesRead += bufVect[i].vals.size * 4
            }
        }
        vectReader.close()
        return vects
    }

    fun readLabels(purpose: String, file: File): ArrayList<Long> {
        throw Exception()
    }

    override val formatId = UUID.fromString("a30433b0-4da5-11e9-8646-d663bd873d93")!!
    override val inputVectReader = SummaryVectReader(true)
    override val inputLabelReader
            get() = throw Exception()
    override val fitToVectReader = MovieMetaDataReader(false)
    override val fitToLabelReader
            get() = throw Exception()

    class SummaryVectReader(normalizeBytes: Boolean) : TsvReader(normalizeBytes, false), VectReader {
        override val supportsSeek: Boolean = false

        override fun seek(pos: Long, relativeTo: SeekRelativity) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun open(dataSelection: DataSelection) {
            this.openTsv(dataSelection)
        }

        override fun read(destination: Array<Vect>): Int = this.read(destination, -1)

        override fun read(destination: Array<Vect>, maxLength: Long): Int {
            val numberOfVectsToRead = destination.size
            var numVectsRead = 0
            for (vectIndex in 0 until numberOfVectsToRead) {
                val line = this.readLine()
                val summaryString = if (line.size > 9) line[9].toLowerCase() else ""
                val summaryText = FloatArray(if (maxLength > 0) maxLength.toInt() else summaryString.length, if (normalizeBytes) {
                    { i -> if (i < summaryString.length) summaryString[i].toFloat() / Char.MAX_VALUE.toFloat() else 0.0f }
                } else {
                    { i -> if (i < summaryString.length) summaryString[i].toFloat() else 0.0f }
                })
                destination[vectIndex] = Vect(summaryText)
                numVectsRead++
            }
            return numVectsRead
        }

        override val isOpen = true

        override fun close() = this.inputStream.close()
    }

    class MovieMetaDataReader(normalizeBytes: Boolean) : TsvReader(normalizeBytes, false), VectReader {
        override val supportsSeek: Boolean = false

        override fun seek(pos: Long, relativeTo: SeekRelativity) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun open(dataSelection: DataSelection) {
            this.openTsv(dataSelection)
        }

        override fun read(destination: Array<Vect>): Int = this.read(destination, -1)

        override fun read(destination: Array<Vect>, maxLength: Long): Int {
            val numberOfVectsToRead = destination.size
            var numVectsRead = 0
            for (vectIndex in 0 until numberOfVectsToRead) {
                val line = this.readLine()
                if (line.size > 8) {
                    val categoriesJsonArray = line[8]
                    val categories = outputLabels.map { x -> categoriesJsonArray.indexOf(x, ignoreCase = true) }
                    val probabilities = FloatArray(categories.size) { i -> if (categories[i] >= 0) 1.0f else 0.0f}
                    destination[numVectsRead] = Vect(probabilities)
                    numVectsRead++
                }
            }
            return numVectsRead
        }

        override val isOpen: Boolean = true

        override fun close() = this.inputStream.close()
    }

    companion object {
    val outputLabels = arrayOf(
            "Action",
            "Adaptation",
            "Adventure",
            "Age",
            "Americana",
            "Amp",
            "Animation",
            "Anime",
            "Anthology",
            "Anthropology",
            "Archaeology",
            "Art",
            "Auto",
            "Avantgarde",
            "Backstage",
            "Biker",
            "Biographical",
            "Biography",
            "Biopic",
            "Black",
            "Blackandwhite",
            "Blaxploitation",
            "Bmovie",
            "Bollywood",
            "British",
            "Buddy",
            "Childhood",
            "Childrens",
            "ChildrensFamily",
            "Chinese",
            "Christian",
            "Christmas",
            "Cinema",
            "Clef",
            "CMovie",
            "Combat",
            "Comedy",
            "Coming",
            "Computer",
            "Concert",
            "Cop",
            "Costume",
            "Courtroom",
            "Creature",
            "Crime",
            "Cult",
            "Culture",
            "Dance",
            "Delinquency",
            "Detective",
            "Disaster",
            "Disaster",
            "Docudrama",
            "Documentary",
            "Dogme",
            "Domestic",
            "Doomsday",
            "Drama",
            "Dystopia",
            "Educational",
            "Ensemble",
            "Epic",
            "Erotic",
            "Errors",
            "Escape",
            "Existentialism",
            "Experimental",
            "Family",
            "FamilyOriented",
            "Fan",
            "Fantasy",
            "Feature",
            "Feminist",
            "Fiction",
            "Filipino",
            "Future",
            "Gangster",
            "Gay",
            "Gender",
            "Giallo",
            "Gothic",
            "Gross",
            "Grossout",
            "Gulf",
            "Heavenly",
            "Heist",
            "Historical",
            "History",
            "Holiday",
            "Hollywood",
            "Horror",
            "Hybrid",
            "Indie",
            "Interest",
            "Issues",
            "Japanese",
            "Juvenile",
            "Kitchen",
            "Language",
            "Law",
            "Legal",
            "LGBT",
            "Life",
            "Linguistics",
            "Literature",
            "Malayalam",
            "Manners",
            "Marriage",
            "Martial",
            "Media",
            "Medical",
            "Melodrama",
            "Mockumentary",
            "Monster",
            "Motion",
            "Movie",
            "Music",
            "Mystery",
            "Natural",
            "New",
            "Noir",
            "Parody",
            "Period",
            "Piece",
            "Plague",
            "Political",
            "Porn",
            "PreCode",
            "Prison",
            "Problem",
            "Propaganda",
            "Psychological",
            "Racing",
            "Realism",
            "Release",
            "Religious",
            "Remake",
            "Road",
            "Roadshow",
            "Rockumentary",
            "Romance",
            "Romantic",
            "Samurai",
            "Satire",
            "Science",
            "SciFi",
            "Screwball",
            "Serial",
            "Sex",
            "Sexploitation",
            "Short",
            "Silent",
            "Sink",
            "Slapstick",
            "Slasher",
            "Slice",
            "Social",
            "Society",
            "Softcore",
            "Sorcery",
            "Space",
            "Spaghetti",
            "Sports",
            "Spy",
            "Stoner",
            "Stop",
            "Story",
            "Superhero",
            "Supernatural",
            "Surrealism",
            "Suspense",
            "Sword",
            "Teen",
            "Television",
            "Theatrical",
            "Themed",
            "Thriller",
            "Time",
            "Tollywood",
            "Tragedy",
            "Tragicomedy",
            "Travel",
            "War",
            "Wave",
            "Western",
            "Women",
            "World"
    )
}
}