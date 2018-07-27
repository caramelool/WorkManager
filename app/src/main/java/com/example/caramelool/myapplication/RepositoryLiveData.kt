package com.example.caramelool.myapplication

import android.arch.lifecycle.*

class RepositoryLiveData<T> {

    private val valueLiveData by lazy { MutableLiveData<T>() }
    private val errorLiveData by lazy { MutableLiveData<String>() }

    var value: T? = null
        set(value) {
            valueLiveData.value = value
            field = value
        }

    var error: String? = null
        set(value) {
            errorLiveData.value = value
            field = value
        }

    fun observer(lifecycle: LifecycleOwner, observer: Observer<T>): RepositoryLiveData<T> {
        valueLiveData.observe(lifecycle, observer)
        return this
    }

    fun observerForever(observer: Observer<T>): RepositoryLiveData<T> {
        valueLiveData.observeForever(observer)
        return this
    }

    fun observerError(lifecycle: LifecycleOwner, observer: Observer<String>): RepositoryLiveData<T> {
        errorLiveData.observe(lifecycle, observer)
        return this
    }

    fun observerErrorForever(observer: Observer<String>): RepositoryLiveData<T> {
        errorLiveData.observeForever(observer)
        return this
    }

    fun postValue(value: T) {
        valueLiveData.postValue(value)
    }

    fun postError(error: String) {
        errorLiveData.postValue(error)
    }
}