package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 2;

    // Pantry table
    private static final String TABLE_PANTRY = "pantry_items";
    private static final String PANTRY_ID = "id";
    private static final String PANTRY_NAME = "name";
    private static final String PANTRY_QUANTITY = "quantity";
    private static final String PANTRY_UNIT = "unit";
    private static final String PANTRY_EXPIRY = "expiry_date";

    // Recipe table
    private static final String TABLE_RECIPES = "recipes";
    private static final String RECIPE_ID = "id";
    private static final String RECIPE_NAME = "name";
    private static final String RECIPE_INSTRUCTIONS = "instructions";

    // Recipe ingredients table
    private static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    private static final String RECIPE_INGREDIENT_ID = "id";
    private static final String RECIPE_INGREDIENT_RECIPE_ID = "recipe_id";
    private static final String RECIPE_INGREDIENT_NAME = "ingredient_name";
    private static final String RECIPE_INGREDIENT_QUANTITY = "quantity";
    private static final String RECIPE_INGREDIENT_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createPantryTable =
                "CREATE TABLE " + TABLE_PANTRY + " (" +
                        PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PANTRY_NAME + " TEXT NOT NULL, " +
                        PANTRY_QUANTITY + " REAL NOT NULL, " +
                        PANTRY_UNIT + " TEXT NOT NULL, " +
                        PANTRY_EXPIRY + " TEXT" +
                        ")";

        String createRecipesTable =
                "CREATE TABLE " + TABLE_RECIPES + " (" +
                        RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RECIPE_NAME + " TEXT NOT NULL, " +
                        RECIPE_INSTRUCTIONS + " TEXT NOT NULL" +
                        ")";

        String createRecipeIngredientsTable =
                "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                        RECIPE_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RECIPE_INGREDIENT_RECIPE_ID + " INTEGER NOT NULL, " +
                        RECIPE_INGREDIENT_NAME + " TEXT NOT NULL, " +
                        RECIPE_INGREDIENT_QUANTITY + " REAL NOT NULL, " +
                        RECIPE_INGREDIENT_UNIT + " TEXT NOT NULL, " +
                        "FOREIGN KEY(" + RECIPE_INGREDIENT_RECIPE_ID + ") REFERENCES " +
                        TABLE_RECIPES + "(" + RECIPE_ID + ")" +
                        ")";

        db.execSQL(createPantryTable);
        db.execSQL(createRecipesTable);
        db.execSQL(createRecipeIngredientsTable);

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);

        onCreate(db);
    }

    // PANTRY CRUD

    public long addPantryItem(PantryItem item) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PANTRY_NAME, item.getName());
        values.put(PANTRY_QUANTITY, item.getQuantity());
        values.put(PANTRY_UNIT, item.getUnit());
        values.put(PANTRY_EXPIRY, item.getExpiryDate());

        long result = db.insert(TABLE_PANTRY, null, values);

        db.close();

        return result;
    }

    public ArrayList<PantryItem> getAllPantryItems() {

        ArrayList<PantryItem> pantryItems = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                PANTRY_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(PANTRY_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(PANTRY_NAME)
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(PANTRY_QUANTITY)
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(PANTRY_UNIT)
                );

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(PANTRY_EXPIRY)
                );

                PantryItem item = new PantryItem(
                        id,
                        name,
                        quantity,
                        unit,
                        expiryDate
                );

                pantryItems.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return pantryItems;
    }

    public PantryItem getPantryItemById(int itemId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                PANTRY_ID + " = ?",
                new String[]{String.valueOf(itemId)},
                null,
                null,
                null
        );

        PantryItem item = null;

        if (cursor.moveToFirst()) {

            item = new PantryItem(
                    cursor.getInt(cursor.getColumnIndexOrThrow(PANTRY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PANTRY_NAME)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(PANTRY_QUANTITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PANTRY_UNIT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PANTRY_EXPIRY))
            );
        }

        cursor.close();
        db.close();

        return item;
    }

    public int updatePantryItem(PantryItem item) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PANTRY_NAME, item.getName());
        values.put(PANTRY_QUANTITY, item.getQuantity());
        values.put(PANTRY_UNIT, item.getUnit());
        values.put(PANTRY_EXPIRY, item.getExpiryDate());

        int result = db.update(
                TABLE_PANTRY,
                values,
                PANTRY_ID + " = ?",
                new String[]{String.valueOf(item.getId())}
        );

        db.close();

        return result;
    }

    public int deletePantryItem(int itemId) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                TABLE_PANTRY,
                PANTRY_ID + " = ?",
                new String[]{String.valueOf(itemId)}
        );

        db.close();

        return result;
    }

    //  RECIPES

    public ArrayList<Recipe> getAllRecipes() {

        ArrayList<Recipe> recipes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                RECIPE_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {

                Recipe recipe = new Recipe(
                        cursor.getInt(cursor.getColumnIndexOrThrow(RECIPE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_INSTRUCTIONS))
                );

                recipes.add(recipe);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recipes;
    }

    public Recipe getRecipeById(int recipeId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPES,
                null,
                RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                null
        );

        Recipe recipe = null;

        if (cursor.moveToFirst()) {

            recipe = new Recipe(
                    cursor.getInt(cursor.getColumnIndexOrThrow(RECIPE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_INSTRUCTIONS))
            );
        }

        cursor.close();
        db.close();

        return recipe;
    }

    public ArrayList<RecipeIngredient> getIngredientsForRecipe(int recipeId) {

        ArrayList<RecipeIngredient> ingredients = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPE_INGREDIENTS,
                null,
                RECIPE_INGREDIENT_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                RECIPE_INGREDIENT_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {

                RecipeIngredient ingredient = new RecipeIngredient(
                        cursor.getInt(cursor.getColumnIndexOrThrow(RECIPE_INGREDIENT_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(RECIPE_INGREDIENT_RECIPE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_INGREDIENT_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(RECIPE_INGREDIENT_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_INGREDIENT_UNIT))
                );

                ingredients.add(ingredient);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return ingredients;
    }

    //  SEED DATA

    private void seedRecipes(SQLiteDatabase db) {

        long oatsId = insertRecipe(
                db,
                "Creamy Oats",
                "1. Add oats and milk to a pot.\n2. Cook over medium heat.\n3. Stir until creamy.\n4. Serve warm."
        );
        addRecipeIngredient(db, oatsId, "oats", 50, "grams");
        addRecipeIngredient(db, oatsId, "milk", 250, "ml");

        long scrambledEggsId = insertRecipe(
                db,
                "Scrambled Eggs",
                "1. Crack the eggs into a bowl.\n2. Whisk them well.\n3. Heat butter in a pan.\n4. Cook the eggs while stirring gently."
        );
        addRecipeIngredient(db, scrambledEggsId, "eggs", 2, "pieces");
        addRecipeIngredient(db, scrambledEggsId, "butter", 10, "grams");

        long tomatoEggsId = insertRecipe(
                db,
                "Tomato Eggs",
                "1. Chop the tomatoes.\n2. Cook tomatoes in a pan.\n3. Add beaten eggs.\n4. Stir until cooked."
        );
        addRecipeIngredient(db, tomatoEggsId, "tomatoes", 2, "pieces");
        addRecipeIngredient(db, tomatoEggsId, "eggs", 2, "pieces");

        long cheeseToastId = insertRecipe(
                db,
                "Cheese Toast",
                "1. Place cheese on bread.\n2. Toast until the bread is golden and the cheese has melted."
        );
        addRecipeIngredient(db, cheeseToastId, "bread", 2, "slices");
        addRecipeIngredient(db, cheeseToastId, "cheese", 50, "grams");

        long boiledEggsId = insertRecipe(
                db,
                "Boiled Eggs",
                "1. Place eggs in a pot of water.\n2. Bring to a boil.\n3. Cook for 8 to 10 minutes.\n4. Cool and peel."
        );
        addRecipeIngredient(db, boiledEggsId, "eggs", 2, "pieces");

        long tomatoToastId = insertRecipe(
                db,
                "Tomato Toast",
                "1. Toast the bread.\n2. Slice the tomato.\n3. Place tomato slices on the toast.\n4. Serve."
        );
        addRecipeIngredient(db, tomatoToastId, "bread", 2, "slices");
        addRecipeIngredient(db, tomatoToastId, "tomatoes", 1, "piece");

        long bananaOatsId = insertRecipe(
                db,
                "Banana Oats",
                "1. Cook oats with milk.\n2. Slice the banana.\n3. Add banana on top and serve."
        );
        addRecipeIngredient(db, bananaOatsId, "oats", 50, "grams");
        addRecipeIngredient(db, bananaOatsId, "milk", 250, "ml");
        addRecipeIngredient(db, bananaOatsId, "banana", 1, "piece");

        long eggSandwichId = insertRecipe(
                db,
                "Egg Sandwich",
                "1. Cook the eggs.\n2. Place them between slices of bread.\n3. Serve warm."
        );
        addRecipeIngredient(db, eggSandwichId, "eggs", 2, "pieces");
        addRecipeIngredient(db, eggSandwichId, "bread", 2, "slices");

        long cheeseEggsId = insertRecipe(
                db,
                "Cheesy Eggs",
                "1. Beat the eggs.\n2. Cook them in a pan.\n3. Add cheese and stir until melted."
        );
        addRecipeIngredient(db, cheeseEggsId, "eggs", 2, "pieces");
        addRecipeIngredient(db, cheeseEggsId, "cheese", 40, "grams");

        long tomatoCheeseToastId = insertRecipe(
                db,
                "Tomato Cheese Toast",
                "1. Put tomato and cheese on bread.\n2. Toast until the cheese melts."
        );
        addRecipeIngredient(db, tomatoCheeseToastId, "bread", 2, "slices");
        addRecipeIngredient(db, tomatoCheeseToastId, "tomatoes", 1, "piece");
        addRecipeIngredient(db, tomatoCheeseToastId, "cheese", 40, "grams");

        long butterToastId = insertRecipe(
                db,
                "Butter Toast",
                "1. Toast the bread.\n2. Spread butter over the warm toast."
        );
        addRecipeIngredient(db, butterToastId, "bread", 2, "slices");
        addRecipeIngredient(db, butterToastId, "butter", 10, "grams");

        long bananaMilkId = insertRecipe(
                db,
                "Banana Milk",
                "1. Add banana and milk to a blender.\n2. Blend until smooth.\n3. Serve immediately."
        );
        addRecipeIngredient(db, bananaMilkId, "banana", 1, "piece");
        addRecipeIngredient(db, bananaMilkId, "milk", 250, "ml");

        long cheeseSandwichId = insertRecipe(
                db,
                "Cheese Sandwich",
                "1. Place cheese between two slices of bread.\n2. Serve cold or toast it."
        );
        addRecipeIngredient(db, cheeseSandwichId, "bread", 2, "slices");
        addRecipeIngredient(db, cheeseSandwichId, "cheese", 50, "grams");

        long eggToastId = insertRecipe(
                db,
                "Egg on Toast",
                "1. Toast the bread.\n2. Cook the egg.\n3. Place the egg on the toast."
        );
        addRecipeIngredient(db, eggToastId, "bread", 1, "slice");
        addRecipeIngredient(db, eggToastId, "eggs", 1, "piece");

        long tomatoSaladId = insertRecipe(
                db,
                "Simple Tomato Salad",
                "1. Slice the tomatoes.\n2. Place them in a bowl.\n3. Serve fresh."
        );
        addRecipeIngredient(db, tomatoSaladId, "tomatoes", 2, "pieces");
    }

    private long insertRecipe(
            SQLiteDatabase db,
            String name,
            String instructions
    ) {

        ContentValues values = new ContentValues();
        values.put(RECIPE_NAME, name);
        values.put(RECIPE_INSTRUCTIONS, instructions);

        return db.insert(
                TABLE_RECIPES,
                null,
                values
        );
    }

    private void addRecipeIngredient(
            SQLiteDatabase db,
            long recipeId,
            String ingredientName,
            double quantity,
            String unit
    ) {

        ContentValues values = new ContentValues();

        values.put(
                RECIPE_INGREDIENT_RECIPE_ID,
                recipeId
        );

        values.put(
                RECIPE_INGREDIENT_NAME,
                ingredientName
        );

        values.put(
                RECIPE_INGREDIENT_QUANTITY,
                quantity
        );

        values.put(
                RECIPE_INGREDIENT_UNIT,
                unit
        );

        db.insert(
                TABLE_RECIPE_INGREDIENTS,
                null,
                values
        );
    }
}