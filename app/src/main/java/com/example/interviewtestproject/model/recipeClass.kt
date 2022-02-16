package com.example.interviewtestproject.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RecipeClass(var recipeName:String, var recipeImage: String, var recipeType:String, var recipeUid: String, var recipeImageUri: Uri?, var stepData: ArrayList<String>, var  recipeIngredient : String):
    Parcelable {
    //        constructor() : this(
//        ""
//        "",
//        "",
//        hostelLocation ""
//
//    )
    constructor() : this("","","","", Uri.EMPTY,arrayListOf<String>(), "")
}