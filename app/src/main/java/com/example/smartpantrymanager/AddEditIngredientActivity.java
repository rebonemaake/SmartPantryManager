package com.example.smartpantrymanager;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText editIngredientName;
    private EditText editQuantity;
    private EditText editUnit;
    private EditText editExpiryDate;
    private Button btnSaveIngredient;
    private Button btnCancel;
    private TextView formTitle;

    private DatabaseHelper databaseHelper;

    private int itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        editIngredientName = findViewById(R.id.editIngredientName);
        editQuantity = findViewById(R.id.editQuantity);
        editUnit = findViewById(R.id.editUnit);
        editExpiryDate = findViewById(R.id.editExpiryDate);
        btnSaveIngredient = findViewById(R.id.btnSaveIngredient);
        btnCancel = findViewById(R.id.btnCancel);
        formTitle = findViewById(R.id.formTitle);

        databaseHelper = new DatabaseHelper(this);

        itemId = getIntent().getIntExtra("item_id", -1);

        if (itemId != -1) {
            loadExistingItem();
        }

        btnSaveIngredient.setOnClickListener(v -> saveIngredient());

        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadExistingItem() {

        PantryItem item = databaseHelper.getPantryItemById(itemId);

        if (item != null) {
            formTitle.setText("Edit Ingredient ♡");

            editIngredientName.setText(item.getName());
            editQuantity.setText(String.valueOf(item.getQuantity()));
            editUnit.setText(item.getUnit());
            editExpiryDate.setText(item.getExpiryDate());

            btnSaveIngredient.setText("UPDATE INGREDIENT");
        }
    }

    private void saveIngredient() {

        String name = editIngredientName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();
        String unit = editUnit.getText().toString().trim();
        String expiryDate = editExpiryDate.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editIngredientName.setError("Please enter an ingredient name");
            editIngredientName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(quantityText)) {
            editQuantity.setError("Please enter a quantity");
            editQuantity.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(unit)) {
            editUnit.setError("Please enter a unit");
            editUnit.requestFocus();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            editQuantity.setError("Enter a valid number");
            editQuantity.requestFocus();
            return;
        }

        if (quantity <= 0) {
            editQuantity.setError("Quantity must be greater than 0");
            editQuantity.requestFocus();
            return;
        }

        if (itemId == -1) {

            PantryItem newItem = new PantryItem(
                    name,
                    quantity,
                    unit,
                    expiryDate
            );

            long result = databaseHelper.addPantryItem(newItem);

            if (result != -1) {
                Toast.makeText(
                        this,
                        "Ingredient added ♡",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            } else {
                Toast.makeText(
                        this,
                        "Could not save ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } else {

            PantryItem updatedItem = new PantryItem(
                    itemId,
                    name,
                    quantity,
                    unit,
                    expiryDate
            );

            int result = databaseHelper.updatePantryItem(updatedItem);

            if (result > 0) {
                Toast.makeText(
                        this,
                        "Ingredient updated ♡",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            } else {
                Toast.makeText(
                        this,
                        "Could not update ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}