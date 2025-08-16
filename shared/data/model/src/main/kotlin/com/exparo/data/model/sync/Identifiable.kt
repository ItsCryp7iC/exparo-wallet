package com.exparo.data.model.sync

interface Identifiable<ID : UniqueId> {
    val id: ID
}
