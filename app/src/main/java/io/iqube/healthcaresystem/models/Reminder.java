package io.iqube.healthcaresystem.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Srinath on 03-03-2017.
 */

public class Reminder extends RealmObject {
    @PrimaryKey
    int id;

    String time;

    String date;


    RealmList<Medicine> medicineList;

    public Reminder(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RealmList<Medicine> getMedicineList() {
        return medicineList;
    }

    public void setMedicineList(RealmList<Medicine> medicineList) {
        this.medicineList = medicineList;
    }

    public Reminder(int id, String time, String date, RealmList<Medicine> medicineList) {

        this.id = id;
        this.time = time;
        this.date = date;
        this.medicineList = medicineList;
    }



}
