package com.carwale.covid.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjtech.covid.R
import com.carwale.covid.model.Filter
import com.carwale.covid.model.Summary
import com.carwale.covid.utils.SharedPreferenceUtil
import com.carwale.covid.utils.Status
import com.carwale.covid.view.adapter.CountryListAdapter
import com.carwale.covid.view.dialog.FilterDialog
import com.carwale.covid.viewmodel.SummaryViewModel
import com.carwale.covid.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_covid_main_screen.*

class CovidMainScreenFragment : Fragment() {

    private lateinit var adapter: CountryListAdapter
    private lateinit var summaryViewModel: SummaryViewModel
    private var isResponseSuccess:Boolean=false


    companion object {
        var summaryList: ArrayList<Summary.Country> = ArrayList()
        var sortList: Int? = null
        var isCountryDescending: Boolean = false
        var isTotalCasesDescending: Boolean = false
        var isDeathDescending: Boolean = false
        var isRecoveredDescending: Boolean = false
        var mainFilter: Filter = Filter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_covid_main_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()

        txtCCCountry.setOnClickListener {
            sortList = 0
            showDrawable()
            adapter.sortData()
            changeOrder()
        }
        txtCTotalCases.setOnClickListener {
            sortList = 1
            showDrawable()
            adapter.sortData()
            changeOrder()
        }
        txtCDeaths.setOnClickListener {
            sortList = 2
            showDrawable()
            adapter.sortData()
            changeOrder()
        }
        txtCRecovered.setOnClickListener {
            sortList = 3
            showDrawable()
            adapter.sortData()
            changeOrder()
        }

        btnFilter.setOnClickListener {

            if (isResponseSuccess) {
                val filterDialog = FilterDialog.newInstance(context!!)
                filterDialog.setOnAlertClickListener(object : FilterDialog.ButtonClick {
                    override fun onClearFilter() {

                    }

                    override fun onApplyFilter() {

                        applyFilter()
                        filterDialog.cancel()
                    }
                })
            }
            else
            {
                Toast.makeText(context,"429 Too Many Requests",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun applyFilter() {
        adapter.sortData()
    }

    private fun setupObserver() {

       // if (MainActivity.hasPermissions(context!!)) {
            summaryViewModel.getSummary().observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        it.data?.let { users -> renderList(users) }
                        isResponseSuccess = true
                        recyclerViewCountry.visibility = View.VISIBLE
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        //recyclerViewCountry.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        //Handle Error
                        progressBar.visibility = View.GONE
                        isResponseSuccess = false
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
       // }
    }

    private fun renderList(summary: Summary?) {

        if (summary != null) {
            if (summary.Countries != null) {
                sortList = 1
                isTotalCasesDescending = true
                showDrawable()
                summaryList.clear()
                summaryList.addAll(summary.Countries)
                val code = SharedPreferenceUtil(context!!).getString("countryCode", "")
                adapter.addData(summary.Countries, code)


            }
            if (summary.Global != null) {
                totalCases.text = summary.formatDecimal(summary.Global.TotalConfirmed)
                deaths.text = summary.formatDecimal(summary.Global.TotalDeaths)
                recovered.text = summary.formatDecimal(summary.Global.TotalRecovered)
            }
        }

    }

    private fun setupUI() {

        recyclerViewCountry.layoutManager = LinearLayoutManager(context)
        adapter = CountryListAdapter(arrayListOf(), context!!)
        recyclerViewCountry.addItemDecoration(
            DividerItemDecoration(
                recyclerViewCountry.context,
                (recyclerViewCountry.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerViewCountry.adapter = adapter
    }

    private fun setupViewModel() {
        summaryViewModel = ViewModelProviders.of(
            this, ViewModelFactory()
        ).get(SummaryViewModel::class.java)
    }

    private fun showDrawable() {
        when (sortList) {
            0 -> {
                if (isCountryDescending) {
                    txtCCCountry.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        ContextCompat.getDrawable(context!!, R.drawable.ic_arrow_upward),
                        null
                    )
                } else {
                    txtCCCountry.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        ContextCompat.getDrawable(context!!, R.drawable.ic_arrow_downward),
                        null
                    )
                }
                isTotalCasesDescending = true
                isDeathDescending = true
                isRecoveredDescending = true
                txtCTotalCases.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )
                txtCDeaths.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )
                txtCRecovered.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )
            }
            1 -> {
                if (isTotalCasesDescending) {
                    txtCTotalCases.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        ContextCompat.getDrawable(context!!, R.drawable.ic_arrow_upward),
                        null
                    )
                } else {
                    txtCTotalCases.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        ContextCompat.getDrawable(context!!, R.drawable.ic_arrow_downward),
                        null
                    )

                }
                isCountryDescending = true
                isDeathDescending = true
                isRecoveredDescending = true

                txtCCCountry.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )
                txtCDeaths.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )
                txtCRecovered.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )

            }
            2 -> {
                if (isDeathDescending) {

                    txtCDeaths.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        ContextCompat.getDrawable(context!!, R.drawable.ic_arrow_upward),
                        null
                    )
                } else {

                    txtCDeaths.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        ContextCompat.getDrawable(context!!, R.drawable.ic_arrow_downward),
                        null
                    )
                }
                isCountryDescending = true
                isTotalCasesDescending = true
                isRecoveredDescending = true

                txtCCCountry.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )
                txtCTotalCases.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )
                txtCRecovered.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )
            }
            3 -> {
                if (isRecoveredDescending) {
                    txtCRecovered.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        ContextCompat.getDrawable(context!!, R.drawable.ic_arrow_upward),
                        null
                    )
                } else {
                    txtCRecovered.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        ContextCompat.getDrawable(context!!, R.drawable.ic_arrow_downward),
                        null
                    )
                }
                isCountryDescending = true
                isTotalCasesDescending = true
                isDeathDescending = true
                txtCCCountry.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )
                txtCDeaths.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )
                txtCTotalCases.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )
            }
        }
    }

    private fun changeOrder() {
        when (sortList) {
            0 -> {
                isCountryDescending =
                    !isCountryDescending

            }
            1 -> {
                isTotalCasesDescending =
                    !isTotalCasesDescending

            }
            2 -> {
                isDeathDescending =
                    !isDeathDescending
            }
            3 -> {
                isRecoveredDescending =
                    !isRecoveredDescending
            }
        }
    }

    fun fetchSummary()
    {
        summaryViewModel.fetchSummaryAfterLocation()
    }

}
