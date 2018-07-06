package com.adiliqbal.rxbus

import io.reactivex.disposables.CompositeDisposable

class Subscriber(var subscriber: Any?) {
    lateinit var compositeDisposable: CompositeDisposable
    var registered: Boolean = false
}
