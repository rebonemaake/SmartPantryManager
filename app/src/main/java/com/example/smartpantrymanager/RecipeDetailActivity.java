package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView textRecipeDetailName;
    private TextView textRecipeIngredients;
    private TextView textRecipeMethod;
    private Button btnBackToRecipes;

    private DatabaseHelper databaseHelper;

    private int recipeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        textRecipeDetailName = findViewById(R.id.textRecipeDetailName);
        textRecipeIngredients = findViewById(R.id.textRecipeIngredients);
        textRecipeMethod = findViewById(R.id.textRecipeMethod);
        btnBackToRecipes = findViewById(R.id.btnBackToRecipes);

        databaseHelper = new DatabaseHelper(this);

        recipeId = getIntent().getIntExtra("recipe_id", -1);

        if (recipeId == -1) {
            Toast.makeText(
                    this,
                    "Recipe could not be loaded",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        loadRecipeDetails();

        btnBackToRecipes.setOnClickListener(v -> finish());
    }

    private void loadRecipeDetails() {

        Recipe recipe = databaseHelper.getRecipeById(recipeId);

        if (recipe == null) {
            Toast.makeText(
                    this,
                    "Recipe not found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        textRecipeDetailName.setText(
                recipe.getName() + " ♡"
        );

        textRecipeMethod.setText(
                recipe.getInstructions()
        );

        ArrayList<RecipeIngredient> ingredients =
                databaseHelper.getIngredientsForRecipe(recipeId);

        StringBuilder ingredientText =
                new StringBuilder();

        for (RecipeIngredient ingredient : ingredients) {

            ingredientText
                    .append("♡ ")
                    .append(formatQuantity(ingredient.getQuantity()))
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append(" ")
                    .append(ingredient.getIngredientName())
                    .append("\n");
        }

        textRecipeIngredients.setText(
                ingredientText.toString().trim()
        );
    }

    private String formatQuantity(double quantity) {

        if (quantity == Math.floor(quantity)) {
            return String.valueOf((int) quantity);
        }

        return String.valueOf(quantity);
    }
}