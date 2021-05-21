package com.caspar.xl.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.caspar.xl.bean.NetworkResult
import com.caspar.xl.bean.response.TranslateBean
import com.caspar.xl.repository.MenuRepository
import kotlinx.coroutines.launch

/**
 *  "CasparXL" 创建 2020/5/12.
 *   界面名称以及功能:
 */
class TranslateViewModel(application: Application) : AndroidViewModel(application) {
    //mData，使用Triple<A,B,C>储存网络请求反应，A为网络请求是否成功，B为成功的消息，C为错误的信息
    val mData: MutableLiveData<NetworkResult<TranslateBean>> by lazy {
        MutableLiveData<NetworkResult<TranslateBean>>()
    }

    fun translate(text: String) {
        viewModelScope.launch {
            val value = MenuRepository.translate(text)
            if (value.code == 200) { //网络请求成功，则返回数据
                mData.value = NetworkResult.Success(value.body)
            } else { //网络请求失败，则返回一个errorBean，errorBean一个Activity拦截一次就够了，统一做处理
                mData.value = NetworkResult.Error(null,code = value.code,message = value.msg)
            }
        }
    }
}
