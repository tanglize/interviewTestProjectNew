package com.example.interviewtestproject.addStep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.interviewtestproject.R
import com.example.interviewtestproject.model.RecipeClass
import com.example.interviewtestproject.updateRecipe.updateRecipe
import kotlinx.android.synthetic.main.activity_edit_step.*

class editStep : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_step)

        var stepDataString = intent.getStringExtra("stepDataString")
        var recipeDetailss = intent.getParcelableExtra<RecipeClass>("recipeDetails")



        if(stepDataString != null){
            editStep_editStep.setText(stepDataString)
            if (recipeDetailss != null) {
                Log.d("edit Step", "${recipeDetailss.stepData.size}")
            }
        }

        editStep_delete_button.setOnClickListener {

            if (recipeDetailss != null) {
                recipeDetailss.stepData.remove(stepDataString)
                var intent = Intent(this, updateRecipe::class.java)
                intent.putExtra("recipeDetails", recipeDetailss)
                finish()
                startActivity(intent)

            }


        }

        editStep_edit_button.setOnClickListener {

            if (recipeDetailss != null) {
                var postition = recipeDetailss.stepData.indexOf(stepDataString)
                recipeDetailss.stepData.set(postition,"${editStep_editStep.text}")
                var intent = Intent(this, updateRecipe::class.java)
                intent.putExtra("recipeDetails", recipeDetailss)
                finish()
                startActivity(intent)
            }

        }


    }
}