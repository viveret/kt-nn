package com.viveret.tinydnn.data.formats

import android.content.Context
import com.viveret.tinydnn.basis.*
import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.data.io.*
import com.viveret.tinydnn.data.train.DataSliceReader
import com.viveret.tinydnn.project.NeuralNetProject
import java.io.File
import java.util.*

@Mime(["text/plain"])
class CMUMovieSummaryCorpusFormat(context: Context) : DataSliceReader {
    override val openRoles: Array<DataRole>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun read(destination: DataValues, offset: Int, amountToRead: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun open(inputSelection: InputSelection) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInt(attr: DataAttr): Int? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getString(attr: DataAttr): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBoolean(attr: DataAttr): Boolean? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun seek(relativeTo: AnchorPoint, offset: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun vectReader(role: DataRole): VectReader? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun labelReader(role: DataRole): LabelReader? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
//
//    override fun getDataValues(tasks: Map<Stream, File>, dataSuite: StreamPackage, project: NeuralNetProject?): DataValues {
//        val inputFile = dataSuite.streams.getValue(DataRole.Input)
//        val labelFile = dataSuite.streams[DataRole.InputLabels]
//        val inputVects = readVects(DataRole.Input, tasks.getValue(inputFile), inputFile, this.inputVectReader, project!!.get().in_data_size())
//        val inputLabels = if (labelFile != null && tasks.containsKey(labelFile)) readLabels(DataRole.InputLabels, tasks.getValue(labelFile)) else ArrayList()
//        val vals = DataValues(inputVects, inputLabels)
//        vals.format = this
//        return vals
//    }

//    override fun read(tasks: Map<Stream, File>, dataSuite: StreamPackage, project: NeuralNetProject?): TrainingDataValues? {
//        val inputFile = dataSuite.streams.getValue(DataRole.Input)
//        val labelFile = dataSuite.streams[DataRole.InputLabels]
//        val fitToFile = dataSuite.streams[DataRole.FitTo]
//        val fitToLabelsFile = dataSuite.streams[DataRole.FitToLabels]
//        val inputVects = readVects(DataRole.Input, tasks.getValue(inputFile), inputFile, this.inputVectReader, project!!.get().in_data_size())
//        val inputLabels = if (labelFile != null && tasks.containsKey(labelFile)) readLabels(DataRole.InputLabels, tasks.getValue(labelFile)) else ArrayList()
//
//        return if (fitToFile != null && tasks.containsKey(fitToFile)) {
//            val fitToVects = readVects(DataRole.FitTo, tasks.getValue(fitToFile), fitToFile, this.fitToVectReader, project.get().out_data_size())
//            val fitToLabels = if (fitToLabelsFile != null && tasks.containsKey(fitToLabelsFile)) readLabels(DataRole.FitToLabels, tasks.getValue(fitToLabelsFile)) else ArrayList()
//            val vals = TrainingDataValues(inputVects, inputLabels, fitToVects, fitToLabels)
//            vals.format = this
//            vals.fitTo!!.format = this
//            vals
//        } else {
//            val vals = TrainingDataValues(inputVects, inputLabels)
//            vals.format = this
//            vals
//        }
//    }

    fun readLabels(purpose: DataRole, file: File): ArrayList<Long> {
        throw Exception()
    }
//    override val inputVectReader = SummaryVectReader(true)
//    override val inputLabelReader
//            get() = throw Exception()
//    override val fitToVectReader = MovieMetaDataReader(false)
//    override val fitToLabelReader
//            get() = throw Exception()

    class SummaryVectReader(normalizeBytes: Boolean) : TsvReader(normalizeBytes, false), VectReader {
        override fun getInt(attr: DataAttr): Int? = null

        override fun getString(attr: DataAttr): String? = null

        override fun getBoolean(attr: DataAttr): Boolean? = null

        override val supportsSeek: Boolean = false

        override fun seek(relativeTo: AnchorPoint, offset: Int): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun open(inputStream: BetterInputStream) {
            this.openTsv(inputStream)
        }

        override fun read(destination: Array<Vect>, offset: Int, count: Int): Int {
            val numberOfVectsToRead = destination.size
            var numVectsRead = 0
            for (vectIndex in 0 until numberOfVectsToRead) {
                val line = this.readLine()
                val summaryString = if (line.size > 9) line[9].toLowerCase() else ""
                val summaryText = FloatArray(if (count > 0) count else summaryString.length, if (normalizeBytes) {
                    { i -> if (i < summaryString.length) summaryString[i].toFloat() / Char.MAX_VALUE.toFloat() else 0.0f }
                } else {
                    { i -> if (i < summaryString.length) summaryString[i].toFloat() else 0.0f }
                })
                destination[vectIndex] = Vect(summaryText, summaryText.size)
                numVectsRead++
            }
            return numVectsRead
        }

        override val isOpen = true

        override fun close() = this.inputStream.close()
    }

    class MovieMetaDataReader(normalizeBytes: Boolean) : TsvReader(normalizeBytes, false), VectReader {
        override fun getInt(attr: DataAttr): Int? = null

        override fun getString(attr: DataAttr): String? = null

        override fun getBoolean(attr: DataAttr): Boolean? = null

        override val supportsSeek: Boolean = false

        override fun seek(relativeTo: AnchorPoint, offset: Int): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun open(inputStream: BetterInputStream) {
            this.openTsv(inputStream)
        }

        override fun read(destination: Array<Vect>, offset: Int, count: Int): Int {
            val numberOfVectsToRead = destination.size
            var numVectsRead = 0
            for (vectIndex in 0 until numberOfVectsToRead) {
                val line = this.readLine()
                if (line.size > 8) {
                    val categoriesJsonArray = line[8]
                    val categories = outputLabels.map { x -> categoriesJsonArray.indexOf(x, ignoreCase = true) }
                    val probabilities = FloatArray(categories.size) { i -> if (categories[i] >= 0) 1.0f else 0.0f}
                    destination[numVectsRead] = Vect(probabilities, probabilities.size)
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