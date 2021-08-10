package io.uvera.template.util.extensions

import org.jdbi.v3.core.result.RowView

inline fun <reified T> RowView.getColumn(s: String): T {
    return this.getColumn(s, T::class.java)
}

inline fun <reified T> RowView.getRow(): T = this.getRow(T::class.java)
