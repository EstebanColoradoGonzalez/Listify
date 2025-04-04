package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.entity.UnitOfMeasurement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UnitOfMeasurementViewModel(application: Application) : AndroidViewModel(application) {
    private val unitOfMeasurementDAO = AppDatabase.getDatabase(application).unitOfMeasurementDao()

    suspend fun getUnitsOfMeasurement(): List<UnitOfMeasurement> {
        return withContext(Dispatchers.IO) {
            unitOfMeasurementDAO.getUnitsOfMeasurement()
        }
    }
}