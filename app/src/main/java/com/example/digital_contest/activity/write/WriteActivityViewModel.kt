package com.example.digital_contest.activity.write

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.digital_contest.activity.sphash.boardDB
import com.example.digital_contest.activity.sphash.currentLocation
import com.example.digital_contest.model.Board
import com.example.digital_contest.model.User
import com.example.digital_contest.model.db.Board.BoardResult

class WriteActivityViewModel : ViewModel() {
    val userData = MutableLiveData<User>()
    val title = MutableLiveData<String>()
    val content = MutableLiveData<String>()
    val img = MutableLiveData<Uri>()

    suspend fun saveBoard() : BoardResult {
        var result: BoardResult

        val boardData = Board(
            title = title.value.toString(),
            writerID = userData.value!!.id,
            contents = content.value.toString(),
            location = currentLocation,
        )

        result = boardDB.saveBoard(boardData, img.value!!)

        return result
    }
}