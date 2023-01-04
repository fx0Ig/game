package com.example.game

import androidx.annotation.DrawableRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.game.data.GameRepository
import java.util.Random

class GameViewModel : ViewModel() {

    var score = MutableLiveData<GameResult>()
    private var _score = 0

    fun rolling(isHeads: Boolean) {
        var isUserWon: Boolean = false
        val random = Random()
        val image = when (random.nextInt(2)) {
            0 -> {
                if (isHeads) {
                    _score += 1
                    isUserWon = true
                } else {
                    _score = 0
                }
                R.drawable.eur_front
            }
            1 -> {
                if (!isHeads) {
                    _score += 1
                    isUserWon = true
                } else {
                    _score = 0
                }
                R.drawable.eur_back
            }
            else -> {
                R.drawable.eur_back
            }
        }
        score.postValue(GameResult(_score, isUserWon, image))
    }

    data class GameResult(val score: Int, val isUserWon: Boolean, @DrawableRes val image: Int)
}

