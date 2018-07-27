package com.example.caramelool.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import androidx.work.State
import androidx.work.WorkStatus
import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("LogNotTimber")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WorkHandler.workList.forEach {
            WorkHandler.observeWork(lifecycle, it.value, ::observerWork)
        }
    }

    private fun observerWork(status: WorkStatus?) {
        status?.run {
            Log.d("observeWork", "${status.tags} & ${state.name}")
            when (state) {
                State.SUCCEEDED -> {
                    val message = "${textView.text}\nSUCCEEDED - ${outputData.getString("work")}"
                    textView.text = message
                }
//            State.ENQUEUED -> TODO()
//            State.RUNNING -> TODO()
//            State.FAILED -> TODO()
//            State.BLOCKED -> TODO()
//            State.CANCELLED -> TODO()
                else -> {

                }
            }
        }
    }
}