package com.handsome.module.login.textwatcher

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout


open class BaseTextWatcher(
  textInputLayout: TextInputLayout,
) : TextWatcher {
  
  protected val mLayout = textInputLayout
  
  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
  }
  
  override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
  }
  
  override fun afterTextChanged(s: Editable) {
    if (s.length > mLayout.counterMaxLength) {
      mLayout.error = "超出限定字数！"
    }else {
      mLayout.error = null
    }
  }
}