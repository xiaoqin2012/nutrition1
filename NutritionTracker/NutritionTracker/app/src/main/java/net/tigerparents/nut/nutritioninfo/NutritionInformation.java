package net.tigerparents.nut.nutritioninfo;

/**
 * Created by xiaoqin on 4/16/2014.
 */
public class NutritionInformation {
    String nutritionDescription;
    double weightValue;
    String weightUnit;
    double percentageFDA;
    double valueFDA;

    public NutritionInformation() {
        this.nutritionDescription = "No Data";
        this.weightValue = 0;
        this.weightUnit = "";
        this.percentageFDA = 0;
        this.valueFDA = 0;
    }

    NutritionInformation(String nutritionDescription, double weightValue, String weightUnit,
                         double valueFDA, double percentageFDA) {
        this.nutritionDescription = nutritionDescription;
        this.weightValue = weightValue;
        this.weightUnit = weightUnit;
        this.valueFDA = valueFDA;
        this.percentageFDA = percentageFDA;
    }

    public String getNutritionDescription() {
        return nutritionDescription;
    }

    public void setNutritionDescription(String nutritionDescription) {
        this.nutritionDescription = nutritionDescription;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public double getWeightValue() {
        return weightValue;
    }

    public void setWeightValue(double weightValue) {
        this.weightValue = weightValue;
    }

    public double getFDA() {
        return valueFDA;
    }

    public void setFDA(double valueFDA) {
        this.percentageFDA = valueFDA;
    }

    public double getPercentageFDA() {
        return percentageFDA;
    }

    public void setPercentageFDA(double percentageFDA) {
        this.percentageFDA = percentageFDA;
    }
}
