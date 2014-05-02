package net.tigerparents.nut.nutritioninfo;

/**
 * Created by piaw on 5/1/2014.
 */
public class FoodNutrientInfo {
    String _foodname;
    String _nutrient_name;
    double _nutrient_value;
    String _nutrient_unit;

    public FoodNutrientInfo(String foodname, String nutrient_name, double nutrient_value,
                            String nutrient_unit) {
        this._foodname = foodname;
        this._nutrient_name = nutrient_name;
        this._nutrient_value = nutrient_value;
        this._nutrient_unit = nutrient_unit;
    }

    public String get_foodname() {
        return _foodname;
    }

    public String get_nutrient_name() {
        return _nutrient_name;
    }

    public double get_nutrient_value() {
        return _nutrient_value;
    }

    public String get_nutrient_unit() {
        return _nutrient_unit;
    }
}
