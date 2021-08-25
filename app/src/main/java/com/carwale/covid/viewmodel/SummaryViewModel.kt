package com.carwale.covid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carwale.covid.model.Summary
import com.carwale.covid.services.repository.SummaryRepository
import com.carwale.covid.utils.Resource
import com.carwale.covid.utils.SummaryTimer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SummaryViewModel(private val summaryRepository: SummaryRepository) : ViewModel(),SummaryTimer.FetchSummaryTimerListener {

    private var getCovidSummary = MutableLiveData<Resource<Summary>>()
    private val compositeDisposable = CompositeDisposable()


    fun fetchSummaryAfterLocation()
    {
        SummaryTimer.getInstance(this).cancelTimer()
        SummaryTimer.getInstance(this).startTimer()
    }

    private fun fetchSummary(){
        getCovidSummary.postValue(Resource.loading(null))

            summaryRepository.getCovidSummary()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ summmary ->
                    getCovidSummary.postValue(Resource.success(summmary))
                }, { error ->
                    getCovidSummary.postValue(Resource.error(error.message!!, null))
                }
                )




    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getSummary(): LiveData<Resource<Summary>> {
        return getCovidSummary
    }

    override fun fetchSummaryListener() {
        fetchSummary()
    }
}