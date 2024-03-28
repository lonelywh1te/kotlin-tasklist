package ru.lonelywh1te.kotlin_tasklist.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.lonelywh1te.kotlin_tasklist.BuildConfig
import ru.lonelywh1te.kotlin_tasklist.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInfoBinding.inflate(layoutInflater, container, false)
        binding.tvAppVersionInfo.text = BuildConfig.VERSION_NAME
        return binding.root
    }
}