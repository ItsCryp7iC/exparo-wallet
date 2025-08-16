package com.exparo.data.db.dao.write

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.exparo.data.db.entity.LoanRecordEntity
import java.util.UUID

@Dao
interface WriteLoanRecordDao {
    @Upsert
    suspend fun save(value: LoanRecordEntity)

    @Upsert
    suspend fun saveMany(value: List<LoanRecordEntity>)

    @Query("DELETE FROM loan_records WHERE id = :id")
    suspend fun deleteById(id: UUID)

    @Query("DELETE FROM loan_records")
    suspend fun deleteAll()
}
