package com.caspar.xl.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.caspar.base.base.BaseActivity
import com.caspar.base.ext.setOnClickListener
import com.caspar.xl.R
import com.caspar.xl.databinding.ActivityTranslateBinding
import com.caspar.xl.eventandstate.ViewEvent
import com.caspar.xl.ext.binding
import com.caspar.xl.ext.observeEvent
import com.caspar.xl.helper.loadNet
import com.caspar.xl.ui.dialog.WaitDialog
import com.caspar.xl.ui.viewmodel.TranslateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TranslateActivity : BaseActivity(), View.OnClickListener {
    private val mBindingView: ActivityTranslateBinding by binding()

    private val mViewModel: TranslateViewModel by viewModels()
    private val dialog by lazy {
        WaitDialog.Builder(this).setMessage("稍等")
    }

    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        mBindingView.title.tvCenter.text = "翻译"
        setOnClickListener(this, R.id.tv_left)
        initViewObserver()
        initNetworkObserver()
        mBindingView.etEnter.setOnClickListener {
            mViewModel.getImage()
        }
    }

    //网络请求监听
    private fun initNetworkObserver() {
        lifecycleScope.launch {
            mViewModel.translateResult.collect {
                mBindingView.ivImage.loadNet(it.random().url?:"", R.drawable.image_loading_bg)
            }
        }
    }

    //视图事件监听
    private fun initViewObserver() {
        mViewModel.viewEvent.observeEvent(this@TranslateActivity) {
            when (it) {
                ViewEvent.DismissDialog -> dialog.dismiss()
                ViewEvent.ShowDialog -> dialog.show()
                is ViewEvent.ShowToast -> {
                    toast(it.message)
                }
            }
        }
    }

    override fun hideSoftByEditViewIds(): IntArray {
        return arrayListOf(R.id.et_enter).toIntArray()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_left -> finish()
        }
    }

}