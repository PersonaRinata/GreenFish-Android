package com.handsome.module.login.textwatcher

import android.text.Editable
import com.google.android.material.textfield.TextInputLayout


class Password1Watcher(
  password: TextInputLayout
) : BaseTextWatcher(password) {

  override fun afterTextChanged(s: Editable) {
    super.afterTextChanged(s)
  }
}