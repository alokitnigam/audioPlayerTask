package com.example.perpuletask

import android.os.Bundle
import com.example.perpuletask.Adapters.ScreenSlidePagerAdapter
import com.example.perpuletask.DI.VMFactory.DaggerViewModelFactory
import com.example.perpuletask.RxClasses.RxBus
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {
    lateinit var pagerAdapter: ScreenSlidePagerAdapter
    @Inject
    lateinit var viewModeFactory: DaggerViewModelFactory
    private var busListFrag: RxBus? = null
    private var busPlayer: RxBus? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        busPlayer = RxBus()
        busListFrag = RxBus()

        pagerAdapter = ScreenSlidePagerAdapter(this,supportFragmentManager,2)
        viewpager.adapter = pagerAdapter
        tablayout.setupWithViewPager(viewpager)

    }
    public fun bus(): RxBus? {
        return busListFrag
    }
    public fun busPlayer(): RxBus? {
        return busPlayer
    }

}
