package com.exparo.domain.usecase.csv

import android.net.Uri
import arrow.core.Either
import com.exparo.base.model.TransactionType
import com.exparo.base.threading.DispatchersProvider
import com.exparo.base.time.TimeConverter
import com.exparo.data.file.FileSystem
import com.exparo.data.model.Account
import com.exparo.data.model.AccountId
import com.exparo.data.model.Category
import com.exparo.data.model.CategoryId
import com.exparo.data.model.Expense
import com.exparo.data.model.Income
import com.exparo.data.model.Transaction
import com.exparo.data.model.Transfer
import com.exparo.data.model.primitive.NonNegativeDouble
import com.exparo.data.model.primitive.toNonNegative
import com.exparo.data.repository.AccountRepository
import com.exparo.data.repository.CategoryRepository
import com.exparo.data.repository.TransactionRepository
import kotlinx.coroutines.withContext
import org.apache.commons.text.StringEscapeUtils
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlin.experimental.ExperimentalTypeInference

class ExportCsvUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository,
    private val dispatchers: DispatchersProvider,
    private val fileSystem: FileSystem,
    private val timeConverter: TimeConverter
) {

    suspend fun exportToFile(
        outputFile: Uri,
        exportScope: suspend TransactionRepository.() -> List<Transaction> = {
            transactionRepository.findAll()
        }
    ): Either<FileSystem.Failure, Unit> = withContext(dispatchers.io) {
        val csv = exportCsv(exportScope)
        fileSystem.writeToFile(outputFile, csv)
    }

    suspend fun exportCsv(
        exportScope: suspend TransactionRepository.() -> List<Transaction>
    ): String = withContext(dispatchers.io) {
        val transactions = transactionRepository.exportScope()
        val accountsMap = accountRepository.findAll().associateBy(Account::id)
        val categoriesMap = categoryRepository.findAll().associateBy(Category::id)

        buildString {
            append(ExparoCsvRow.Columns.joinToString(separator = CSV_SEPARATOR))
            append(NEWLINE)
            for (trn in transactions) {
                append(
                    trn.toExparoCsvRow().toCsvString(
                        accountsMap = accountsMap,
                        categoriesMap = categoriesMap
                    )
                )
                append(NEWLINE)
            }
        }
    }

    private fun ExparoCsvRow.toCsvString(
        accountsMap: Map<AccountId, Account>,
        categoriesMap: Map<CategoryId, Category>,
    ): String = csvRow {
        // Date
        csvAppend(date?.csvFormat(timeConverter))
        // Title
        csvAppend(title?.value)
        // Category
        csvAppend(categoriesMap[category]?.name?.value)
        // Account
        csvAppend(accountsMap[account]?.name?.value)
        // Amount
        csvAppend(amount.value.csvFormat())
        // Currency
        csvAppend(currency.code)
        // Type
        csvAppend(type.name)
        // Transfer Amount
        csvAppend(transferAmount?.value?.csvFormat())
        // Transfer Currency
        csvAppend(transferCurrency?.code)
        // To Account
        csvAppend(accountsMap[toAccountId]?.name?.value)
        // Receive Amount
        csvAppend(receiveAmount?.value?.csvFormat())
        // Receive Currency
        csvAppend(receiveCurrency?.code)
        // Description
        csvAppend(description?.value)
        // Due Date
        csvAppend(dueData?.csvFormat(timeConverter))
        // ID
        csvAppend(id.value.toString())
    }

    @OptIn(ExperimentalTypeInference::class)
    private fun csvRow(@BuilderInference build: CsvRowScope.() -> Unit): String {
        val columns = mutableListOf<String>()
        val rowScope = object : CsvRowScope {
            override fun csvAppend(value: String?) {
                columns.add(value?.escapeCsvString() ?: "")
            }
        }
        rowScope.build()
        return columns.joinToString(separator = CSV_SEPARATOR)
    }

    private fun String.escapeCsvString(): String = try {
        StringEscapeUtils.escapeCsv(this).escapeSpecialChars()
    } catch (e: Exception) {
        escapeSpecialChars()
    }

    private fun String.escapeSpecialChars(): String = replace("\\", "")

    private fun Transaction.toExparoCsvRow(): ExparoCsvRow = when (this) {
        is Expense -> expenseCsvRow()
        is Income -> incomeCsvRow()
        is Transfer -> transferCsvRow()
    }

    private fun Expense.expenseCsvRow(): ExparoCsvRow = ExparoCsvRow(
        date = time.takeIf
        { settled },
        title = title,
        category = category,
        account = account,
        amount = value.amount.toNonNegative(),
        currency = value.asset,
        type = TransactionType.EXPENSE,
        transferAmount = null,
        transferCurrency = null,
        toAccountId = null,
        receiveAmount = null,
        receiveCurrency = null,
        description = description,
        dueData = time.takeIf
        { !settled },
        id = id
    )

    private fun Income.incomeCsvRow(): ExparoCsvRow = ExparoCsvRow(
        date = time.takeIf { settled },
        title = title,
        category = category,
        account = account,
        amount = value.amount.toNonNegative(),
        currency = value.asset,
        type = TransactionType.INCOME,
        transferAmount = null,
        transferCurrency = null,
        toAccountId = null,
        receiveAmount = null,
        receiveCurrency = null,
        description = description,
        dueData = time.takeIf { !settled },
        id = id
    )

    private fun Transfer.transferCsvRow(): ExparoCsvRow = ExparoCsvRow(
        date = time.takeIf { settled },
        title = title,
        category = category,
        account = fromAccount,
        amount = NonNegativeDouble.unsafe(0.0),
        currency = fromValue.asset,
        type = TransactionType.TRANSFER,
        transferAmount = fromValue.amount,
        transferCurrency = fromValue.asset,
        toAccountId = toAccount,
        receiveAmount = toValue.amount,
        receiveCurrency = toValue.asset,
        description = description,
        dueData = time.takeIf { !settled },
        id = id
    )

    private fun Instant.csvFormat(timeConverter: TimeConverter): String {
        return with(timeConverter) {
            this@csvFormat.toLocalDateTime()
        }.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    private fun Double.csvFormat(): String = DecimalFormat(NUMBER_FORMAT).apply {
        decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH)
    }.format(this)

    interface CsvRowScope {
        fun csvAppend(value: String?)
    }

    companion object {
        private const val CSV_SEPARATOR = ","
        private const val NEWLINE = "\n"
        private const val NUMBER_FORMAT = "#,##0.00"
    }
}