package com.batuy.neco_shopinglist.utils

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

class MyOnTouchListener:View.OnTouchListener {
    @SuppressLint("SuspiciousIndentation", "ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent?): Boolean {
        var xDelta=0.0f
        var yDelta=0.0f
                when(event?.action){
                    MotionEvent.ACTION_DOWN->{
                        xDelta=v.x-event.rawX
                        yDelta=v.y-event.rawY
                    }
                    MotionEvent.ACTION_MOVE->{
                        v.x=xDelta+event.rawX
                        v.x=yDelta+event.rawY
                    }
                }
        return true
    }
}