package com.carwale.covid.view.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pjtech.covid.R
import com.carwale.covid.model.Summary
import com.carwale.covid.utils.SharedPreferenceUtil
import com.carwale.covid.view.fragment.CovidMainScreenFragment
import kotlinx.android.synthetic.main.recyclerview_country_list.view.*

class CountryListAdapter(
    private val arrayList: ArrayList<Summary.Country>,
    private val context: Context
    // private val sendQuantityType: SendSelectedType
) :
    RecyclerView.Adapter<CountryListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.recyclerview_country_list, viewGroup,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position], position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(result: Summary.Country, position: Int) {

            if (position == 0) {
                val code = SharedPreferenceUtil(context).getString("countryCode", "")
                if (code != null && code != "") {
                    itemView.txtCountry.typeface = Typeface.create(
                        "sans-serif-light",
                        Typeface.BOLD
                    )
                    itemView.txtTotalCases.typeface = Typeface.create(
                        "sans-serif-light",
                        Typeface.BOLD
                    )
                    itemView.txtDeaths.typeface = Typeface.create(
                        "sans-serif-light",
                        Typeface.BOLD
                    )
                    itemView.txtRecovered.typeface = Typeface.create(
                        "sans-serif-light",
                        Typeface.BOLD
                    )
                } else {
                    itemView.txtCountry.typeface = Typeface.create(
                        "sans-serif-light",
                        Typeface.NORMAL
                    )
                    itemView.txtTotalCases.typeface = Typeface.create(
                        "sans-serif-light",
                        Typeface.NORMAL
                    )
                    itemView.txtDeaths.typeface = Typeface.create(
                        "sans-serif-light",
                        Typeface.NORMAL
                    )
                    itemView.txtRecovered.typeface = Typeface.create(
                        "sans-serif-light",
                        Typeface.NORMAL
                    )
                }
            } else {
                itemView.txtCountry.typeface = Typeface.create(
                    "sans-serif-light",
                    Typeface.NORMAL
                )
                itemView.txtTotalCases.typeface = Typeface.create(
                    "sans-serif-light",
                    Typeface.NORMAL
                )
                itemView.txtDeaths.typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
                itemView.txtRecovered.typeface = Typeface.create(
                    "sans-serif-light",
                    Typeface.NORMAL
                )

            }
            itemView.txtCountry.text = result.Country
            itemView.txtTotalCases.text = result.formatDecimal(result.TotalConfirmed)
            itemView.txtDeaths.text = result.formatDecimal(result.TotalDeaths)
            itemView.txtRecovered.text = result.formatDecimal(result.TotalRecovered)


        }
    }

    fun addData(
        list: ArrayList<Summary.Country>,
        countryCode: String?
    ) {
        arrayList.clear()
        arrayList.addAll(list)
        sortOrder()
        if (countryCode != null) {
            for (position in list.indices) {
                if (list[position].CountryCode == countryCode) {
                    arrayList.removeAt(position)
                    arrayList.add(0, list[position])
                }
            }
        }
        notifyDataSetChanged()
        // arrayList.addAll(list)

    }

    fun sortData() {
        val country = arrayList.removeAt(0)
        applyFilter()
        arrayList.add(0, country)
        notifyDataSetChanged()
    }

    private fun sortOrder() {
        when (CovidMainScreenFragment.sortList) {
            0 -> {
                if (CovidMainScreenFragment.isCountryDescending) {

                    arrayList.sortWith(Comparator { o1, o2 -> o1.Country!!.compareTo(o2.Country!!) })

                } else {
                    arrayList.sortWith(Comparator { o1, o2 -> o2.Country!!.compareTo(o1.Country!!) })
                }

            }
            1 -> {
                if (CovidMainScreenFragment.isTotalCasesDescending) {
                    arrayList.sortWith(Comparator { o1, o2 -> o2.TotalConfirmed!!.compareTo(o1.TotalConfirmed!!) })
                } else {

                    arrayList.sortWith(Comparator { o1, o2 -> o1.TotalConfirmed!!.compareTo(o2.TotalConfirmed!!) })
                }

            }
            2 -> {
                if (CovidMainScreenFragment.isDeathDescending) {
                    arrayList.sortWith(Comparator { o1, o2 -> o2.TotalDeaths!!.compareTo(o1.TotalDeaths!!) })
                } else {

                    arrayList.sortWith(Comparator { o1, o2 -> o1.TotalDeaths!!.compareTo(o2.TotalDeaths!!) })
                }
            }
            3 -> {
                if (CovidMainScreenFragment.isRecoveredDescending) {
                    arrayList.sortWith(Comparator { o1, o2 -> o2.TotalRecovered!!.compareTo(o1.TotalRecovered!!) })
                } else {
                    arrayList.sortWith(Comparator { o1, o2 -> o1.TotalRecovered!!.compareTo(o2.TotalRecovered!!) })
                }
            }
        }
    }

    private fun applyFilter() {
        val filter = CovidMainScreenFragment.mainFilter
        if (filter.isTotalConfirmGreater == null && filter.isDeathGreater == null && filter.isRecoveredGreater == null) {
            val code = SharedPreferenceUtil(context).getString("countryCode", "")
            addData(CovidMainScreenFragment.summaryList,code)
        }
        if (filter.isTotalConfirmGreater != null && filter.isDeathGreater == null && filter.isRecoveredGreater == null) {
            arrayList.clear()
            if (filter.isTotalConfirmGreater!!) {
                for (list in CovidMainScreenFragment.summaryList) {
                    if (list.TotalConfirmed!! >= filter.TotalConfirmed!!) {
                        arrayList.add(list)
                    }
                }
            } else {
                for (list in CovidMainScreenFragment.summaryList) {
                    if (list.TotalConfirmed!! <= filter.TotalConfirmed!!) {
                        arrayList.add(list)
                    }
                }
            }

        }
        if (filter.isDeathGreater != null && filter.isTotalConfirmGreater == null  && filter.isRecoveredGreater == null) {
            arrayList.clear()
            if (filter.isDeathGreater!!) {
                for (list in CovidMainScreenFragment.summaryList) {
                    if (list.TotalDeaths!! >= filter.Deaths!!) {
                        arrayList.add(list)
                    }
                }
            } else {
                for (list in CovidMainScreenFragment.summaryList) {
                    if (list.TotalDeaths!! <= filter.Deaths!!) {
                        arrayList.add(list)
                    }
                }
            }

        }

        if (filter.isRecoveredGreater != null && filter.isTotalConfirmGreater == null  && filter.isDeathGreater == null) {
            arrayList.clear()
            if (filter.isRecoveredGreater!!) {
                for (list in CovidMainScreenFragment.summaryList) {
                    if (list.TotalRecovered!! >= filter.Recovered!!) {
                        arrayList.add(list)
                    }
                }
            } else {
                for (list in CovidMainScreenFragment.summaryList) {
                    if (list.TotalRecovered!! <= filter.Recovered!!) {
                        arrayList.add(list)
                    }
                }
            }

        }

        if (filter.isRecoveredGreater != null && filter.isTotalConfirmGreater != null  && filter.isDeathGreater == null) {
            arrayList.clear()
            if (filter.isRecoveredGreater!! && filter.isTotalConfirmGreater!!) {
                for (list in CovidMainScreenFragment.summaryList) {
                    if (list.TotalRecovered!! >= filter.Recovered!! && list.TotalConfirmed!! >= filter.TotalConfirmed!!) {
                        arrayList.add(list)
                    }
                }
            } else {
                for (list in CovidMainScreenFragment.summaryList) {
                    if (list.TotalRecovered!! <= filter.Recovered!! && list.TotalConfirmed!! <= filter.TotalConfirmed!!) {
                        arrayList.add(list)
                    }
                }
            }

        }
        if (filter.isRecoveredGreater == null && filter.isTotalConfirmGreater != null  && filter.isDeathGreater != null) {
            arrayList.clear()
            if (filter.isDeathGreater!! && filter.isTotalConfirmGreater!!) {
                for (list in CovidMainScreenFragment.summaryList) {
                    if (list.TotalConfirmed!! >= filter.TotalConfirmed!! && list.TotalDeaths!! >= filter.Deaths!!) {
                        arrayList.add(list)
                    }
                }
            } else {
                for (list in CovidMainScreenFragment.summaryList) {
                    if (list.TotalConfirmed!! <= filter.TotalConfirmed!! && list.TotalDeaths!! <= filter.Deaths!!) {
                        arrayList.add(list)
                    }
                }
            }

        }

        if (filter.isRecoveredGreater != null && filter.isTotalConfirmGreater == null  && filter.isDeathGreater != null) {
            arrayList.clear()
            if (filter.isDeathGreater!! && filter.isRecoveredGreater!!) {
                for (list in CovidMainScreenFragment.summaryList) {
                    if (list.TotalRecovered!! >= filter.Recovered!! && list.TotalDeaths!! >= filter.Deaths!!) {
                        arrayList.add(list)
                    }
                }
            } else {
                for (list in CovidMainScreenFragment.summaryList) {
                    if (list.TotalRecovered!! <= filter.Recovered!! && list.TotalDeaths!! <= filter.Deaths!!) {
                        arrayList.add(list)
                    }
                }
            }

        }

        if (filter.isRecoveredGreater != null && filter.isTotalConfirmGreater != null  && filter.isDeathGreater != null) {
            arrayList.clear()
            if (filter.isDeathGreater!! && filter.isRecoveredGreater!! && filter.isTotalConfirmGreater!!) {
                for (list in CovidMainScreenFragment.summaryList) {
                    if (list.TotalRecovered!! >= filter.Recovered!! && list.TotalDeaths!! >= filter.Deaths!! && list.TotalConfirmed!! >= filter.TotalConfirmed!!) {
                        arrayList.add(list)
                    }
                }
            } else {
                for (list in CovidMainScreenFragment.summaryList) {
                    if (list.TotalRecovered!! <= filter.Recovered!! && list.TotalDeaths!! <= filter.Deaths!! && list.TotalConfirmed!! <= filter.TotalConfirmed!!) {
                        arrayList.add(list)
                    }
                }
            }

        }
        sortOrder()

    }

    interface SendSelectedType {
        //fun send(bleDeviceData: Users, position: Int)
    }
}