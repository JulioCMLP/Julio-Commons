package com.julioapps.commons.helpers

public inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long) = this.map { selector(it) }.sum()
