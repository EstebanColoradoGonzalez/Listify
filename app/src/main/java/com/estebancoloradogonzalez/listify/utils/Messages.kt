package com.estebancoloradogonzalez.listify.utils

object Messages {
    const val ENTER_VALID_NAME_MESSAGE: String = "Ingrese un nombre válido (solo letras y espacios)."
    const val ENTER_VALID_BUDGET_MESSAGE: String = "Ingrese un presupuesto válido (número positivo con hasta dos decimales)."
    const val ENTER_VALID_PRICE_MESSAGE: String = "Ingrese un precio válido (número positivo con hasta dos decimales)."
    const val ENTER_VALID_QUANTITY_MESSAGE: String = "Ingrese una cantidad válida (número positivo con hasta dos decimales)."
    const val DATA_UPDATED_SUCCESSFULLY_MESSAGE: String = "Datos actualizados correctamente."
    const val ENTER_VALID_CATEGORY_NAME_MESSAGE: String = "Ingrese un nombre válido para la categoría (solo letras y espacios)."
    const val CATEGORY_NAME_TOO_LONG_MESSAGE: String = "El nombre de la categoría no puede tener mas de 100 caracteres."
    const val CATEGORY_NAME_ALREADY_EXISTS_MESSAGE: String = "Ya existe una categoría con ese nombre."
    const val BUDGET_EXCEEDED: String = "La cantidad y precio del producto están excediendo el presupuesto mensual del usuario."
    const val PRODUCT_ALREADY_EXISTS: String = "Ya existe un producto con ese nombre."
    const val ENTER_VALID_DATE_MESSAGE: String = "La fecha es obligatoría, ingrese una."
    const val DATE_IS_BEFORE_LAST_SHOPPING_LIST: String  = "La fecha no puede ser anterior o igual a la fecha de la ultima lista de compras generada, la cual es: "
    const val DATE_IS_NOT_SEVEN_DAYS_AFTER: String = "Aun no han pasado 7 días desde la ultima lista de compras"
    const val THERE_ARE_NOT_ACTIVE_PRODUCTS: String = "No hay productos activos registrados."
    const val THERE_IS_AN_ACTIVE_SHOPPING_LIST: String = "Todavia hay una lista de compras activa."
    const val NOT_SUPPORTED_FREQUENCY: String = "Frecuencia no soportada: "
    const val THERE_ARE_PRODUCTS_HAVE_NOT_BEEN_COMPLETED: String = "Hay productos dentro de la lista que no han sido completados o eliminados."
}