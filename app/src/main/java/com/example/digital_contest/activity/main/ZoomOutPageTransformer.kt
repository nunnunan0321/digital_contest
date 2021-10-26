package com.example.digital_contest.activity.main

import android.view.View
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

//화면 크기 상수
private const val MIN_SCALE = 0.85f //축소된 화면 크기
private const val MIN_ALPHA = 0.5f //축소가 적용될 드래그 반경

// https://developer.android.com/training/animation/screen-slide#pagetransformer (코드 참고)
class ZoomOutPageTransformer : ViewPager2.PageTransformer, ViewPager.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            val pageHeight = height
            when {
                position < -1 -> { // [-Infinity,-1)
                    // 회면이 왼쪽으로 벗어났을 때
                    alpha = 0f
                }
                position <= 1 -> { // [-1,1]
                    // 페이지 축소, 기본 슬라이드 전환
                    val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horzMargin = pageWidth * (1 - scaleFactor) / 2
                    translationX = if (position < 0) {
                        horzMargin - vertMargin / 2
                    } else {
                        horzMargin + vertMargin / 2
                    }

                    // 페이지 축소
                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    // 크기에 비례
                    alpha = (MIN_ALPHA +
                            (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                }
                else -> { // (1,+Infinity]
                    // 회면이 오른쪽으로 벗어났을 때
                    alpha = 0f
                }
            }
        }
    }

}