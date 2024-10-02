package com.icmen.medicinetime.ui.fragment.home

import android.os.Bundle
import com.icmen.medicinetime.databinding.FragmentHomeBinding
import com.icmen.medicinetime.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePageFragment : BaseFragment<FragmentHomeBinding, HomePageViewModel>() {

    override fun setViewModelClass() = HomePageViewModel::class.java

    override fun setViewBinding(): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {}

}
