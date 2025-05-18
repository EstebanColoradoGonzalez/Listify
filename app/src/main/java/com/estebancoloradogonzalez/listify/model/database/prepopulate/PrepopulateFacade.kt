package com.estebancoloradogonzalez.listify.model.database.prepopulate

import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.database.service.CategoryService
import com.estebancoloradogonzalez.listify.model.database.service.EstablishmentService
import com.estebancoloradogonzalez.listify.model.database.service.FrequencyService
import com.estebancoloradogonzalez.listify.model.database.service.StateService
import com.estebancoloradogonzalez.listify.model.database.service.UnitService

class PrepopulateFacade(db: AppDatabase) {
    private val stateService = StateService(db)
    private val unitService = UnitService(db)
    private val establishmentService = EstablishmentService(db)
    private val frequencyService = FrequencyService(db)
    private val categoryService = CategoryService(db)

    suspend fun prepopulateDatabase() {
        stateService.prepopulate()
        unitService.prepopulate()
        establishmentService.prepopulate()
        frequencyService.prepopulate()
        categoryService.prepopulate()
    }
}