package com.exparo.data.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.exparo.data.db.dao.read.AccountDao
import com.exparo.data.db.dao.read.BudgetDao
import com.exparo.data.db.dao.read.CategoryDao
import com.exparo.data.db.dao.read.ExchangeRatesDao
import com.exparo.data.db.dao.read.LoanDao
import com.exparo.data.db.dao.read.LoanRecordDao
import com.exparo.data.db.dao.read.PlannedPaymentRuleDao
import com.exparo.data.db.dao.read.SettingsDao
import com.exparo.data.db.dao.read.TagDao
import com.exparo.data.db.dao.read.TagAssociationDao
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
import com.exparo.data.db.dao.write.WriteTagDao
import com.exparo.data.db.dao.write.WriteTagAssociationDao
import com.exparo.data.db.dao.write.WriteTransactionDao
import com.exparo.data.db.entity.AccountEntity
import com.exparo.data.db.entity.BudgetEntity
import com.exparo.data.db.entity.CategoryEntity
import com.exparo.data.db.entity.ExchangeRateEntity
import com.exparo.data.db.entity.LoanEntity
import com.exparo.data.db.entity.LoanRecordEntity
import com.exparo.data.db.entity.PlannedPaymentRuleEntity
import com.exparo.data.db.entity.SettingsEntity
import com.exparo.data.db.entity.TagEntity
import com.exparo.data.db.entity.TagAssociationEntity
import com.exparo.data.db.entity.TransactionEntity
import com.exparo.data.db.entity.UserEntity
import com.exparo.data.db.migration.Migration123to124_LoanIncludeDateTime
import com.exparo.data.db.migration.Migration124to125_LoanEditDateTime
import com.exparo.data.db.migration.Migration126to127_LoanRecordType
import com.exparo.data.db.migration.Migration127to128_PaidForDateRecord
import com.exparo.data.db.migration.Migration128to129_DeleteIsDeleted
import com.exparo.data.db.migration.Migration129to130_LoanIncludeNote
import com.exparo.domain.db.RoomTypeConverters
import com.exparo.domain.db.migration.Migration105to106_TrnRecurringRules
import com.exparo.domain.db.migration.Migration106to107_Wishlist
import com.exparo.domain.db.migration.Migration107to108_Sync
import com.exparo.domain.db.migration.Migration108to109_Users
import com.exparo.domain.db.migration.Migration109to110_PlannedPayments
import com.exparo.domain.db.migration.Migration110to111_PlannedPaymentRule
import com.exparo.domain.db.migration.Migration111to112_User_testUser
import com.exparo.domain.db.migration.Migration112to113_ExchangeRates
import com.exparo.domain.db.migration.Migration113to114_Multi_Currency
import com.exparo.domain.db.migration.Migration114to115_Category_Account_Icons
import com.exparo.domain.db.migration.Migration115to116_Account_Include_In_Balance
import com.exparo.domain.db.migration.Migration116to117_SalteEdgeIntgration
import com.exparo.domain.db.migration.Migration117to118_Budgets
import com.exparo.domain.db.migration.Migration118to119_Loans
import com.exparo.domain.db.migration.Migration119to120_LoanTransactions
import com.exparo.domain.db.migration.Migration120to121_DropWishlistItem
import com.exparo.domain.db.migration.Migration122to123_ExchangeRates
import com.exparo.domain.db.migration.Migration125to126_Tags

@Database(
    entities = [
        AccountEntity::class, TransactionEntity::class, CategoryEntity::class,
        SettingsEntity::class, PlannedPaymentRuleEntity::class,
        UserEntity::class, ExchangeRateEntity::class, BudgetEntity::class,
        LoanEntity::class, LoanRecordEntity::class, TagEntity::class, TagAssociationEntity::class
    ],
    autoMigrations = [
        AutoMigration(
            from = 121,
            to = 122,
            spec = ExparoRoomDatabase.DeleteSEMigration::class
        )
    ],
    version = 130,
    exportSchema = true
)
@TypeConverters(RoomTypeConverters::class)
abstract class ExparoRoomDatabase : RoomDatabase() {
    abstract val accountDao: AccountDao
    abstract val transactionDao: TransactionDao
    abstract val categoryDao: CategoryDao
    abstract val budgetDao: BudgetDao
    abstract val plannedPaymentRuleDao: PlannedPaymentRuleDao
    abstract val settingsDao: SettingsDao
    abstract val userDao: UserDao
    abstract val exchangeRatesDao: ExchangeRatesDao
    abstract val loanDao: LoanDao
    abstract val loanRecordDao: LoanRecordDao
    abstract val tagDao: TagDao
    abstract val tagAssociationDao: TagAssociationDao

    abstract val writeAccountDao: WriteAccountDao
    abstract val writeTransactionDao: WriteTransactionDao
    abstract val writeCategoryDao: WriteCategoryDao
    abstract val writeBudgetDao: WriteBudgetDao
    abstract val writePlannedPaymentRuleDao: WritePlannedPaymentRuleDao
    abstract val writeSettingsDao: WriteSettingsDao
    abstract val writeExchangeRatesDao: WriteExchangeRatesDao
    abstract val writeLoanDao: WriteLoanDao
    abstract val writeLoanRecordDao: WriteLoanRecordDao
    abstract val writeTagDao: WriteTagDao
    abstract val writeTagAssociationDao: WriteTagAssociationDao

    companion object {
        const val DB_NAME = "exparowallet.db"

        fun migrations() = arrayOf(
            Migration105to106_TrnRecurringRules(),
            Migration106to107_Wishlist(),
            Migration107to108_Sync(),
            Migration108to109_Users(),
            Migration109to110_PlannedPayments(),
            Migration110to111_PlannedPaymentRule(),
            Migration111to112_User_testUser(),
            Migration112to113_ExchangeRates(),
            Migration113to114_Multi_Currency(),
            Migration114to115_Category_Account_Icons(),
            Migration115to116_Account_Include_In_Balance(),
            Migration116to117_SalteEdgeIntgration(),
            Migration117to118_Budgets(),
            Migration118to119_Loans(),
            Migration119to120_LoanTransactions(),
            Migration120to121_DropWishlistItem(),
            Migration122to123_ExchangeRates(),
            Migration123to124_LoanIncludeDateTime(),
            Migration124to125_LoanEditDateTime(),
            Migration125to126_Tags(),
            Migration126to127_LoanRecordType(),
            Migration127to128_PaidForDateRecord(),
            Migration128to129_DeleteIsDeleted(),
            Migration129to130_LoanIncludeNote()
        )

        @Suppress("SpreadOperator")
        fun create(applicationContext: Context): ExparoRoomDatabase {
            return Room
                .databaseBuilder(
                    applicationContext,
                    ExparoRoomDatabase::class.java,
                    DB_NAME
                )
                .addMigrations(*migrations())
                .build()
        }
    }

    @DeleteColumn(tableName = "accounts", columnName = "seAccountId")
    @DeleteColumn(tableName = "transactions", columnName = "seTransactionId")
    @DeleteColumn(tableName = "transactions", columnName = "seAutoCategoryId")
    @DeleteColumn(tableName = "categories", columnName = "seCategoryName")
    class DeleteSEMigration : AutoMigrationSpec
}
