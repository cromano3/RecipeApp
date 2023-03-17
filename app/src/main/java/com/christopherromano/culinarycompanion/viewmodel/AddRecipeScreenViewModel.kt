package com.christopherromano.culinarycompanion.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.christopherromano.culinarycompanion.datamodel.AddRecipeScreenUiStateDataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AddRecipeScreenViewModel(application: Application): ViewModel() {

    val uiState = MutableStateFlow(AddRecipeScreenUiStateDataModel())

    fun addInstructionField(){

        var myList = uiState.value.instructionsList

        myList = myList.map { it } as MutableList<String>

        myList.add("")

        uiState.update {
            it.copy(
                instructionsList = myList
            )
        }

    }

    fun deleteInstructionField(){

        var myList = uiState.value.instructionsList

        myList = myList.map { it } as MutableList<String>

        myList.removeLast()

        uiState.update {
            it.copy(
                instructionsList = myList
            )
        }

    }

    fun updateInstruction(text: String, xIterator: Int){

        var myList: MutableList<String> = uiState.value.instructionsList

        println(text)

        myList = myList.mapIndexed {index, it -> if(xIterator == index) text else it } as MutableList<String>

        println(myList)

        uiState.update {
            it.copy(
                instructionsList = myList
            )
        }

    }

}