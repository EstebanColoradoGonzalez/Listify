package com.estebancoloradogonzalez.listify.model.database.service

import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.entity.State
import com.estebancoloradogonzalez.listify.utils.TextConstants

class StateService(private val db: AppDatabase) {
    suspend fun prepopulate() {
        val stateDao = db.stateDao()
        stateDao.deleteAll()
        stateDao.insertAll(
            State(name = TextConstants.STATUS_ACTIVE),
            State(name = TextConstants.STATUS_COMPLETED),
            State(name = TextConstants.STATUS_CANCELLED)
        )
    }
}