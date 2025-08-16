package com.exparo.design.utils

import android.os.Handler
import android.os.Looper

@Deprecated("Old design system. Use `:exparo-design` and Material3")
fun postDelayed(delayMs: Long, run: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({ run() }, delayMs)
}
