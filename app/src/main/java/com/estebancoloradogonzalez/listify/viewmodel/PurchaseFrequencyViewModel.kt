package com.estebancoloradogonzalez.listify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.entity.PurchaseFrequency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PurchaseFrequencyViewModel(application: Application) : AndroidViewModel(application) {
    private val purchaseFrequencyDao = AppDatabase.getDatabase(application).purchaseFrequencyDao()

    suspend fun fetchPurchaseFrequencies(): List<PurchaseFrequency> = withContext(Dispatchers.IO) {
        purchaseFrequencyDao.getAll()
    }
}
