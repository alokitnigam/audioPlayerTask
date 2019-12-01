package com.example.perpuletask.DI

import android.app.Application
import com.app.nasatask.DI.Modules.ContextModule
import com.example.perpuletask.DI.Modules.ActivityBindingModule
import com.example.perpuletask.DI.Modules.FragmentBindingModule
import com.example.perpuletask.DI.Network.NetworkModule
import com.example.perpuletask.DI.VMFactory.MyViewModelModule
import com.example.perpuletask.DI.VMFactory.ViewModelFactoryModule
import com.example.perpuletask.DI.database.RoomDBModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class,AndroidSupportInjectionModule::class, FragmentBindingModule::class,
    ActivityBindingModule::class, NetworkModule::class,
    ViewModelFactoryModule::class, MyViewModelModule::class, RoomDBModule::class])
interface ApplicationComponent : AndroidInjector<DaggerApplication> {


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }

}