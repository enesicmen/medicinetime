package com.icmen.medicinetime.ui.fragment.profile

import android.os.Bundle
import com.icmen.medicinetime.databinding.FragmentProfileBinding
import com.icmen.medicinetime.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfilePageFragment : BaseFragment<FragmentProfileBinding, ProfilePageViewModel>() {

    override fun initView(savedInstanceState: Bundle?) {}

    override fun setViewModelClass() = ProfilePageViewModel::class.java

    override fun setViewBinding(): FragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater)
}
