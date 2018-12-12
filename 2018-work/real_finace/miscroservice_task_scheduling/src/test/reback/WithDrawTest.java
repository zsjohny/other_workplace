package com.finace.miscroservice;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class WithDrawTest {

    public static void main(String[] args) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL("").openConnection();
    }

}
