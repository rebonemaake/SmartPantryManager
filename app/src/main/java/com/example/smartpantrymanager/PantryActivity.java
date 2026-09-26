package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PantryActivity extends AppCompatActivity {

    private ListView listPantryItems;
    private TextView textEmptyPantry;
    private EditText searchPantry;

    private Button btnAddItem;
    private Button btnSuggestedRecipes;
    private Button btnSettings;

    private DatabaseHelper databaseHelper;
    private PantryAdapter pantryAdapter;

    private ArrayList<PantryItem> allPantryItems;
    private ArrayList<PantryItem> displayedPantryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        listPantryItems = findViewById(R.id.listPantryItems);
        textEmptyPantry = findViewById(R.id.textEmptyPantry);
        searchPantry = findViewById(R.id.searchPantry);

        btnAddItem = findViewById(R.id.btnAddItem);
        btnSuggestedRecipes = findViewById(R.id.btnSuggestedRecipes);
        btnSettings = findViewById(R.id.btnSettings);

        databaseHelper = new DatabaseHelper(this);

        allPantryItems = new ArrayList<>();
        displayedPantryItems = new ArrayList<>();

        pantryAdapter = new PantryAdapter(
                this,
                displayedPantryItems,
                new PantryAdapter.OnPantryItemActionListener() {

                    @Override
                    public void onEdit(PantryItem item) {
                        openEditIngredient(item);
                    }

                    @Override
                    public void onDelete(PantryItem item) {
                        confirmDelete(item);
                    }
                }
        );

        listPantryItems.setAdapter(pantryAdapter);

        btnAddItem.setOnClickListener(v -> {

            Intent intent = new Intent(
                    PantryActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });

        btnSuggestedRecipes.setOnClickListener(v -> {

            Intent intent = new Intent(
                    PantryActivity.this,
                    SuggestedRecipesActivity.class
            );

            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {

            Intent intent = new Intent(
                    PantryActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);
        });

        searchPantry.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after
            ) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count
            ) {
                filterPantry(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        loadPantryItems();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {
            loadPantryItems();
        }
    }

    private void loadPantryItems() {

        allPantryItems.clear();

        allPantryItems.addAll(
                databaseHelper.getAllPantryItems()
        );

        filterPantry(
                searchPantry.getText().toString()
        );
    }

    private void filterPantry(String searchText) {

        displayedPantryItems.clear();

        String search =
                searchText.trim().toLowerCase();

        if (search.isEmpty()) {

            displayedPantryItems.addAll(
                    allPantryItems
            );

        } else {

            for (PantryItem item : allPantryItems) {

                if (item.getName()
                        .toLowerCase()
                        .contains(search)) {

                    displayedPantryItems.add(item);
                }
            }
        }

        pantryAdapter.notifyDataSetChanged();

        updateEmptyMessage();
    }

    private void updateEmptyMessage() {

        if (displayedPantryItems.isEmpty()) {

            textEmptyPantry.setVisibility(View.VISIBLE);
            listPantryItems.setVisibility(View.GONE);

        } else {

            textEmptyPantry.setVisibility(View.GONE);
            listPantryItems.setVisibility(View.VISIBLE);
        }
    }

    private void openEditIngredient(PantryItem item) {

        Intent intent = new Intent(
                PantryActivity.this,
                AddEditIngredientActivity.class
        );

        intent.putExtra(
                "item_id",
                item.getId()
        );

        startActivity(intent);
    }

    private void confirmDelete(PantryItem item) {

        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient?")
                .setMessage(
                        "Remove "
                                + item.getName()
                                + " from your pantry?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) ->
                                deleteIngredient(item)
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .show();
    }

    private void deleteIngredient(PantryItem item) {

        int result =
                databaseHelper.deletePantryItem(
                        item.getId()
                );

        if (result > 0) {

            Toast.makeText(
                    this,
                    "Ingredient deleted ♡",
                    Toast.LENGTH_SHORT
            ).show();

            loadPantryItems();

        } else {

            Toast.makeText(
                    this,
                    "Could not delete ingredient",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}