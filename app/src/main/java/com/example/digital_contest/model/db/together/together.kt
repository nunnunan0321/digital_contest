package com.example.digital_contest.model.db.together

import android.net.Uri
import com.example.digital_contest.model.TogetherBoard

interface together {
    suspend fun saveBoard(together: TogetherBoard, img : Uri) : TogetherResult
    suspend fun getAllBoardBtRootId(rootBoardID : String) : List<TogetherBoard>
    suspend fun saveImg(img : Uri, writerId : String) : String?
}