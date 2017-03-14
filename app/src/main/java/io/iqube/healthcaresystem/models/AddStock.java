package io.iqube.healthcaresystem.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Srinath on 04-03-2017.
 */

public class AddStock extends RealmObject {

    @PrimaryKey
    int id;

    int QTYAdded;

    Tablet TabletId;

    Date AddedAt;

    int NoOfDays;
}
