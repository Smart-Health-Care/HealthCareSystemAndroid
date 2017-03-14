package io.iqube.healthcaresystem.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Srinath on 07-03-2017.
 */

public class Medicine extends RealmObject {
    @PrimaryKey
    int id;

    String name;

    String Dosage;

    String quantity;

    String prescription;
    public Medicine(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return Dosage;
    }

    public void setDosage(String dosage) {
        Dosage = dosage;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public Medicine(int id, String name, String dosage, String quantity, String prescription) {

        this.id = id;
        this.name = name;
        Dosage = dosage;
        this.quantity = quantity;
        this.prescription = prescription;
    }
}
