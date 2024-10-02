package com.icmen.medicinetime.ui.fragment.addmedicine

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.icmen.medicinetime.data.Resource
import com.icmen.medicinetime.databinding.FragmentAddMedicineBinding
import com.icmen.medicinetime.ui.base.BaseFragment
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
                    Toast.makeText(requireContext(), "Kaydediliyor...", Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    Toast.makeText(requireContext(), "İlaç başarıyla kaydedildi.", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
