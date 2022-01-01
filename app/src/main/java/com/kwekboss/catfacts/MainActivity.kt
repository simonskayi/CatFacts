package com.kwekboss.catfacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.kwekboss.catfacts.api.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse


const val Base_URL = "https://cat-fact.herokuapp.com"

class MainActivity : AppCompatActivity() {

    lateinit var mainFact: TextView
    lateinit var factDate: TextView
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //referencing views
        val btnGetFact = findViewById<Button>(R.id.btn_cat_Fact)
        progressBar = findViewById(R.id.progressBar)
        mainFact = findViewById(R.id.txtFact)
        factDate = findViewById(R.id.txtDate)



        //function to start loading data
       loadTheData()


        btnGetFact.setOnClickListener {
            loadTheData()
        }
    }

    private fun loadTheData() {
        //Making the ui invisible until first internet request
        uiInvisible()

        //Handling Coroutine
        GlobalScope.launch(Dispatchers.IO) {
           
           try{
            val response = RetrofitInstance.retrofit.getCatFact().awaitResponse()

            if (response.isSuccessful) {
                val data = response.body()!!
                withContext(Dispatchers.Main) {

                   //making the UI visible
                    uiVisible()

                    mainFact.text = data.text
                    factDate.text = data.createdAt
                }
            }
        }
           catch (e:Exception)
           {
            withContext(Dispatchers.Main){
                Toast.makeText(applicationContext, "No Internet Connection.Try Again", Toast.LENGTH_SHORT).show()
            }
        }

        }
    }
    private fun uiInvisible(){
        mainFact.visibility = View.INVISIBLE
        factDate.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    private fun uiVisible(){
        mainFact.visibility = View.VISIBLE
        factDate.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
    }
}