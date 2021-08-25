package com.carwale.covid.utils

import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class SummaryTimer(private val fetchSummaryTimerListener: FetchSummaryTimerListener) :
    Timer("schedule", true) {

    companion object {
        @Volatile
        private var INSTANCE: SummaryTimer? = null

        fun getInstance(fetchSummaryTimerListener: FetchSummaryTimerListener): SummaryTimer {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = SummaryTimer(fetchSummaryTimerListener)
                INSTANCE = instance
                return instance
            }
        }
    }

    fun startTimer() {
        try {
            if (INSTANCE != null) {
                INSTANCE!!.scheduleAtFixedRate(1000, 120000)
                {
                    fetchSummaryTimerListener.fetchSummaryListener()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancelTimer() {
        if (INSTANCE != null) {
            INSTANCE!!.cancel()
            INSTANCE = null
        }
    }

    interface FetchSummaryTimerListener {
        fun fetchSummaryListener()
    }

}