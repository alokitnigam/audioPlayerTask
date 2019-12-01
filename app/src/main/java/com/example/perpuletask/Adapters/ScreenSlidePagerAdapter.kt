package com.example.perpuletask.Adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.perpuletask.Fragments.AudioListFragment
import com.example.perpuletask.Fragments.AudioPlayerFragment

public class ScreenSlidePagerAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return AudioListFragment.newInstance()
            }
            else -> {
                return AudioPlayerFragment.newInstance()
            }


        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 ->{
                return "Audios"

            }else ->{
                return "Player"
            }
        }
    }
}