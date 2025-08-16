package com.exparo.data.model.testing

import com.exparo.data.model.AccountId
import com.exparo.data.model.CategoryId
import com.exparo.data.model.TransactionId
import java.util.UUID

object ModelFixtures {
    val AccountId = AccountId(UUID.randomUUID())
    val CategoryId = CategoryId(UUID.randomUUID())
    val TransactionId = TransactionId(UUID.randomUUID())
}
