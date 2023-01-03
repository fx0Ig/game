package com.example.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.game.data.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainScreenViewModel : ViewModel() {

    private var job: Job? = null
    var result = MutableLiveData<Int>()
    var link = MutableLiveData<String>()

    fun getShowGame(): MutableLiveData<Int> {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = GameRepository.getFirebaseGamePass()
            val linkResponse = GameRepository.getFirebaseWeblink()
            withContext(Dispatchers.Main) {
                link.value = linkResponse
                when (response) {
                    true -> result.value = 0
                    false -> result.value = 1
                }
            }
        }
        return result
    }
}