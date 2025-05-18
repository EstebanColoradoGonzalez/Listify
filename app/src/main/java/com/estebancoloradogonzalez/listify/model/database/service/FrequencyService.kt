package com.estebancoloradogonzalez.listify.model.database.service

import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.entity.PurchaseFrequency
import com.estebancoloradogonzalez.listify.utils.TextConstants

class FrequencyService(private val db: AppDatabase) {
    suspend fun prepopulate() {
        val frequencyDao = db.purchaseFrequencyDao()
        frequencyDao.deleteAll()
        frequencyDao.insertAll(
            PurchaseFrequency(name = TextConstants.FREQUENCY_WEEKLY),
            PurchaseFrequency(name = TextConstants.FREQUENCY_FORTNIGHTLY),
            PurchaseFrequency(name = TextConstants.FREQUENCY_MONTHLY),
            PurchaseFrequency(name = TextConstants.FREQUENCY_BIMONTHLY),
            PurchaseFrequency(name = TextConstants.FREQUENCY_QUARTERLY),
            PurchaseFrequency(name = TextConstants.FREQUENCY_FOUR_MONTHLY),
            PurchaseFrequency(name = TextConstants.FREQUENCY_SEMIANNUAL)
        )
    }
}