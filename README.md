Smart Pantry Manager

About the App

Smart Pantry Manager is an Android application I developed to help users keep track of the food items they have at home. The idea behind the app is to make it easier to know what is currently in the pantry, how much of each item is available, and when an item is going to expire.

The application also uses the ingredients stored in the pantry to provide recipe suggestions. This can help users decide what they can prepare using the ingredients they already have.

What the App Can Do

The current version of the application includes:

* Adding ingredients to the pantry
* Editing pantry ingredients
* Keeping track of quantities and units
* Adding expiry dates to pantry items
* Searching for pantry items
* Viewing stored pantry items
* Getting suggested recipes
* Viewing recipe details
* Saving pantry and recipe information locally

Database Choice

SQLite

I chose SQLite for the Smart Pantry Manager because the information used by the application can be stored directly on the device. The app mainly needs to keep track of local pantry and recipe information, so an online database was not necessary for the current version.

SQLite also works well with Android and allows the application to continue storing the user’s pantry information without depending on an internet connection.

The database is called smart_pantry.db. I used Android’s SQLiteOpenHelper class in DatabaseHelper.java to create and manage the database.

The database currently contains tables for information such as pantry items and recipes.

Technologies Used

* Java
* Android Studio
* XML
* SQLite
* Git
* GitHub

How to Run the Application

To run the Smart Pantry Manager project:

1. Download or clone the project from the public GitHub repository.
2. Open the project using Android Studio.
3. Allow Android Studio to complete the Gradle synchronisation.
4. Connect an Android phone or start an Android Emulator.
5. Select the device in Android Studio.
6. Build and run the application.
7. The application should then open on the selected Android device or emulator.

Important Project Files

Some of the main files used in the application are:

* MainActivity.java – handles the main screen.
* PantryActivity.java – displays the pantry items.
* AddEditIngredientActivity.java – used when adding or editing pantry items.
* SuggestedRecipesActivity.java – handles the recipe suggestion screen.
* RecipeDetailActivity.java – displays the details of a selected recipe.
* DatabaseHelper.java – manages the SQLite database.
* PantryAdapter.java – helps display pantry items in the application.
* RecipeAdapter.java – helps display recipes in the application.

Version Control

Git is being used throughout the development of the Smart Pantry Manager project. The project is stored in a public GitHub repository, and changes are being added through separate commits as different parts of the application are developed and improved.

This allows the development progress to be tracked instead of only uploading the completed application at the end.