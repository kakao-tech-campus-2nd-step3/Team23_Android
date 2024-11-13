package com.kappzzang.jeongsan.retrofit.error

class AuthenticateError(message: String) : Throwable(message)

class InvalidInputError(message: String) : Throwable(message)

class ServerInternalError(message: String) : Throwable(message)
