package com.example.domain.models.sig

class Sig {
    val response: Response?=null

    constructor()

    constructor(response: Response)

    override fun toString(): String {
        return "Sig(response=$response)"
    }
}