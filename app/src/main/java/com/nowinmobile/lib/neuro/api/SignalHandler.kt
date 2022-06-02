package com.nowinmobile.lib.neuro.api

interface SignalHandler {
    val schemes: Set<String>

    val hosts: Set<String>

    val paths: Set<String>

    fun handle(signal: Signal)
}