package io.iqube.healthcaresystem.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Srinath on 04-03-2017.
 */

public class Tablet extends RealmObject{
    @PrimaryKey
    int id;

    String tabletName;

    String isActive;

    String Prescription;

    String TotalQtyStocked;

    String TotalQtyDispensed;

    String CreatedAt;

    String TimeFlexibility;


}
