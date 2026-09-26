package com.example.smartpantrymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RecipeAdapter extends ArrayAdapter<Recipe> {

    private final Context context;
    private final ArrayList<Recipe> recipes;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        super(context, 0, recipes);

        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public View getView(
            int position,
            @Nullable View convertView,
            @NonNull ViewGroup parent
    ) {

        if (convertView == null) {
            convertView = LayoutInflater
                    .from(context)
                    .inflate(R.layout.item_recipe, parent, false);
        }

        Recipe recipe = recipes.get(position);

        TextView textRecipeName =
                convertView.findViewById(R.id.textRecipeName);

        TextView textRecipeStatus =
                convertView.findViewById(R.id.textRecipeStatus);

        textRecipeName.setText(recipe.getName());
        textRecipeStatus.setText("You have everything ♡");

        return convertView;
    }
}