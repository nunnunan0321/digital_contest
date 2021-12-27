package com.example.digital_contest.activity.main.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.digital_contest.R
import com.example.digital_contest.activity.main.MainActivity
import com.example.digital_contest.activity.sphash.boardDB
import com.example.digital_contest.activity.sphash.currentLocation
import com.example.digital_contest.activity.view.ViewActivity
import com.example.digital_contest.activity.write.WriteActivity
import com.example.digital_contest.databinding.FragmentMainTabBinding
import com.example.digital_contest.model.Board
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.*
import java.util.*


class MainFragment:Fragment(),
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var binding : FragmentMainTabBinding
    private var permissionDenied = false
    private lateinit var map: GoogleMap
    var userLocationLatitude : Double = 0.0 // 위치 정보는 어디서든지 접근 가능함
    var userLocationLongitude : Double = 0.0
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallbackCheck : Boolean = true

    lateinit var launch : Job


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_tab, container, false)
        val root = binding.root

        binding.floatBtnMainGotoWrite.setOnClickListener {
            val intent = Intent(requireContext(), WriteActivity::class.java)
            intent.putExtra("userData", (activity as MainActivity).userData)
            startActivity(intent)
        }

        return root
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


    override fun onStart() {
        super.onStart()
        locationCallbackCheck = true
        printMap()
    }

    override fun onResume() {
        super.onResume()
        locationCallbackCheck = true
        printMap()
    }

    override fun onPause() {
        super.onPause()
        locationCallbackCheck = true
    }

    override fun onStop() {
        super.onStop()
        locationCallbackCheck = true
    }

    // 지도가 준비되었을 때 실행되는 함수
    @SuppressLint("MissingPermission") // Permission 컴파일 에러 제거
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap ?: return
        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)
        enableMyLocation()

        launch = CoroutineScope(Dispatchers.Main).launch {
            val boardsData : Map<String, Board> = boardDB.getAllBoard()

            for (key in boardsData.keys){
                val board = boardsData[key]!!

                val bd: BitmapDescriptor? = if(board.writerID == (activity as MainActivity).userData.id)
                                        activity?.let { bitmapDescriptorFromVector(it, R.drawable.marker_img_2) }
                                else activity?.let { bitmapDescriptorFromVector(it, R.drawable.marker_img_1) }

                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(board.location.latitude, board.location.longitude))
                        .title(board.title)
                        .icon(bd)
                ).tag = key
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            launch.join()
        }

        map.setOnMarkerClickListener(markerClickListener)
        map.setOnInfoWindowClickListener(markerTitleClickListener)

        locationUpdateCallback()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        launch.cancel()
    }

    // 마커를 클릭했을 때 실행되는 함수
    var markerClickListener = OnMarkerClickListener { marker ->
            val markerTitle = marker.title
            // 선택한 타겟의 위치
            val location = marker.position

            false
        }

    // 마커의 타이틀을 클릭했을 때 실행되는 함수
    var markerTitleClickListener = GoogleMap.OnInfoWindowClickListener { marker ->
            val markerId = marker.tag
//            Toast.makeText(requireContext(), "마커 타이틀 클릭 Marker Title : $markerTitle", Toast.LENGTH_SHORT).show()

            val intent = Intent(requireContext(), ViewActivity::class.java)
            intent.putExtra("boardId", markerId.toString())
            intent.putExtra("userData", (activity as MainActivity).userData)
            startActivity(intent)
        }

    // 지도 내에서 내 위치 버튼을 클릭했을 때 실행되는 함수
    override fun onMyLocationButtonClick(): Boolean {
        // 카메라가 현재 위치로 이동됨
        return false
    }

    // 지도 내에서 현재 위치를 클릭했을 때(파란색) 실행되는 함수
    override fun onMyLocationClick(location: Location) {
        //location 에 위치 정보
//        Log.d(TAG, "location : ${location.latitude} ${location.longitude}")
        //map.addMarker(MarkerOptions().position(LatLng(10.0, 0.0)).title("Marker"))
    }


    // Permisson 에 대한 결과를 반환하는 함수
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
        enableMyLocation()

    }

    // 맵이 켜지지 않았을 때 실행되는 함수
    private fun setUpMapIfNeeded() {
        val mapFragment : SupportMapFragment = SupportMapFragment.newInstance()
        childFragmentManager.beginTransaction().replace(R.id.map, mapFragment, "MapTag").commit()
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        // Permission Check
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        } else {
        }
    }

    // Permission 이 제대로 들어오지 않았을 때 실행되는 함수
    private fun showMissingPermissionError() {

    }

    // 현재 위치 정보 업데이트 중지
//    private fun stopLocationUpdates() {
//        fusedLocationClient.removeLocationUpdates(locationCallback)
//    }


    // 현재 위치를 주소로 반환 (ex: 대한민국 서울시 ~~)
    private fun getAddress(position: LatLng) {
        val geoCoder = Geocoder(context, Locale.getDefault())
        val address =
            geoCoder.getFromLocation(position.latitude, position.longitude, 1).first()
                .getAddressLine(0)

        Log.e("Address", address)
    }

    @SuppressLint("MissingPermission")
    private fun locationUpdateCallback() {
        fusedLocationClient =  LocationServices.getFusedLocationProviderClient(requireActivity())

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 20 * 1000

        // location 콜백
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {
                        // 위치
                        val latitude = location.latitude
                        val longitude = location.longitude

                        // 아래 두값 전송하면 됨, 사용자 위치임
                        userLocationLatitude = latitude
                        userLocationLongitude = longitude
                        currentLocation = GeoPoint(userLocationLatitude, userLocationLongitude)

                        // locationCallback 이 최초 1회 실행되었을 때만
                        if(locationCallbackCheck) {
                            map.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        latitude,
                                        longitude
                                    ), 14.0F
                                )
                            )
                        }
                    }
                }
                locationCallbackCheck = false
            }
        }

        // location 업데이트 요청, 업데이트 시작
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun printMap() {
        setUpMapIfNeeded()
        if (permissionDenied) {
            // 권한이 부여되지 않으면 오류 메세지 출력
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    // 구글 맵 마커 벡터 -> 비트맵 변경
    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}

