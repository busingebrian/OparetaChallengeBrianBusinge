package com.leroy.oparetachallenge.api

open class Resolvable {
    var message: String? = null
    var status: String? = null
    var statusCode = 0
    var error: Error? = null

    @Transient
    var apiException: NetworkApiException? = null
        private set

    constructor() {}
    constructor(resolvableApiException: NetworkApiException?) {
        this.apiException = resolvableApiException
    }

    data class Error(
        val message: String? = "A network error occurred"
    )

    companion object {
        const val STATUS_SUCCESS = "success"
        const val STATUS_FAILED = "failed"
    }
}
