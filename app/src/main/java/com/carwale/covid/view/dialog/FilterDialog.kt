package com.carwale.covid.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.pjtech.covid.R
import com.carwale.covid.model.Filter
import com.carwale.covid.view.fragment.CovidMainScreenFragment
import kotlinx.android.synthetic.main.filter_dialog.*
import kotlinx.android.synthetic.main.filter_dialog.view.*

class FilterDialog(context: Context, check: Boolean) : AlertDialog(context) {

    private val TAG = FilterDialog::class.java.simpleName

    private var buttonClick: ButtonClick? = null

    /**
     * Creating and returns single instance
     *
     * @return
     */
    companion object {

        fun newInstance(
            context: Context
        ): FilterDialog {
            val alertDialog = FilterDialog(context)
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            return alertDialog
        }
    }

    // ALLOWS YOU TO SET LISTENER && INVOKE THE OVERIDING METHOD
    // FROM WITHIN ACTIVITY
    fun setOnAlertClickListener(buttonClick: ButtonClick) {
        this.buttonClick = buttonClick
    }

    interface ButtonClick {
        fun onClearFilter()

        fun onApplyFilter()
    }

    constructor(context: Context) : this(context, true) {
        //setContentView(R.layout.custom_alert_dialog);
        // Get the layout inflater
        val inflater = layoutInflater
        // Inflate and set the layout for the dialog
        val view = inflater.inflate(R.layout.filter_dialog, null)
        setView(view)
        setCancelable(false)

        val filter = CovidMainScreenFragment.mainFilter
        if (filter.isTotalConfirmGreater!=null)
        {
            if (filter.isTotalConfirmGreater!!)
            {
                view.rdGreater.isChecked = true
            }
            else
            {
                view.rdLower.isChecked = true
            }
            view.edTotalCase.setText(filter.TotalConfirmed.toString())
        }
        if (filter.isDeathGreater!=null)
        {
            if (filter.isDeathGreater!!)
            {
                view.rdDeathGreater.isChecked = true
            }
            else
            {
                view.rdDeathLower.isChecked = true
            }
            view.edDeaths.setText(filter.Deaths.toString())
        }
        if (filter.isRecoveredGreater!=null)
        {
            if (filter.isRecoveredGreater!!)
            {
                view.rdRecoveredGreater.isChecked = true
            }
            else
            {
                view.rdRecoveredLower.isChecked = true
            }
            view.edRecovered.setText(filter.Recovered.toString())
        }
        try {
            view.btnClearFilter!!.setOnClickListener {

                view.grpTotalCase.clearCheck()
                view.grpDeath.clearCheck()
                view.grpRecovered.clearCheck()
                view.edTotalCase.setText("")
                view.edDeaths.setText("")
                view.edRecovered.setText("")
                CovidMainScreenFragment.mainFilter  = Filter()
                buttonClick!!.onClearFilter()
            }

            view.btnFilter!!.setOnClickListener {

                if (checkValidation()) {
                    buttonClick!!.onApplyFilter()
                }
            }

            view.imageClose!!.setOnClickListener {
                dismiss()
                //buttonClick!!.onNegativeButtonClick()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        show()
    }

    private fun checkValidation() :Boolean
    {
        val idTotal: Int = grpTotalCase.checkedRadioButtonId
        val totalCase = edTotalCase.text.toString().trim()
        // val filter = Filter()
        val filter = CovidMainScreenFragment.mainFilter
        if (idTotal!=-1){

            if (totalCase!="") {
                val radio: RadioButton = this.findViewById(idTotal)!!
                filter.isTotalConfirmGreater = radio.text==context.getString(R.string.greaterOrEqual)
                filter.TotalConfirmed = totalCase.toInt()
            }
        }
        else
        {
            if (totalCase!="")
            {
                Toast.makeText(context,"Please select total case filter option" ,
                    Toast.LENGTH_SHORT).show()
                return false
            }
        }
        val idDeath: Int = grpDeath.checkedRadioButtonId
        val totalDeath =edDeaths.text.toString()
        if (idDeath!=-1){
            if (totalDeath!="") {
                val radio: RadioButton = this.findViewById(idDeath)!!
                filter.isDeathGreater = radio.text==context.getString(R.string.greaterOrEqual)
                filter.Deaths = totalDeath.toInt()
            }
        }
        else
        {
            if (totalDeath!="")
            {
                Toast.makeText(context,"Please select death filter option" ,
                    Toast.LENGTH_SHORT).show()
                return false
            }
        }
        val idRecovered: Int = grpRecovered.checkedRadioButtonId
        val totalRecovered = edRecovered.text.toString().trim()
        if (idRecovered!=-1){
            if (totalRecovered!="") {
                val radio: RadioButton = this.findViewById(idRecovered)!!
                filter.isRecoveredGreater = radio.text==context.getString(R.string.greaterOrEqual)
                filter.Recovered = totalRecovered.toInt()
            }
        }
        else
        {
            if (totalRecovered!="")
            {
                Toast.makeText(context,"Please select recovered filter option" ,
                    Toast.LENGTH_SHORT).show()
                return false
            }
        }

        return true
    }

}