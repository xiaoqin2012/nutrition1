package net.tigerparents.nut;

import java.util.ArrayList;

/**
 * Created by xiaoqin on 4/10/2014.
 */
public class NutritionData {
    public static NutritionData getNutritionDataFor(String food_name, int weightInOunces) {
        return new NutritionData();
    }

    public ArrayList<NutritionInformation> getNutritionInformation() {
        ArrayList<NutritionInformation> info = new ArrayList<NutritionInformation>();
        NutritionInformation ni = new NutritionInformation("Dummy", 0.0, 0.0);
        info.add(ni);
        return info;
    }

    public double getCalories() {
        return 0;
    }

    class NutritionInformation {
        String nutritionDescription;
        double weightValue;
        double percentageFDA;

        NutritionInformation(String nutritionDescription, double weightValue,
                             double percentageFDA) {
            this.nutritionDescription = nutritionDescription;
            this.weightValue = weightValue;
            this.percentageFDA = percentageFDA;
        }

        public String getNutritionDescription() {
            return nutritionDescription;
        }

        public void setNutritionDescription(String nutritionDescription) {
            this.nutritionDescription = nutritionDescription;
        }

        public double getWeightValue() {
            return weightValue;
        }

        public void setWeightValue(double weightValue) {
            this.weightValue = weightValue;
        }

        public double getPercentageFDA() {
            return percentageFDA;
        }

        public void setPercentageFDA(double percentageFDA) {
            this.percentageFDA = percentageFDA;
        }
    }
}
