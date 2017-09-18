package com.five.high.emirim.testintent;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by jinselin on 2017-09-16.
 */

@IgnoreExtraProperties
class CalEvent {
    public String name;
    public String location;

    public CalEvent(){

    }

    public CalEvent(String name, String location) {
        this.name = name;
        this.location = location;
    }

    @Override
    public String toString() {
        return " " + name + " / " + location;
    }
}
