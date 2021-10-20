package com.example.digital_contest.Activity.Main.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.digital_contest.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirstTabFragment:Fragment(), OnMapReadyCallback {
    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseFirestore
    lateinit var mapFragment : MapView
    private val MAPVIEW_BUNDLE_KEY = "AIzaSyDTwDqbndD9z0kxeJYS6aiC1ZU1ZfvA3LA"

    override fun onCreateView(//view를 넣어주는 역할을
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

        var mapViewBundle : Bundle? = null
        if(savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mapFragment = activity?.supportFragmentManager?.findFragmentById(R.id.map) as MapView
        mapFragment.onCreate(mapViewBundle)
        mapFragment!!.getMapAsync(this)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle : Bundle? = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if(mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mapFragment.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        mapFragment.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapFragment.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapFragment.onStop()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(MarkerOptions().position(LatLng(0.0, 0.0)).title("Marker"))
    }

    override fun onPause() {
        mapFragment.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapFragment.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapFragment.onLowMemory()
    }
}
