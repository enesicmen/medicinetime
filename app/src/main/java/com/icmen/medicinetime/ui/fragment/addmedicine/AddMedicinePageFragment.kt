package com.icmen.medicinetime.ui.fragment.addmedicine

import android.os.Bundle
import com.icmen.medicinetime.databinding.FragmentAddMedicineBinding
import com.icmen.medicinetime.ui.base.BaseFragment

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMedicinePageFragment : BaseFragment<FragmentAddMedicineBinding, AddMedicinePageViewModel>() {

    override fun setViewBinding(): FragmentAddMedicineBinding =
        FragmentAddMedicineBinding.inflate(layoutInflater)

    override fun setViewModelClass() = AddMedicinePageViewModel::class.java

    override fun initView(savedInstanceState: Bundle?) {}


}
