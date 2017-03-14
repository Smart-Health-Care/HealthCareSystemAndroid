package io.iqube.healthcaresystem.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Srinath on 04-03-2017.
 */

public class Time extends RealmObject {
    @PrimaryKey
    int id;

    int Hour;

    int Minute;

    int DayOfWeek;
}
