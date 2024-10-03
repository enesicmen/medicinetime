package com.icmen.medicinetime.ui.fragment.addmedicine

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.icmen.medicinetime.R
import com.icmen.medicinetime.data.Resource
import com.icmen.medicinetime.databinding.FragmentAddMedicineBinding
import com.icmen.medicinetime.ui.base.BaseFragment
import com.icmen.medicinetime.ui.fragment.custom.CustomDialogWithOneButtonFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddMedicinePageFragment : BaseFragment<FragmentAddMedicineBinding, AddMedicinePageViewModel>() {


    override fun setViewBinding(): FragmentAddMedicineBinding =
        FragmentAddMedicineBinding.inflate(layoutInflater)

    override fun setViewModelClass() = AddMedicinePageViewModel::class.java

    override fun initView(savedInstanceState: Bundle?) {
        setupStartDatePicker()
        setupEndDatePicker()
        setupTimePicker()
        setupSaveButton()
        observeViewModel()
        onBackPressedDispatcher()
    }

    private fun setupStartDatePicker() {
        getViewBinding()?.etStartDate?.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    getViewBinding()?.etStartDate?.setText(date)
                },
                year, month, day)
            datePickerDialog.show()
        }
    }

    private fun setupEndDatePicker() {
        getViewBinding()?.etEndDate?.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    getViewBinding()?.etEndDate?.setText(date)
                },
                year, month, day)
            datePickerDialog.show()
        }
    }

    private fun setupTimePicker() {
        getViewBinding()?.etMedicineTime?.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(requireContext(),
                { _, selectedHour, selectedMinute ->
                    val time = String.format("%02d:%02d", selectedHour, selectedMinute)
                    getViewBinding()?.etMedicineTime?.setText(time)
                },
                hour, minute, true)
            timePickerDialog.show()
        }
    }

    private fun setupSaveButton() {
        getViewBinding()?.btnSaveMedicine?.setOnClickListener {
            val medicineName = getViewBinding()?.etMedicineName?.text.toString()
            val dosage = getViewBinding()?.etDosage?.text.toString()
            val frequency = getViewBinding()?.etFrequency?.text.toString()
            val startDate = getViewBinding()?.etStartDate?.text.toString()
            val endDate = getViewBinding()?.etEndDate?.text.toString()
            val medicineTime = getViewBinding()?.etMedicineTime?.text.toString()
            val notes = getViewBinding()?.etNotes?.text.toString()

            getViewModel()?.saveMedicineData(
                medicineName, dosage, frequency, startDate, medicineTime, notes, endDate
            )
        }
    }

    private fun observeViewModel() {
        getViewModel()?.saveMedicineResult?.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    getViewBinding()?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    getViewBinding()?.progressBar?.visibility = View.GONE
                    navigateToSuccessPage()
                }
                is Resource.Error -> {
                    getViewBinding()?.progressBar?.visibility = View.GONE
                    when (result.error) {
                        "0" -> {
                            val title = getString(R.string.error)
                            val message = getString(R.string.fill_in_all_fields)
                            setOneButtonDialog(title,message)
                        }
                        "1" -> {
                            val title = getString(R.string.error)
                            val message = getString(R.string.invalid_date_format)
                            setOneButtonDialog(title,message)
                        }
                        "2" -> {
                            val title = getString(R.string.error)
                            val message = getString(R.string.date_error)
                            setOneButtonDialog(title,message)
                        }
                        "3" -> {
                            val title = getString(R.string.error)
                            val message = getString(R.string.user_session_not_found)
                            setOneButtonDialog(title,message)
                        }
                        "4" -> {
                            val title = getString(R.string.error)
                            val message = getString(R.string.medicine_could_not_be_saved)
                            setOneButtonDialog(title,message)
                        }
                        "5" -> {
                            val title = getString(R.string.error)
                            val message = getString(R.string.date_error_sec)
                            setOneButtonDialog(title,message)
                        }
                    }
                }
            }
        })
    }

    private fun setOneButtonDialog(title: String, message: String) {
        val dialog = CustomDialogWithOneButtonFragment.newInstance(title, message)
        dialog.onOkClicked = {}
        dialog.show(requireActivity().supportFragmentManager, "customDialog")
    }

    private fun onBackPressedDispatcher(){
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateToProductsPageAndClearBackStack()
                }
            }
        )
    }

    private fun navigateToSuccessPage() {
        val title = getString(R.string.success)
        val message = getString(R.string.success_add_medicine)
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.addMedicinePageFragment, true)
            .build()
        setOneButtonDialog(title, message)
        findNavController().navigate(R.id.action_addMedicinePageFragment_to_homePageFragment, null, navOptions)
        updateBottomNavigationView()
    }

    private fun navigateToProductsPageAndClearBackStack() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.addMedicinePageFragment, true)
            .build()
        findNavController().navigate(R.id.homePageFragment, null, navOptions)
        updateBottomNavigationView()
    }

    private fun updateBottomNavigationView() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView?.selectedItemId = R.id.menu_item_home
    }
}
