package com.example.domain.helpers

class InfoModel {
    var name: String? = null
    var value: String? = null

    constructor(name: String?, value: String?) {
        this.name = name
        this.value = value
    }


    override fun toString(): String {
        return "InfoModel(name=$name, value=$value)"
    }


}