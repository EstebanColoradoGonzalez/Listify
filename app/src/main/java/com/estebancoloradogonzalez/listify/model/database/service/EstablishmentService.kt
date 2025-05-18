package com.estebancoloradogonzalez.listify.model.database.service

import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.entity.Establishment
import com.estebancoloradogonzalez.listify.utils.TextConstants

class EstablishmentService(private val db: AppDatabase) {
    suspend fun prepopulate() {
        val establishmentDao = db.establishmentDao()
        establishmentDao.deleteAll()
        establishmentDao.insertAll(
            Establishment(name = TextConstants.STORE_D1),
            Establishment(name = TextConstants.STORE_DOLLAR_CITY),
            Establishment(name = TextConstants.STORE_BUTCHER),
            Establishment(name = TextConstants.STORE_GREENGROCER),
            Establishment(name = TextConstants.STORE_OTHER)
        )
    }
}