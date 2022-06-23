/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.amphibians.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amphibians.network.Amphibian
import com.example.amphibians.network.AmphibianApi
import kotlinx.coroutines.launch

enum class AmphibianApiStatus {LOADING, ERROR, DONE}

class AmphibianViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<AmphibianApiStatus>()

    // The backing property for accessing the value of status outside the class
    val status: LiveData<AmphibianApiStatus> = _status

    /**
     * We use [MutableLiveData] because we will be updating the List with new values
     */
    private val _amphibians = MutableLiveData<List<Amphibian>>()

    /**
     *  The backing property however,
     *  will be used to store the selected amphibian shown on the detail screen
     */
    val amphibians: LiveData<List<Amphibian>> = _amphibians

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _type = MutableLiveData<String>()
    val type: LiveData<String> = _type

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    /**
     * Call getAmphibianList() so that it can display status immediately
     */
    init {
        getAmphibianList()
    }

    /**
     * Gets Amphibian info from the API Retrofit service and updates the
     * [Amphibian] [List] [LiveData]
     */
    fun getAmphibianList(){

        viewModelScope.launch {
            _status.value = AmphibianApiStatus.LOADING
            try{
                _amphibians.value = AmphibianApi.retrofitService.getInfo()
                _status.value = AmphibianApiStatus.DONE
            } catch(e: Exception){
                _amphibians.value = listOf()
                _status.value = AmphibianApiStatus.ERROR
            }

        }
    }

    /**
     * Depending on which amphibian is clicked, the values in  [AmphibianViewModel]
     * should change accordingly
     */
    fun onAmphibianClicked(amphibian: Amphibian) {
        _name.value = amphibian.name
        _type.value = amphibian.type
        _description.value = amphibian.description
    }
}
