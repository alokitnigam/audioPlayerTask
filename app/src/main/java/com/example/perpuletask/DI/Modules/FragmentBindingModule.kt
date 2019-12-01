package com.example.perpuletask.DI.Modules

import com.example.perpuletask.Fragments.AudioListFragment
import com.example.perpuletask.Fragments.AudioPlayerFragment
import com.example.perpuletask.Fragments.FragmentModules.AudioListFragModule
import com.example.perpuletask.Fragments.FragmentModules.AudioPlayerFragModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class
FragmentBindingModule {

    @ContributesAndroidInjector(modules = [AudioListFragModule::class])
    abstract fun contributesSearchFrag(): AudioListFragment
//    @ContributesAndroidInjector(modules = [AudioPlayerFragment::class])

    @ContributesAndroidInjector(modules = [AudioPlayerFragModule::class])
    abstract fun contributesRepoDetailsFrag(): AudioPlayerFragment
}