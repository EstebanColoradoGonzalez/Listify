package com.estebancoloradogonzalez.listify.model.database.prepopulate

import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.database.service.CategoryService
import com.estebancoloradogonzalez.listify.model.database.service.EstablishmentService
import com.estebancoloradogonzalez.listify.model.database.service.FrequencyService
import com.estebancoloradogonzalez.listify.model.database.service.ProductRegistrationService
import com.estebancoloradogonzalez.listify.model.database.service.ShoppingListGenerator
import com.estebancoloradogonzalez.listify.model.database.service.StateService
import com.estebancoloradogonzalez.listify.model.database.service.UnitService
import com.estebancoloradogonzalez.listify.model.database.service.UserRegistrationService

class PrepopulateFacade(db: AppDatabase) {
    private val stateService = StateService(db)
    private val unitService = UnitService(db)
    private val establishmentService = EstablishmentService(db)
    private val frequencyService = FrequencyService(db)
    private val categoryService = CategoryService(db)
    private val userRegistration = UserRegistrationService(db)
    private val productRegistration = ProductRegistrationService(db)
    private val shoppingListGenerator = ShoppingListGenerator(db)

    suspend fun prepopulateDatabase() {
        stateService.prepopulate()
        unitService.prepopulate()
        establishmentService.prepopulate()
        frequencyService.prepopulate()
        categoryService.prepopulate()
        registerTestData()
    }

    private suspend fun registerTestData() {
        val userId = userRegistration.registerTestUser()

        productRegistration.registerTestProducts(userId)
        shoppingListGenerator.generateTestShoppingLists(userId)
    }
}
