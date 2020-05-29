package com.sk32.threadswithkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_coroutine.*
import kotlinx.android.synthetic.main.activity_real_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlin.system.exitProcess

class Coroutine : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine)

        button_co.setOnClickListener {
            //Really simple validation, a number of iterations is required in order to perform the Monte Carlo Method
            if(Iterations.text.toString().isEmpty()){
                //A simple feedback if the number of desired iteratios is empty
                Toast.makeText(applicationContext, "Please ingress a numerical value", Toast.LENGTH_SHORT).show()
                Iterations.requestFocus()
            }else{
                //If the field where the user defines the number of iterations is not empty the process start
                myCoroutines() //BTW, the most creative name ever
            }


        }

        //For navigation
        button_co2.setOnClickListener {
            startActivity(Intent(this, RealMain::class.java))
        }

    }

    //Notice that here the function has at the begining the reservated word suspend.
    //It is important for the coroutines, we recieve as a parameter to the cosntructor a variable for the total amount of iterations
    suspend fun distance(iterations: Int): Int {
        //This is something kind of similar to the typical Thread.sleep(). However it is a different.
        delay(1)
        //Variable for the total points inside the circle
        var inside = 0

        //The same code as the thread part. Calculation of points inside the circle
        for (i in 1 until iterations){
            val x = Math.random()
            val y = Math.random()
            if (x * x + y * y <= 1.0){
                inside++
            }
        }
        return inside
    }

    //This time I used the runBlocking method for the creation of the coroutines.
    //This method allows me to syncronize each coroutine almost automatically.
    fun myCoroutines() = runBlocking {
        //Here we take the value for the number of iterations
        var numIterationsCo = Iterations.text.toString().toInt()

        //For the meassure of the time taken
        val startTime = System.currentTimeMillis()

        //Here we define the diferent distances for each coroutine using dispatchers with their default behavior. This time I used 4
        val result1 = withContext(Dispatchers.Default) { distance(numIterationsCo/4) }
        val result2 = withContext(Dispatchers.Default) { distance(numIterationsCo/4) }
        val result3 = withContext(Dispatchers.Default) { distance(numIterationsCo/4) }
        val result4 = withContext(Dispatchers.Default) { distance(numIterationsCo/4) }
        //However you can uncomment the lines above this comment and test it with more coroutines. Do no t forget to change the value of 4 and 9
//        val result5 = withContext(Dispatchers.Default) { distance(numIterationsCo/9) }
//        val result6 = withContext(Dispatchers.Default) { distance(numIterationsCo/9) }
//        val result7 = withContext(Dispatchers.Default) { distance(numIterationsCo/9) }
//        val result8 = withContext(Dispatchers.Default) { distance(numIterationsCo/9) }
//        val result9 = withContext(Dispatchers.Default) { distance(numIterationsCo/9) }


//      //Here is where the final calculation happens
        val res = (4.0 * (result1 + result2 + result3 + result4 /*+ result5 + result6 + result7 + result8 + result9*/)) / (numIterationsCo)

        //Since we are only interested on the taken time for the mathematical process wi end the meassure of time here.
        val endTime = System.currentTimeMillis()

        //Since we are running the coroutines on another thread different from the User Interface one we need to run the UI THread
        //in order to displey the results on the screen
        runOnUiThread(Runnable {
            textview_co.setText("Pi: $res")
            textview_timeCo.setText("Time taken: ${endTime - startTime}")

        })
    }
}
