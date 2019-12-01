package  com.example.perpuletask.DI.Modules


import com.example.perpuletask.MainActivity
import com.example.perpuletask.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributesMainActivity(): MainActivity


}