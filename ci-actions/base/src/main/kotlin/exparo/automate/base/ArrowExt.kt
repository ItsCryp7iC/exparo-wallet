package exparo.automate.base

import arrow.core.Either

fun <A, B> Either<A, B>.getOrThrow(): B {
    return fold(
        ifLeft = { throw ExparoError(it.toString()) },
        ifRight = { it }
    )
}
