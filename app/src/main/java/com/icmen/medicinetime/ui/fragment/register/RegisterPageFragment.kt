package com.icmen.medicinetime.ui.fragment.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import com.icmen.medicinetime.R
import com.icmen.medicinetime.data.Resource
import com.icmen.medicinetime.databinding.FragmentRegisterBinding
import com.icmen.medicinetime.ui.base.BaseFragment
import com.icmen.medicinetime.ui.fragment.custom.CustomDialogWithOneButtonFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class RegisterPageFragment : BaseFragment<FragmentRegisterBinding, RegisterPageViewModel>() {

    override fun setViewBinding(): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(layoutInflater)
    }

    override fun setViewModelClass() = RegisterPageViewModel::class.java

    override fun initView(savedInstanceState: Bundle?) {
        registerUser()
        goToLoginPage()
        observeViewModel()
        setDatePicker()
    }

    private fun registerUser(){
        getViewBinding()?.btnRegister?.setOnClickListener {
            val name = getViewBinding()?.etName?.text.toString()
            val surname = getViewBinding()?.etSurname?.text.toString()
            val email = getViewBinding()?.etEmail?.text.toString()
            val password = getViewBinding()?.etPassword?.text.toString()
            val birthday = getViewBinding()?.etBirthDate?.text.toString()

            getViewModel()?.registerUser(name, surname, email, password, birthday)
        }
    }
    private fun observeViewModel() {
        getViewModel()?.registrationResult?.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    getViewBinding()?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    getViewBinding()?.progressBar?.visibility = View.GONE
                    updateUI(resource.data)
                }
                is Resource.Error -> {
                    getViewBinding()?.progressBar?.visibility = View.GONE
                    when (resource.error) {
                        "0" -> {
                            val title = getString(R.string.error)
                            val message = getString(R.string.fill_in_all_fields)
                            setOneButtonDialog(title,message)
                        }
                        "1" -> {
                            val title = getString(R.string.error)
                            val message = getString(R.string.must_be_at_least_six_characters_long)
                            setOneButtonDialog(title,message)
                        }
                        "2" -> {
                            val title = getString(R.string.error)
                            val message = getString(R.string.email_already_in_use)
                            setOneButtonDialog(title,message)
                        }
                        "3" -> {
                            val title = getString(R.string.error)
                            val message = getString(R.string.registration_failed)
                            setOneButtonDialog(title,message)
                        }
                        "4" -> {
                            val title = getString(R.string.error)
                            val message = getString(R.string.unknown_error_occurred)
                            setOneButtonDialog(title,message)
                        }
                        "5" -> {
                            val title = getString(R.string.error)
                            val message = getString(R.string.invalid_email_format)
                            setOneButtonDialog(title,message)
                        }
                    }
                }
            }
        })

        getViewModel()?.progressVisibility?.observe(viewLifecycleOwner, Observer { isVisible ->
            getViewBinding()?.progressBar?.visibility = if (isVisible) View.VISIBLE else View.GONE
        })
    }

    private fun updateUI(user: FirebaseUser?) {
        val login = RegisterPageFragmentDirections.actionRegisterPageFragmentToHomePageFragment()
        findNavController().navigate(login)
    }

    private fun goToLoginPage() {
        getViewBinding()?.tvLogin?.setOnClickListener {
            val login = RegisterPageFragmentDirections.actionRegisterPageFragmentToLoginPageFragment()
            findNavController().navigate(login)
        }
    }
    private fun setOneButtonDialog(title: String, message: String) {
        val dialog = CustomDialogWithOneButtonFragment.newInstance(title, message)
        dialog.onOkClicked = {}
        dialog.show(requireActivity().supportFragmentManager, "customDialog")
    }

    private fun setDatePicker(){
        getViewBinding()?.etBirthDate?.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                getViewBinding()?.etBirthDate?.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }
}
