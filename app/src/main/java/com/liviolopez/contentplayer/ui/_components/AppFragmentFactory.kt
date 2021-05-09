package com.liviolopez.contentplayer.ui._components

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.liviolopez.contentplayer.ui.home.HomeFragment
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
class AppFragmentFactory @Inject constructor()
: FragmentFactory(){
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            HomeFragment::class.java.name -> HomeFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}