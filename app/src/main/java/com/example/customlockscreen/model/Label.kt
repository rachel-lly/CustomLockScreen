package com.example.customlockscreen.model


class Label(var text : String,var date: Long){

        var day = (date-System.currentTimeMillis())/(1000*3600*24)

//        fun getDay(): Long =

}


