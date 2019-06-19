package com.viveret.tinydnn.project

import android.webkit.ConsoleMessage
import com.viveret.tinydnn.data.DataMethod
import com.viveret.tinydnn.data.SaveConfig
import com.viveret.tinydnn.project.actions.ProjectAction
import com.viveret.tinydnn.tinydnn.model.INetworkModelWithWeights
import java.text.SimpleDateFormat
import java.util.*


class NeuralNetProject(private val myNetworkModelWithWeights: INetworkModelWithWeights) {
    val id: UUID = UUID.randomUUID()

    private val observers = HashSet<INeuralNetworkObserver>()

    fun addObserver(observer: INeuralNetworkObserver) {
        this.observers.add(observer)
    }

    fun removeObserver(observer: INeuralNetworkObserver) {
        this.observers.remove(observer)
    }

    fun notifyObservers(event: ProjectAction) {
        for (observer in this.observers) {
            observer.onNeuralNetworkChange(this, event)
        }
    }

    val savedMessages = ArrayList<SavedMesssage>()

    var name: String
        get() = myNetworkModelWithWeights.name
        set(name) {
            this.myNetworkModelWithWeights.name = name
        }

    fun get(): INetworkModelWithWeights = this.myNetworkModelWithWeights

    override fun toString(): String = myNetworkModelWithWeights.toString()

    fun hasUnsavedChanges(): Boolean {
        return false
    }

    fun discardChanges() {

    }

    fun saveMessage(messageLevel: ConsoleMessage.MessageLevel, message: String) {
        this.savedMessages.add(SavedMesssage(messageLevel, message))
    }

    fun supportsDataMethod(dataMethod: DataMethod): Boolean = dataMethod.supports(this.get().in_data_size())

    fun save(saveConfig: SaveConfig) {
        try {
            this.get().save(saveConfig.outputFile.absolutePath, saveConfig.saveWhat, saveConfig.saveFormat)
            saveConfig.saved()
        } catch (e: Exception) {
            throw Exception("Could not save file", e)
        }
    }

    class SavedMesssage(val messageLevel: ConsoleMessage.MessageLevel, val message: String) {
        val timestamp = Calendar.getInstance().time

        override fun toString(): String = "${df.format(timestamp)} ${this.messageLevel.name}> $message"

        companion object {
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        }
    }
}
