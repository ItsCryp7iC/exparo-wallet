package com.exparo.navigation

/**
 * Marks a screen in the Exparo Wallet's navigation graph.
 * Extend it when creating a new screen.
 */
sealed class Screen(val route: String) {
    /**
     * Marks whether a given screen is a legacy Exparo Wallet one.
     * If it's a legacy screen, it automatically adds a Surface to make it work.
     * Do NOT mark new Material3 screens as legacy.
     */
    open val isLegacy: Boolean
        get() = false
}