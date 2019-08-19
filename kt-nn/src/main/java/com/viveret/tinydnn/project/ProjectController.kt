package com.viveret.tinydnn.project

import android.webkit.ConsoleMessage
import com.viveret.tinydnn.basis.Vect
import com.viveret.tinydnn.data.DataMethod
import com.viveret.tinydnn.data.DataValues
import com.viveret.tinydnn.data.Scenario
import com.viveret.tinydnn.data.train.DataSliceReader
import com.viveret.tinydnn.data.train.TrainingMethod
import com.viveret.tinydnn.util.async.OnItemSelectedListener

interface ProjectController : OnItemSelectedListener, ProjectProvider {
    fun message(msgLvL: ConsoleMessage.MessageLevel, message: String)
    fun trainUsingMethod(method: TrainingMethod)
    fun trainUsingScenario(scenario: Scenario)
    fun trainOnData(trainingData: DataValues)
    fun predictUsingDataMethod(dataMethod: DataMethod)
    fun predictUsingScenario(scenario: Scenario)
    fun judgeUsingScenario(scenario: Scenario)
    fun judgeUsingDataMethod(dataMethod: DataMethod)
    fun judgeUsingData(data: DataSliceReader)
    fun predictOutputFromInput(data: Vect)
    fun selectDataValueFromSet(data: DataSliceReader)
}
