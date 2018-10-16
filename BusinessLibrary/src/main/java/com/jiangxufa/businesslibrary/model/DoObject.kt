package com.jiangxufa.businesslibrary.model


import java.io.Serializable

abstract class DoObject : Serializable {


    lateinit var id: String
    var updatetime: Long = 0
    var createtime: Long = 0
    var status: Int = 0

    val isEmptyObject: Boolean
        get() = id.isEmpty()

    constructor() {}

    constructor(id: String) {
        this.id = id
    }

    constructor(id: String, updatetime: Long, createtime: Long, status: Int) {
        this.id = id
        this.updatetime = updatetime
        this.createtime = createtime
        this.status = status
    }

}
