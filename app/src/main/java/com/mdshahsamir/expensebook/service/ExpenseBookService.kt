package com.mdshahsamir.expensebook.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Process
import android.util.Log
import android.widget.Toast

class ExpenseBookService: Service() {

    val TAG = "ExpenseBookService:::";
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        var expiretime = 0L

        override fun handleMessage(msg: Message) {
            try {
                Thread.sleep(expiretime)
                Log.i(TAG, msg.arg1.toString())
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }

            stopSelf()
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate Called")

        HandlerThread("ServiceStartArgument", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand Called")

        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        serviceHandler?.expiretime = intent?.getIntExtra("a",0)!!.toLong()
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        Log.i(TAG, "onBing Called")
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy Called")
    }
}