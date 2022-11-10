package com.example.myapplication

import androidx.compose.runtime.mutableStateOf

class MainViewModel {
//
//    lateinit var question:String
//    val OptionsList =  mutableStateListOf<String>()

    val firstName = mutableStateOf("")
    val lastName = mutableStateOf("")
    val email = mutableStateOf("")
    val mobileNum = mutableStateOf(0L)
    val age = mutableStateOf(0)
    val selectedCourse = mutableStateOf("")
    val selectedSubCourse = mutableStateOf("")

}