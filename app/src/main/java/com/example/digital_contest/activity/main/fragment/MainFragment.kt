package com.example.digital_contest.activity.main.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.digital_contest.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class MainFragment:Fragment(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {
    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseFirestore
    private var permissionDenied = false
    private lateinit var map: GoogleMap


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_tab, container, false)
    }
    fun newInstant() : MainFragment
    {
        val args = Bundle()
        val frag = MainFragment()
        frag.arguments = args
        return frag
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        class MainActivity : AppCompatActivity()

        //getAddress(LatLng(37.5759396,126.9758432))

    }

    override fun onResume() {
        super.onResume()
        setUpMapIfNeeded()
        if (permissionDenied) {
            // 권한이 부여되지 않으면 오류 메세지 출력
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap ?: return
        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)
        enableMyLocation()
        googleMap.addMarker(MarkerOptions().position(LatLng(0.0, 0.0)).title("Marker"))
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(requireContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        // 카메라가 현재 위치로 이동됨
        return false
    }

    override fun onMyLocationClick(location: Location) {
        //location 에 위치 정보
        Toast.makeText(requireContext(), "Current location:\n$location", Toast.LENGTH_LONG).show()
        Log.d(TAG, "location : ${location.latitude} ${location.longitude}")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
        enableMyLocation()

    }


    private fun setUpMapIfNeeded() {
        val mapFragment : SupportMapFragment = SupportMapFragment.newInstance()
        childFragmentManager.beginTransaction().replace(R.id.map, mapFragment, "MapTag").commit()
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        } else {
        }
    }

    private fun showMissingPermissionError() {

    }

    private fun getAddress(position: LatLng) {
        val geoCoder = Geocoder(context, Locale.getDefault())
        val address =
            geoCoder.getFromLocation(position.latitude, position.longitude, 1).first()
                .getAddressLine(0)

        Log.e("Address", address)
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }




}
