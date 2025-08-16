package com.exparo.search

sealed interface SearchEvent {
    data class Search(val query: String) : SearchEvent
}
