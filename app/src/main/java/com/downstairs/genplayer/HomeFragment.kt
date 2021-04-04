package com.downstairs.genplayer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment(R.layout.home_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toVideoFragmentButton.setOnClickListener {
            findNavController().navigate(R.id.fromHomeScreenToVideoScreen)
        }
    }
}