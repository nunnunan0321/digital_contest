package com.example.digital_contest.model.db.Board

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.digital_contest.activity.sphash.boardDB
import com.example.digital_contest.model.Board
import com.firebase.geofire.GeoLocation
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BoardDB : board {
    val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    lateinit var mLocationManager: LocationManager
    lateinit var mLocationListener: LocationListener

    override suspend fun saveBoard(boardData: Board): BoardResult {
        var result : BoardResult = BoardResult.OK

        db.collection("board").add(boardData)
            .addOnFailureListener {
                result = BoardResult.Fail
            }
            .await()
        return result
    }
}