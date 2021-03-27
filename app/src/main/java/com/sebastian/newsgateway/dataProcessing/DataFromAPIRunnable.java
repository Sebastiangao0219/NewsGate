package com.sebastian.newsgateway.dataProcessing;

import android.net.Uri;

import com.sebastian.newsgateway.models.Article;
import com.sebastian.newsgateway.models.NewsSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DataFromAPIRunnable implements Runnable{
    private final DataManager dataManager;
    private final String dataURL;

    public DataFromAPIRunnable(DataManager dataManager, String dataURL) {
        this.dataManager = dataManager;
        this.dataURL = dataURL;
    }

    @Override
    public void run() {
        Uri dataUri = Uri.parse(dataURL);
        String urlToUse = dataUri.toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent","");
            connection.connect();

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        processResults(sb.toString());
    }

    public void processResults(String s) {
        if (dataURL.contains("headlines")){
            ArrayList<Article> articleArrayList = s.isEmpty() ? null : ParseJson.parseArticles(s);
            dataManager.getArticleInfo(articleArrayList);
        } else {
            ArrayList<NewsSource> newsList = s.isEmpty() ? null : ParseJson.parseSources(s);
            dataManager.getSourceInfo(newsList);
        }
    }
}
