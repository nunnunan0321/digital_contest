package com.example.digital_contest.Activity.Main.Fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.digital_contest.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirstTabFragment:Fragment(), OnMapReadyCallback {
    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first_tab, container, false)
    }
    fun newInstant() : FirstTabFragment
    {
        val args = Bundle()
        val frag = FirstTabFragment()
        frag.arguments = args
        return frag
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        class MainActivity : AppCompatActivity()

//        val mapFragment = activity?.supportFragmentManager?.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)

    }

    override fun onResume() {
        super.onResume()
        setUpMapIfNeeded()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(MarkerOptions().position(LatLng(0.0, 0.0)).title("Marker"))
    }

    private fun setUpMapIfNeeded() {
        val mapFragment : SupportMapFragment = SupportMapFragment.newInstance()
        childFragmentManager.beginTransaction().replace(R.id.map, mapFragment, "MapTag").commit()
        mapFragment.getMapAsync(this)
    }

}
