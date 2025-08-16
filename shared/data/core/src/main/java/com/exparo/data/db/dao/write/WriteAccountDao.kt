package com.exparo.data.db.dao.write

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.exparo.data.db.entity.AccountEntity
import java.util.UUID

@Dao
interface WriteAccountDao {
    @Upsert
    suspend fun save(value: AccountEntity)

    @Upsert
    suspend fun saveMany(values: List<AccountEntity>)

    @Query("DELETE FROM accounts WHERE id = :id")
    suspend fun deleteById(id: UUID)

    @Query("DELETE FROM accounts")
    suspend fun deleteAll()
}
