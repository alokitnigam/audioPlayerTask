package com.app.nasa.Base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel

import dagger.android.support.DaggerFragment


abstract class BaseFragment<T : ViewModel>  : DaggerFragment() {
    private var viewModel: T? = null
    @LayoutRes
    protected abstract fun layoutRes(): Int
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(layoutRes(), container, false)
        return view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.viewModel = viewModel ?: getViewModel()
    }
    abstract fun getViewModel(): T
}