
# Listify

## 📌 Overview
Listify is a mobile application designed to optimize personal shopping management by providing an easy and efficient way to plan, record, and control household expenses. By automating shopping lists, tracking prices, and organizing products by store, Listify helps users make informed decisions and keep their budgets under control. With a practical and accessible approach, Listify aims to become a reliable tool for those who want to improve their shopping experience without complications, ensuring a balance between simplicity and functionality.
  
---  

## 🚀 Features

### **User Management**
- Users must enter their full name upon first accessing the application.
- Users must define a monthly shopping budget upon first accessing the application.
- Users can modify their full name at any time.
- Users can modify their monthly budget at any time.

### **Product Management**
- Users can register products with the following attributes:
    - Name
    - Current price
    - Unit of measurement
    - Usual purchase quantity
    - Store (*D1, Dollar City, Butcher Shop, Grocery Store, Others*)
    - Purchase frequency (*weekly, biweekly, monthly, bimonthly, quarterly, four-monthly, or semi-annually*)
    - Category (*created or selected by the user*)
- Users can modify the attributes of a registered product.
- Users can delete a registered product.
- Users can activate or deactivate products based on availability or necessity.
- The system alerts the user when the total cost of active products exceeds the monthly budget.
- Users can manage product categories with the following actions:
    - Create a new category if it does not exist.
    - Modify the name of an existing category.
    - Delete a category, applying a logical deletion to preserve historical records.

### **Shopping List Generation**
- Users can generate a shopping list based on the following criteria:
    - Only include products in the "Active" state.
    - Consider the purchase frequency established for each product.
    - Review the purchase history to avoid duplicates based on purchase frequency.
- Users can modify the generated shopping lists with the following actions:
    - Add additional products.
    - Remove included products.
    - Modify the quantity of a product.
    - Modify the price of a product.
    - Modify the designated shopping day.
- The system automatically subdivides the shopping list by store.
- When the user modifies the price or quantity of a product in the latest generated shopping list, the system updates the current price and quantity of the registered product.
- If the user modifies the price or quantity of a product in a list prior to the latest generated one, the system does not update the current price and quantity of the product.

### **History & Reports**
- Users can view the history of completed purchases.
- The shopping history displays the following data:
    - Purchase date
    - Purchased products with their quantity and price at that time
    - Total amount spent on the purchase

---  

## 📂 Project Structure

```  
listify/  
│-- app/  
│   │-- src/  
│   │   │-- main/  
│   │   │   │-- java/com/estebancoloradogonzalez/listify/  
│   │   │   │   │-- view/ (Activities & UI components)  
│   │   │   │   │-- viewmodel/ (ViewModels for managing UI logic)  
│   │   │   │   │-- model/  
│   │   │   │   │   │-- entity/ (Database entities)  
│   │   │   │   │   │-- database/ (Database access and DAO classes)  
│   │   │   │-- res/  
│   │   │   │   │-- layout/ (XML UI layouts)  
│   │   │   │   │-- drawable/ (Images and graphic resources)  
│   │-- build.gradle  
│   │-- AndroidManifest.xml  
│-- README.md  
```  
  
---  

## 🛠️ Tech Stack
- **Kotlin** - Primary programming language
- **Android Jetpack** - Modern Android components (ViewModel, LiveData, Room, etc.)
- **Room Database** - Local database for storing user and product information
- **MVVM Architecture** - Clean separation of concerns for better maintainability
- **Coroutines** - Asynchronous programming for better performance

---  

## 📌 Installation
1. Clone the repository:  
   ``sh  
  git clone https://github.com/EstebanColoradoGonzalez/Listify.git ``
2. Open the project in **Android Studio**.
3. Sync Gradle and install dependencies.
4. Run the project on an emulator or physical device.

---  

## 📜 License
This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.
  
---  

## 🤝 Contributing
Contributions are welcome! If you want to contribute:
1. Fork the repository.
2. Create a new branch (`feature/new-feature`).
3. Commit your changes.
4. Push to the branch.
5. Submit a Pull Request.

---  

## ✨ Author
**Esteban Colorado González**