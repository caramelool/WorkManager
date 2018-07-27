package com.example.caramelool.myapplication

import android.annotation.SuppressLint
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LiveData
import android.util.Log
import android.widget.Toast
import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkHandler {

    lateinit var workList: Map<String, OneTimeWorkRequest>

    fun setupWork() {
        workList = mapOf(
                "A" to createWork("A"),
                "B" to createWork("B", 5000),
                "C" to createWork("C"),
                "D" to createWork("D"),
                "E" to createWork("E"))

        runOnlyEnqueue()
//        runBeginWith()
//        runChain()
    }

    private fun runOnlyEnqueue() {
        WorkManager.getInstance()
                .enqueue(workList.map { it.value })
    }

    private fun runBeginWith() {
        WorkManager.getInstance()
                .beginWith(workList["A"])
                .then(workList["B"], workList["C"])
                .then(workList["D"])
                .then(workList["E"])
                .enqueue()
    }

    private fun runChain() {
        val chan1 = WorkManager.getInstance()
                .beginWith(workList["A"])
                .then(workList["E"])
        val chan2 = WorkManager.getInstance()
                .beginWith(workList["B"])
                .then(workList["C"])
        val chan3 = WorkContinuation
                .combine(chan1, chan2)
                .then(workList["D"])
        chan3.enqueue()
    }

    private fun createWork(
            letter: String,
            sleep: Long = 1000
    ): OneTimeWorkRequest {
        val constraint = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_ROAMING)
//                .setRequiresDeviceIdle(true)
//                .setRequiresCharging(true)
                .build()
        return OneTimeWorkRequest.Builder(WorkTest::class.java)
                .setConstraints(constraint)
                .setInputData(Data.Builder()
                        .putString("letter", letter)
                        .putLong("sleep", sleep)
                        .build())
                .build()
    }

    fun observeWork(lifecycle: Lifecycle, work: WorkRequest?, observer: (WorkStatus?) -> Unit) {
        if (work?.id == null) return
        WorkManager.getInstance().getStatusById(work.id)
                .observe({ lifecycle }) {
                    observer(it)
                }
    }

    fun stopWork(work: WorkRequest?) {
        val id = work?.id ?: return
        WorkManager.getInstance().cancelWorkById(id)
    }

}

@SuppressLint("LogNotTimber")
class WorkTest : Worker() {
    override fun doWork(): Result {
        val letter = inputData.getString("letter")
        val sleep = inputData.getLong("sleep", 1000)
        Thread.sleep(sleep)
        outputData = Data.Builder()
                .putString("work", letter)
                .putString("name", "Lucas Caramelo")
                .putString("labday", "WorkManager")
                .build()
//        return Result.SUCCESS
        return when (letter) {
            "B" -> Result.FAILURE
            else -> Result.SUCCESS
        }
    }
}