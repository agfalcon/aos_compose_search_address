package kgb.plum.searchaddresspractice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val _address = MutableLiveData("")
    val address: LiveData<String> = _address

    fun setAddress(text: String) {
        _address.value = text
    }
}