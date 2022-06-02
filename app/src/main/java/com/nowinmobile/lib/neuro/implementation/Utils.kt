package com.nowinmobile.lib.neuro.implementation

internal fun String.mapType(): Any {
    val int = toIntOrNull()
    if (int != null) return int

    val long = toLongOrNull()
    if (long != null) return long

    val double = toDoubleOrNull()
    if (double != null) return double

    val boolean = toBooleanStrictOrNull()
    if (boolean != null) return boolean

    return this
}