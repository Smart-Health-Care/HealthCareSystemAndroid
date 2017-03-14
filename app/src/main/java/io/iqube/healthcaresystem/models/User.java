package io.iqube.healthcaresystem.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Srinath on 04-03-2017.
 */

public class User extends RealmObject {
    @PrimaryKey
    int id;

    String username;

    String Password;

    String FirstName;

    String LastName;

    String Age;

    String Email;
}
