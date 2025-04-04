package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.entity.Establishment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EstablishmentViewModel(application: Application) : AndroidViewModel(application) {
    private val establishmentDAO = AppDatabase.getDatabase(application).establishmentDao()

    suspend fun getEstablishments(): List<Establishment> {
        return withContext(Dispatchers.IO) {
            establishmentDAO.getEstablishments()
        }
    }
}