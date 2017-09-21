package com.five.high.emirim.testintent;

/**
 * Created by jinselin on 2017-09-21.
 */

public class CalEventWithKey extends CalEvent {
    public String key;

    public CalEventWithKey( String name, String location, String key){
        super(name, location);
        this.key = key;
    }
}
