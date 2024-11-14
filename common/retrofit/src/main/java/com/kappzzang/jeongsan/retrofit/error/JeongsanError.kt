package com.kappzzang.jeongsan.retrofit.error

class AuthenticateError(message: String) : Throwable(message) {
    constructor(): this("")
}

class InvalidInputError(message: String) : Throwable(message) {
    constructor(): this("")
}

class ItemNotFoundError(message: String) : Throwable(message) {
    constructor(): this("")
}

class ServerInternalError(message: String) : Throwable(message) {
    constructor(): this("")
}
