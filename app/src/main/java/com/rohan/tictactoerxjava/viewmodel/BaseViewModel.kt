package com.rohan.tictactoerxjava.viewmodel

import androidx.lifecycle.ViewModel

abstract class BaseViewModel: ViewModel {
    constructor() {
        initDefaultParams()
    }
    abstract fun initDefaultParams()
}