package com.example.smartpantrymanager;

import java.util.ArrayList;

public class IngredientMatcher {

    public static boolean canMakeRecipe(
            Recipe recipe,
            ArrayList<RecipeIngredient> requiredIngredients,
            ArrayList<PantryItem> pantryItems
    ) {

        for (RecipeIngredient required : requiredIngredients) {

            PantryItem matchingPantryItem =
                    findMatchingPantryItem(
                            required,
                            pantryItems
                    );

            if (matchingPantryItem == null) {
                return false;
            }

            String pantryUnitType =
                    getUnitType(matchingPantryItem.getUnit());

            String requiredUnitType =
                    getUnitType(required.getUnit());

            if (!pantryUnitType.equals(requiredUnitType)) {
                return false;
            }

            double pantryQuantity =
                    convertQuantityToBaseUnit(
                            matchingPantryItem.getQuantity(),
                            matchingPantryItem.getUnit()
                    );

            double requiredQuantity =
                    convertQuantityToBaseUnit(
                            required.getQuantity(),
                            required.getUnit()
                    );

            if (pantryQuantity < requiredQuantity) {
                return false;
            }
        }

        return true;
    }

    private static PantryItem findMatchingPantryItem(
            RecipeIngredient required,
            ArrayList<PantryItem> pantryItems
    ) {

        String requiredName =
                normalizeIngredientName(
                        required.getIngredientName()
                );

        for (PantryItem pantryItem : pantryItems) {

            String pantryName =
                    normalizeIngredientName(
                            pantryItem.getName()
                    );

            if (pantryName.equals(requiredName)) {
                return pantryItem;
            }
        }

        return null;
    }

    private static String normalizeIngredientName(
            String ingredientName
    ) {

        if (ingredientName == null) {
            return "";
        }

        String normalized =
                ingredientName
                        .trim()
                        .toLowerCase();

        if (normalized.endsWith("ies")
                && normalized.length() > 3) {

            normalized =
                    normalized.substring(
                            0,
                            normalized.length() - 3
                    ) + "y";

        } else if (normalized.endsWith("oes")
                && normalized.length() > 3) {

            normalized =
                    normalized.substring(
                            0,
                            normalized.length() - 2
                    );

        } else if (normalized.endsWith("s")
                && normalized.length() > 1) {

            normalized =
                    normalized.substring(
                            0,
                            normalized.length() - 1
                    );
        }

        return normalized;
    }

    private static String normalizeUnit(String unit) {

        if (unit == null) {
            return "";
        }

        return unit
                .trim()
                .toLowerCase();
    }

    private static String getUnitType(String unit) {

        String normalized = normalizeUnit(unit);

        // Mass
        if (normalized.equals("g")
                || normalized.equals("gram")
                || normalized.equals("grams")
                || normalized.equals("kg")
                || normalized.equals("kilogram")
                || normalized.equals("kilograms")) {

            return "mass";
        }

        // Volume
        if (normalized.equals("ml")
                || normalized.equals("millilitre")
                || normalized.equals("millilitres")
                || normalized.equals("milliliter")
                || normalized.equals("milliliters")
                || normalized.equals("l")
                || normalized.equals("litre")
                || normalized.equals("litres")
                || normalized.equals("liter")
                || normalized.equals("liters")) {

            return "volume";
        }

        // Countable food items
        if (normalized.equals("piece")
                || normalized.equals("pieces")
                || normalized.equals("pc")
                || normalized.equals("pcs")
                || normalized.equals("slice")
                || normalized.equals("slices")
                || normalized.equals("unit")
                || normalized.equals("units")) {

            return "count";
        }

        return normalized;
    }

    private static double convertQuantityToBaseUnit(
            double quantity,
            String unit
    ) {

        String normalized = normalizeUnit(unit);

        // kilograms -> grams
        if (normalized.equals("kg")
                || normalized.equals("kilogram")
                || normalized.equals("kilograms")) {

            return quantity * 1000;
        }

        // litres -> millilitres
        if (normalized.equals("l")
                || normalized.equals("litre")
                || normalized.equals("litres")
                || normalized.equals("liter")
                || normalized.equals("liters")) {

            return quantity * 1000;
        }

        return quantity;
    }
}