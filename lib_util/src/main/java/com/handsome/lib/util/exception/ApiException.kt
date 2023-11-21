package com.handsome.lib.util.exception


class ApiException(
  val errorCode: Int,
  val errorMsg: String
) : RuntimeException("errorCode = $errorCode   errorMsg = $errorMsg")