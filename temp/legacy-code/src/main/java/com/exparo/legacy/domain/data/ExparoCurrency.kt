package com.exparo.wallet.domain.data

import android.icu.util.Currency
import androidx.compose.runtime.Immutable
import com.exparo.legacy.utils.getDefaultFIATCurrency

@Immutable
data class ExparoCurrency(
    val code: String,
    val name: String,
    val isCrypto: Boolean
) {
    companion object {
        private const val CRYPTO_DECIMAL = 18
        private const val FIAT_DECIMAL = 2

        private val CRYPTO = setOf(
            ExparoCurrency(
                code = "BTC",
                name = "Bitcoin",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "ETH",
                name = "Ethereum",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "USDT",
                name = "Tether USD",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "BNB",
                name = "Binance Coin",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "ADA",
                name = "Cardano",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "XRP",
                name = "Ripple",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "DOGE",
                name = "Dogecoin",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "USDC",
                name = "USD Coin",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "DOT",
                name = "Polkadot",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "UNI",
                name = "Uniswap",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "BUSD",
                name = "Binance USD",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "BCH",
                name = "Bitcoin Cash",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "SOL",
                name = "Solana",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "LTC",
                name = "Litecoin",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "LINK",
                name = "ChainLink Token",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "SHIB",
                name = "Shiba Inu coin",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "LUNA",
                name = "Terra",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "AVAX",
                name = "Avalanche",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "MATIC",
                name = "Polygon",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "CRO",
                name = "Cronos",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "WBTC",
                name = "Wrapped Bitcoin",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "ALGO",
                name = "Algorand",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "XLM",
                name = "Stellar",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "MANA",
                name = "Decentraland",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "AXS",
                name = "Axie Infinity",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "DAI",
                name = "Dai",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "ICP",
                name = "Internet Computer",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "ATOM",
                name = "Cosmos",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "FIL",
                name = "Filecoin",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "ETC",
                name = "Ethereum Classic",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "DASH",
                name = "Dash",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "TRX",
                name = "Tron",
                isCrypto = true
            ),
            ExparoCurrency(
                code = "TON",
                name = "Tonchain",
                isCrypto = true
            ),
        )

        fun getAvailable(): List<ExparoCurrency> {
            return Currency.getAvailableCurrencies()
                .map {
                    ExparoCurrency(
                        code = it.currencyCode,
                        name = it.displayName,
                        isCrypto = false
                    )
                }
                .plus(CRYPTO)
        }

        fun fromCode(code: String): ExparoCurrency? {
            if (code.isBlank()) return null

            val crypto = CRYPTO.find { it.code == code }
            if (crypto != null) {
                return crypto
            }

            return try {
                val fiat = Currency.getInstance(code)
                ExparoCurrency(
                    fiatCurrency = fiat
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun getDefault(): ExparoCurrency = ExparoCurrency(
            fiatCurrency = getDefaultFIATCurrency()
        )

        fun getDecimalPlaces(assetCode: String): Int =
            if (fromCode(assetCode) in CRYPTO) CRYPTO_DECIMAL else FIAT_DECIMAL
    }

    constructor(fiatCurrency: Currency) : this(
        code = fiatCurrency.currencyCode,
        name = fiatCurrency.displayName,
        isCrypto = false
    )
}
