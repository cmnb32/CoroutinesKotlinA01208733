package com.sk32.threadswithkotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_coroutine.*
import kotlinx.android.synthetic.main.activity_real_main.*

class RealMain : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_real_main)

        val button1 = findViewById<Button>(R.id.button_first)

        //The click listener for starting th process of calculating Pi
        button1.setOnClickListener {

            //Really simple validation, a number of iterations is required in order to perform the Monte Carlo Method
            if(IterationsThreads.text.toString().isEmpty()){
                //A simple feedback if the number of desired iteratios is empty
                Toast.makeText(applicationContext, "Please ingress a numerical value", Toast.LENGTH_SHORT).show()
                IterationsThreads.requestFocus()
            }else{
                //If the field where the user defines the number of iterations is not empty the process starts
                startThread1()
            }


        }
        //Just for navigating between Threads and Coroutine method
        button_second.setOnClickListener {
            startActivity(Intent(this, Coroutine::class.java))
        }

    }

    //Function that handles the creation of the threads
    fun startThread1(){
//        var textTest1 = findViewById<TextView>(R.id.textview_first)
//        var textTest2 = findViewById<TextView>(R.id.textview_second)

        //A variable that helps us to measure the time taken by the phone to finish the task
        val startTime = System.currentTimeMillis()

        //We are getting the vale for the number of iterations
        val numIterations = IterationsThreads.text.toString().toInt()

        //This time we will try the process with 4 threads
        //We use the class that we have created (created afeter this function in this case)
        //in order to create our threads
        val t1 = Thread1(numIterations/4,this) //I put in the constructor a variable of tyoe activity for test purposes, it is not mandatory to pass it.
        val t2 = Thread1(numIterations/4, this)//You will understand a few lines further
        val t3 = Thread1(numIterations/4, this)
        val t4 = Thread1(numIterations/4, this)

        //Now we are really creating threads
        val threadExample = Thread(t1)
        val threadExample2 = Thread(t2)
        val threadExample3 = Thread(t3)
        val threadExample4 = Thread(t4)

        //Now we start each thread.
        threadExample.start()
        threadExample2.start()
        threadExample3.start()
        threadExample4.start()

        //As in Java, it is important to join the threads
        threadExample.join()
        threadExample2.join()
        threadExample3.join()
        threadExample4.join()

        //Each thread has a value called inside, there is the final value for the total points inside the circle.
        //We sum the total number of inside points of each thread and apply the formula for calculating pi
        var res3 = 4.0 * (t1.inside + t2.inside + t3.inside + t4.inside) / (numIterations)

        //For printing in the Logcat section in Android Studio
//        Log.d("RealMain", res3.toString())

        //Print in the UI the result of Pi
        textview_first.setText("Pi: ${res3.toString()}")

        //We get the final time for calculating the taken time for the task
        val endTime = System.currentTimeMillis()
//        println("Time taken: ${endTime - startTime}")

        //print in the UI
        textview_timeT.setText("Time taken: ${endTime - startTime}")
    }

    //Here is the class for the creation of the threads. Each thread will perform the code inside the class
    class Thread1(var iterations: Int, val activity: Activity): Runnable {//As I mentioned before, the Activity parameter was for experimental puroposes and you can avoid this parameter
        //Creation of a variable aviable in each thread that will count the total amount of points inside the circle
        var inside: Int=0

        override fun run() {
            for (i in 1 until iterations){//The iteration is done until the value given by the user
                val x = Math.random()//We use this method for generating random numbers in x and in y for their representation in a 2d graph.
                val y = Math.random()
                if (x * x + y * y <= 1.0){//Checks if the dot is inside the circle, if it is it adds 1 to the counter
                    inside++
                }
            }
//            Log.d("RealMain","Pi: " + inside)

            //This is the reason for the Activity variable in the constructor. Since this is another thread different
            // from the thread that handles the User Interface you need to run the Ui THread in order to update User Interface values and data.
//            activity.runOnUiThread(Runnable {
////                activity.textview_first.text = inside.toString()
//
//            })
        }
    }

}
