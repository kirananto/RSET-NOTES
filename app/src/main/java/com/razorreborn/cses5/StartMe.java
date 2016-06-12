package com.razorreborn.cses5;

import android.app.Application;
import com.parse.Parse;

/**
 * Created by razorSharp on 15/3/16.
 */
public class StartMe extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("K96pNpoYAZYZyNa3y30f1KEkomuRJrIv2lQf9ehV")
                .clientKey("60FK9foilbdWnqJPwRMTHz3F4yCNIxRbx4loWJxi")
                .server("https://parseapi.back4app.com")
        .build()
        );
    }
}
