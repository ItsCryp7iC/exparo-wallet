package com.exparo.drivebackup.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "backup_metadata")
data class BackupMetadata(
    @PrimaryKey
    val id: String,
    val fileName: String,
    val fileSize: Long,
    val createdDate: Date,
    val lastModifiedDate: Date,
    val driveFileId: String?,
    val isUploaded: Boolean,
    val isEncrypted: Boolean,
    val version: Int = 1
)