package com.example.alarm;

public class MedicineLibrary {

    String name, description, dose;

    public MedicineLibrary(){

    }

    public MedicineLibrary(String name, String description, String dose) {
        this.name = name;
        this.description = description;
        this.dose = dose;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }
}
