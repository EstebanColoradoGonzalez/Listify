package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.entity.Establishment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EstablishmentViewModel(application: Application) : AndroidViewModel(application) {
    private val establishmentDao = AppDatabase.getDatabase(application).establishmentDao()

    suspend fun fetchEstablishments(): List<Establishment> = withContext(Dispatchers.IO) {
        establishmentDao.getAll()
    }
}