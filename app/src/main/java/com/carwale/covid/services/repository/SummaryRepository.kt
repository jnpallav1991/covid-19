package com.carwale.covid.services.repository

import com.carwale.covid.AppApplication
import com.carwale.covid.model.Summary
import io.reactivex.Single

class SummaryRepository {

    fun getCovidSummary(): Single<Summary> {
        return AppApplication.getApiClient().getRestInterface().getSummary()
    }

}