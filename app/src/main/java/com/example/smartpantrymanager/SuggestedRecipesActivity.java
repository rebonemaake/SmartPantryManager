package com.example.smartpantrymanager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
public class SuggestedRecipesActivity extends AppCompatActivity {
    private ListView listSuggestedRecipes;
    private TextView textNoRecipes;
    private Button btnBackToPantry;
    private DatabaseHelper databaseHelper;
    private RecipeAdapter recipeAdapter;
    private ArrayList<Recipe> suggestedRecipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        listSuggestedRecipes = findViewById(R.id.listSuggestedRecipes);
        textNoRecipes = findViewById(R.id.textNoRecipes);
        btnBackToPantry = findViewById(R.id.btnBackToPantry);
        databaseHelper = new DatabaseHelper(this);
        suggestedRecipes = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(
                this,
                suggestedRecipes
        );
        listSuggestedRecipes.setAdapter(recipeAdapter);
        listSuggestedRecipes.setOnItemClickListener(
                (parent, view, position, id) -> {
                    Recipe selectedRecipe =
                            suggestedRecipes.get(position);
                    Intent intent = new Intent(
                            SuggestedRecipesActivity.this,
                            RecipeDetailActivity.class
                    );
                    intent.putExtra(
                            "recipe_id",
                            selectedRecipe.getId()
                    );
                    startActivity(intent);
                }
        );
        btnBackToPantry.setOnClickListener(v -> finish());
        loadSuggestedRecipes();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (databaseHelper != null) {
            loadSuggestedRecipes();
        }
    }
    private void loadSuggestedRecipes() {
        suggestedRecipes.clear();
        ArrayList<PantryItem> pantryItems =
                databaseHelper.getAllPantryItems();
        ArrayList<Recipe> allRecipes =
                databaseHelper.getAllRecipes();
        for (Recipe recipe : allRecipes) {
            ArrayList<RecipeIngredient> requiredIngredients =
                    databaseHelper.getIngredientsForRecipe(
                            recipe.getId()
                    );
            boolean canMakeRecipe =
                    IngredientMatcher.canMakeRecipe(
                            recipe,
                            requiredIngredients,
                            pantryItems
                    );
            if (canMakeRecipe) {
                suggestedRecipes.add(recipe);
            }
        }
        recipeAdapter.notifyDataSetChanged();
        updateRecipeEmptyState(pantryItems);
    }
    private void updateRecipeEmptyState(
            ArrayList<PantryItem> pantryItems) {
        if (suggestedRecipes.isEmpty()) {
            textNoRecipes.setVisibility(View.VISIBLE);
            listSuggestedRecipes.setVisibility(View.GONE);
            if (pantryItems.isEmpty()) {
                textNoRecipes.setText(
                        "Your pantry is empty ♡\n" +
                                "Add some ingredients to get recipe suggestions."
                );
            } else {
                textNoRecipes.setText(
                        "No recipes available yet ♡\n" +
                                "Try adding more ingredients to your pantry."
                );
            }
        } else {
            textNoRecipes.setVisibility(View.GONE);
            listSuggestedRecipes.setVisibility(View.VISIBLE);
        }
    }
}