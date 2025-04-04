package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.entity.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StateViewModel(application: Application) : AndroidViewModel(application) {
    private val stateDAO = AppDatabase.getDatabase(application).stateDao()

    suspend fun getStates(): List<State> {
        return withContext(Dispatchers.IO) {
            stateDAO.getStates()
        }
    }
}