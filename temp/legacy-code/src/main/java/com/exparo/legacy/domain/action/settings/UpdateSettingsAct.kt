package com.exparo.legacy.domain.action.settings

import com.exparo.data.db.dao.write.WriteSettingsDao
import com.exparo.frp.action.FPAction
import com.exparo.legacy.datamodel.Settings
import javax.inject.Inject

class UpdateSettingsAct @Inject constructor(
    private val writeSettingsDao: WriteSettingsDao
) : FPAction<Settings, Settings>() {
    override suspend fun Settings.compose(): suspend () -> Settings = suspend {
        writeSettingsDao.save(this.toEntity())
        this
    }
}
