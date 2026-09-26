package com.example.smartpantrymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PantryAdapter extends ArrayAdapter<PantryItem> {

    public interface OnPantryItemActionListener {
        void onEdit(PantryItem item);

        void onDelete(PantryItem item);
    }

    private final Context context;
    private final ArrayList<PantryItem> pantryItems;
    private final OnPantryItemActionListener listener;

    public PantryAdapter(
            Context context,
            ArrayList<PantryItem> pantryItems,
            OnPantryItemActionListener listener
    ) {
        super(context, 0, pantryItems);

        this.context = context;
        this.pantryItems = pantryItems;
        this.listener = listener;
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
                    .inflate(R.layout.item_pantry, parent, false);
        }

        PantryItem item = pantryItems.get(position);

        TextView textIngredientName =
                convertView.findViewById(R.id.textIngredientName);

        TextView textQuantity =
                convertView.findViewById(R.id.textQuantity);

        TextView textExpiryDate =
                convertView.findViewById(R.id.textExpiryDate);

        Button btnEditItem =
                convertView.findViewById(R.id.btnEditItem);

        Button btnDeleteItem =
                convertView.findViewById(R.id.btnDeleteItem);

        textIngredientName.setText("♡  " + item.getName());

        textQuantity.setText(
                formatQuantity(item.getQuantity())
                        + " "
                        + item.getUnit()
        );

        String expiryDate = item.getExpiryDate();

        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            textExpiryDate.setText("No expiry date");
        } else {
            textExpiryDate.setText("Expires: " + expiryDate);
        }

        btnEditItem.setOnClickListener(v ->
                listener.onEdit(item)
        );

        btnDeleteItem.setOnClickListener(v ->
                listener.onDelete(item)
        );

        return convertView;
    }

    private String formatQuantity(double quantity) {

        if (quantity == Math.floor(quantity)) {
            return String.valueOf((int) quantity);
        }

        return String.valueOf(quantity);
    }
}

