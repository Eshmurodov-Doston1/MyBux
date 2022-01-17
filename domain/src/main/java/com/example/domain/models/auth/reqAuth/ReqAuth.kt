package com.example.domain.models.auth.reqAuth

class ReqAuth {
    var sig:String?=null
    var phone:String?=null
    var password:String?=null
    var remember:String?=null
    var device_id:String?=null
    var terminal_id:String?=null

    constructor()
    constructor(
        sig: String?,
        phone: String?,
        password: String?,
        remember: String?,
        device_id: String?,
        terminal_id: String?
    ) {
        this.sig = sig
        this.phone = phone
        this.password = password
        this.remember = remember
        this.device_id = device_id
        this.terminal_id = terminal_id
    }


    override fun toString(): String {
        return "ReqAuth(sig=$sig, phone=$phone, password=$password, remember=$remember, device_id=$device_id)"
    }


}