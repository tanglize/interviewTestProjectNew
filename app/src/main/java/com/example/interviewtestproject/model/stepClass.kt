package com.example.interviewtestproject.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class StepClass( var stepData:ArrayList<String>, var recipeData:RecipeClass):
    Parcelable {
    //        constructor() : this(
//        ""
//        "",
//        "",
//        hostelLocation ""
//
//    )
    constructor() : this(arrayListOf(),RecipeClass())
}