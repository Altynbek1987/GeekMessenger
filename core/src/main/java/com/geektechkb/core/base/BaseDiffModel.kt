package com.geektechkb.core.base

interface BaseDiffModel<T> {
    val id: T?
    override fun equals(other: Any?): Boolean
}