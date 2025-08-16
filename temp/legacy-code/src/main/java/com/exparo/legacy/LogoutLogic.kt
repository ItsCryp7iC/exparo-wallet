package com.exparo.legacy

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.exparo.base.legacy.SharedPrefs
import com.exparo.data.DataObserver
import com.exparo.data.DataWriteEvent
import com.exparo.data.db.dao.read.UserDao
import com.exparo.data.db.dao.write.WriteBudgetDao
import com.exparo.data.db.dao.write.WriteLoanDao
import com.exparo.data.db.dao.write.WriteLoanRecordDao
import com.exparo.data.db.dao.write.WritePlannedPaymentRuleDao
import com.exparo.data.db.dao.write.WriteSettingsDao
import com.exparo.data.repository.AccountRepository
import com.exparo.data.repository.CategoryRepository
import com.exparo.data.repository.ExchangeRatesRepository
import com.exparo.data.repository.TagRepository
import com.exparo.data.repository.TransactionRepository
import com.exparo.legacy.utils.ioThread
import com.exparo.navigation.MainScreen
import com.exparo.navigation.Navigation
import com.exparo.navigation.OnboardingScreen
import javax.inject.Inject

@Deprecated("Migrate to an UseCase in the domain layer.")
class LogoutLogic @Inject constructor(
    private val sharedPrefs: SharedPrefs,
    private val navigation: Navigation,
    private val dataObserver: DataObserver,
    private val dataStore: DataStore<Preferences>,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val tagRepository: TagRepository,
    private val userDao: UserDao,
    private val writeSettingsDao: WriteSettingsDao,
    private val writePlannedPaymentRuleDao: WritePlannedPaymentRuleDao,
    private val writeBudgetDao: WriteBudgetDao,
    private val writeLoanDao: WriteLoanDao,
    private val writeLoanRecordDao: WriteLoanRecordDao,
    private val exchangeRatesRepository: ExchangeRatesRepository
) {
    suspend fun logout() {
        ioThread {
            deleteAllData()
            dataStore.edit {
                it.clear()
            }
            sharedPrefs.removeAll()
        }

        dataObserver.post(DataWriteEvent.AllDataChange)
        navigation.resetBackStack()
        navigation.navigateTo(OnboardingScreen)
    }

    private suspend fun deleteAllData() {
        accountRepository.deleteAll()
        transactionRepository.deleteAll()
        categoryRepository.deleteAll()
        tagRepository.deleteAll()
        writeSettingsDao.deleteAll()
        writePlannedPaymentRuleDao.deleteAll()
        userDao.deleteAll()
        writeBudgetDao.deleteAll()
        writeLoanDao.deleteAll()
        writeLoanRecordDao.deleteAll()
        exchangeRatesRepository.deleteAll()
    }

    suspend fun cloudLogout() {
        navigation.navigateTo(MainScreen)
    }
}
