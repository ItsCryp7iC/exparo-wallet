package com.exparo.data.di

import android.content.Context
import com.exparo.data.db.ExparoRoomDatabase
import com.exparo.data.db.dao.read.AccountDao
import com.exparo.data.db.dao.read.BudgetDao
import com.exparo.data.db.dao.read.CategoryDao
import com.exparo.data.db.dao.read.ExchangeRatesDao
import com.exparo.data.db.dao.read.LoanDao
import com.exparo.data.db.dao.read.LoanRecordDao
import com.exparo.data.db.dao.read.PlannedPaymentRuleDao
import com.exparo.data.db.dao.read.SettingsDao
import com.exparo.data.db.dao.read.TagAssociationDao
import com.exparo.data.db.dao.read.TagDao
import com.exparo.data.db.dao.read.TransactionDao
import com.exparo.data.db.dao.read.UserDao
import com.exparo.data.db.dao.write.WriteAccountDao
import com.exparo.data.db.dao.write.WriteBudgetDao
import com.exparo.data.db.dao.write.WriteCategoryDao
import com.exparo.data.db.dao.write.WriteExchangeRatesDao
import com.exparo.data.db.dao.write.WriteLoanDao
import com.exparo.data.db.dao.write.WriteLoanRecordDao
import com.exparo.data.db.dao.write.WritePlannedPaymentRuleDao
import com.exparo.data.db.dao.write.WriteSettingsDao
import com.exparo.data.db.dao.write.WriteTagAssociationDao
import com.exparo.data.db.dao.write.WriteTagDao
import com.exparo.data.db.dao.write.WriteTransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDbModule {

    @Provides
    @Singleton
    fun provideExparoRoomDatabase(
        @ApplicationContext appContext: Context,
    ): ExparoRoomDatabase {
        return ExparoRoomDatabase.create(
            applicationContext = appContext,
        )
    }

    @Provides
    fun provideUserDao(db: ExparoRoomDatabase): UserDao {
        return db.userDao
    }

    @Provides
    fun provideAccountDao(db: ExparoRoomDatabase): AccountDao {
        return db.accountDao
    }

    @Provides
    fun provideTransactionDao(db: ExparoRoomDatabase): TransactionDao {
        return db.transactionDao
    }

    @Provides
    fun provideCategoryDao(db: ExparoRoomDatabase): CategoryDao {
        return db.categoryDao
    }

    @Provides
    fun provideBudgetDao(db: ExparoRoomDatabase): BudgetDao {
        return db.budgetDao
    }

    @Provides
    fun provideSettingsDao(db: ExparoRoomDatabase): SettingsDao {
        return db.settingsDao
    }

    @Provides
    fun provideLoanDao(db: ExparoRoomDatabase): LoanDao {
        return db.loanDao
    }

    @Provides
    fun provideLoanRecordDao(db: ExparoRoomDatabase): LoanRecordDao {
        return db.loanRecordDao
    }

    @Provides
    fun providePlannedPaymentRuleDao(db: ExparoRoomDatabase): PlannedPaymentRuleDao {
        return db.plannedPaymentRuleDao
    }

    @Provides
    fun provideTagDao(db: ExparoRoomDatabase): TagDao {
        return db.tagDao
    }

    @Provides
    fun provideTagAssociationDao(db: ExparoRoomDatabase): TagAssociationDao {
        return db.tagAssociationDao
    }

    @Provides
    fun provideExchangeRatesDao(
        roomDatabase: ExparoRoomDatabase
    ): ExchangeRatesDao {
        return roomDatabase.exchangeRatesDao
    }

    @Provides
    fun provideWriteAccountDao(db: ExparoRoomDatabase): WriteAccountDao {
        return db.writeAccountDao
    }

    @Provides
    fun provideWriteTransactionDao(db: ExparoRoomDatabase): WriteTransactionDao {
        return db.writeTransactionDao
    }

    @Provides
    fun provideWriteCategoryDao(db: ExparoRoomDatabase): WriteCategoryDao {
        return db.writeCategoryDao
    }

    @Provides
    fun provideWriteBudgetDao(db: ExparoRoomDatabase): WriteBudgetDao {
        return db.writeBudgetDao
    }

    @Provides
    fun provideWriteSettingsDao(db: ExparoRoomDatabase): WriteSettingsDao {
        return db.writeSettingsDao
    }

    @Provides
    fun provideWriteLoanDao(db: ExparoRoomDatabase): WriteLoanDao {
        return db.writeLoanDao
    }

    @Provides
    fun provideWriteLoanRecordDao(db: ExparoRoomDatabase): WriteLoanRecordDao {
        return db.writeLoanRecordDao
    }

    @Provides
    fun provideWritePlannedPaymentRuleDao(db: ExparoRoomDatabase): WritePlannedPaymentRuleDao {
        return db.writePlannedPaymentRuleDao
    }

    @Provides
    fun provideWriteExchangeRatesDao(db: ExparoRoomDatabase): WriteExchangeRatesDao {
        return db.writeExchangeRatesDao
    }

    @Provides
    fun provideWriteTagDao(db: ExparoRoomDatabase): WriteTagDao {
        return db.writeTagDao
    }

    @Provides
    fun provideWriteTagAssociationDao(db: ExparoRoomDatabase): WriteTagAssociationDao {
        return db.writeTagAssociationDao
    }
}
