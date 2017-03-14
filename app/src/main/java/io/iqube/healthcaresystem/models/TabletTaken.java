package io.iqube.healthcaresystem.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Srinath on 04-03-2017.
 */

public class TabletTaken extends RealmObject {
    @PrimaryKey
    int id;

    Tablet TabletID;

    Date taken_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tablet getTabletID() {
        return TabletID;
    }

    public void setTabletID(Tablet tabletID) {
        TabletID = tabletID;
    }

    public Date getTaken_at() {
        return taken_at;
    }

    public void setTaken_at(Date taken_at) {
        this.taken_at = taken_at;
    }

    public boolean is_correct() {
        return is_correct;
    }

    public void setIs_correct(boolean is_correct) {
        this.is_correct = is_correct;
    }

    boolean is_correct;
}
