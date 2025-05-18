package com.estebancoloradogonzalez.listify.model.database.service

import com.estebancoloradogonzalez.listify.model.database.AppDatabase
import com.estebancoloradogonzalez.listify.model.entity.Category
import com.estebancoloradogonzalez.listify.utils.TextConstants

class CategoryService(private val db: AppDatabase) {
    suspend fun prepopulate() {
        val categoryDao = db.categoryDao()
        categoryDao.deleteAll()
        categoryDao.insertAll(
            Category(name = TextConstants.FRUITS),
            Category(name = TextConstants.VEGETABLES),
            Category(name = TextConstants.SPICES_AND_CONDIMENTS),
            Category(name = TextConstants.BEVERAGES),
            Category(name = TextConstants.GRAINS_AND_CEREALS),
            Category(name = TextConstants.MEATS_AND_PROTEINS),
            Category(name = TextConstants.DAIRY_AND_DERIVATIVES),
            Category(name = TextConstants.BAKERY),
            Category(name = TextConstants.SAUCES_AND_DRESSINGS),
            Category(name = TextConstants.SWEETENERS),
            Category(name = TextConstants.OILS_AND_FATS),
            Category(name = TextConstants.SNACKS),
            Category(name = TextConstants.CLEANING_AND_HOME),
            Category(name = TextConstants.PETS),
            Category(name = TextConstants.OTHERS),
        )
    }
}