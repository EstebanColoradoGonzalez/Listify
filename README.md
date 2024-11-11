# Listify

**Listify** es una aplicación para gestionar las compras personales y optimizar el consumo de productos, permitiendo al usuario mantener un control de frecuencia de compras, actualización de precios, y predicciones de consumo. Además, se integra con Google Calendar para automatizar las fechas de compra y ofrece un presupuesto mensual detallado por categorías. Este proyecto está construido con **Java 21** y **Spring Boot 3**, siguiendo una arquitectura hexagonal para facilitar la escalabilidad y el mantenimiento del sistema.

## Tabla de Contenido
- [Descripción del Proyecto](#descripción-del-proyecto)
- [Características Principales](#características-principales)
- [Instalación](#instalación)
- [Uso](#uso)
- [Contribuir](#contribuir)
- [Licencia](#licencia)
- [Extensiones Recomendadas para VSCode](#extensiones-recomendadas-para-vscode)
- [Screenshots](#screenshots)

## Descripción del Proyecto

Listify ayuda a los usuarios a gestionar sus compras mediante una lista dinámica y personalizada que se ajusta según los patrones de consumo y las necesidades del usuario. Los productos se categorizan por frecuencia de compra y se ajustan automáticamente basándose en el consumo histórico. Además, Listify proporciona un presupuesto mensual detallado y organiza las compras por establecimiento, facilitando la administración de las compras diarias.

## Características Principales

1. **Frecuencia de Compras y Registro de Productos**: Ajuste automático de las frecuencias de compra.
2. **Flexibilidad de los Productos**: Activación/desactivación de productos para futuras compras.
3. **Estimaciones de Consumo**: Algoritmo de aprendizaje que ajusta la lista de compras según patrones históricos de consumo.
4. **Actualización de Precios**: Ajuste de precios y cantidades después de cada compra.
5. **Integración con Google Calendar**: Programación automática de fechas de compra.
6. **Gestión de Cambios en el Consumo**: Ajustes en las listas futuras según duración y uso real de productos.
7. **Presupuesto Mensual**: Desglose detallado de gastos por categoría.
8. **Distribución de Productos por Establecimientos**: Organización de productos por tienda o establecimiento habitual.

## Instalación

Para instalar el proyecto localmente:

1. Clona el repositorio:
    ```bash
    git clone https://github.com/estebancoloradogonzalez/listify.git
    ```

2. Entra en el directorio del proyecto:
    ```bash
    cd listify
    ```

3. Instala las dependencias de Java y Spring Boot ejecutando:
    ```bash
    ./gradlew build
    ```

4. Ejecuta el servidor:
    ```bash
    ./gradlew bootRun
    ```

## Uso

Para utilizar Listify:

1. Abre la aplicación en `http://localhost:8080`.
2. Registra productos y establece su frecuencia de compra inicial.
3. Conecta Google Calendar en la configuración para programar las fechas de compra.
4. Al final de cada compra, ajusta precios y cantidades para actualizar las listas futuras.

## Contribuir

Si deseas contribuir al proyecto:

1. Realiza un fork del repositorio.
2. Crea una nueva rama con la característica o corrección que deseas agregar:
    ```bash
    git checkout -b nombre-de-la-funcion
    ```
3. Realiza tus cambios y haz un commit:
    ```bash
    git commit -m "Descripción de los cambios"
    ```
4. Haz push a tu rama:
    ```bash
    git push origin nombre-de-la-funcion
    ```
5. Crea un Pull Request en el repositorio principal.

## Licencia

Este proyecto está bajo la licencia [MIT](LICENSE).