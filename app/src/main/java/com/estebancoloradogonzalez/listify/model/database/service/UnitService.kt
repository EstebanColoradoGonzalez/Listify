package com.estebancoloradogonzalez.listify.model.database.service

import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.entity.UnitOfMeasurement
import com.estebancoloradogonzalez.listify.utils.TextConstants

class UnitService(private val db: AppDatabase) {
    suspend fun prepopulate() {
        val unitDao = db.unitOfMeasurementDao()
        unitDao.deleteAll()
        unitDao.insertAll(
            UnitOfMeasurement(name = TextConstants.UNIT_KILOGRAM, symbol = TextConstants.UNIT_KILOGRAM_SHORT),
            UnitOfMeasurement(name = TextConstants.UNIT_POUND, symbol = TextConstants.UNIT_POUND_SHORT),
            UnitOfMeasurement(name = TextConstants.UNIT_LITER, symbol = TextConstants.UNIT_LITER_SHORT),
            UnitOfMeasurement(name = TextConstants.UNIT_COUNT, symbol = TextConstants.UNIT_COUNT_SHORT)
        )
    }
}