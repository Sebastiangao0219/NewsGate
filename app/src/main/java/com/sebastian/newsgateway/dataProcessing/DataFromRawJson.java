package com.sebastian.newsgateway.dataProcessing;

import android.content.Context;

import com.sebastian.newsgateway.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class DataFromRawJson {
    public static HashMap<String, String> getCountriesCode(Context context) {
        String data = loadJSONData(R.raw.country_codes, context);
        HashMap<String, String> countriesCodes = ParseJson.parseLocalJSON(data, "countries");
        return countriesCodes;
    }

    public static HashMap<String, String> getLanguageCode(Context context) {
        String data = loadJSONData(R.raw.language_codes, context);
        HashMap<String, String> languageCodes = ParseJson.parseLocalJSON(data, "languages");
        return languageCodes;
    }

    public static String loadJSONData(int id, Context context) {
        try {
            InputStream is = context.getResources().openRawResource(id);

            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            reader.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
