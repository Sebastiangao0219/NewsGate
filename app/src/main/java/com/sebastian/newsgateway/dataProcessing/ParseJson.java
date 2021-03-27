package com.sebastian.newsgateway.dataProcessing;

import com.sebastian.newsgateway.models.Article;
import com.sebastian.newsgateway.models.NewsSource;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ParseJson {
    public static ArrayList<NewsSource> parseSources(String s) {
        try {
            ArrayList<NewsSource> list = new ArrayList<>();

            JSONObject jsonObject = new JSONObject((s));

            JSONArray jsonArray = jsonObject.getJSONArray("sources");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                String id = obj.getString("id");
                String name = obj.getString("name");
                String category = obj.getString("category");
                String language = obj.getString("language");
                String country = obj.getString("country");

                NewsSource news = new NewsSource(id, name, category, language, country);
                list.add(news);
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static ArrayList<Article> parseArticles(String s) {
        try {
            ArrayList<Article> list = new ArrayList<>();

            JSONObject jsonObject = new JSONObject((s));

            JSONArray jsonArray = jsonObject.getJSONArray("articles");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                String author = null;
                if (obj.has("author")){
                    String authorString = obj.getString("author");
                    author = authorString.equals("null") || authorString.isEmpty() ? null : authorString;
                }

                String title = null;
                if (obj.has("title")){
                    title = obj.getString("title");
                }


                String description = null;
                if (obj.has("description")){
                    String deString = obj.getString("description");
                    description = deString.equals("null") || deString.isEmpty() ? null : deString;
                }


                String url = null;
                if (obj.has("url")){
                    String urlString = obj.getString("url");
                    url = urlString.equals("null") || urlString.isEmpty() ? null : urlString;
                }


                String urlToImage = null;
                if (obj.has("urlToImage")){
                    String urlToImageString = obj.getString("urlToImage");
                    urlToImage = urlToImageString.equals("null") || urlToImageString.isEmpty() ? null : urlToImageString;
                }

                String publishedAt = null;
                if (obj.has("publishedAt")){
                    String publishedAtString = obj.getString("publishedAt");
                    publishedAt = publishedAtString.equals("null") || publishedAtString.isEmpty() ? null : publishedAtString;
                }

                Article article = new Article(author,title,description,url,urlToImage,publishedAt);
                list.add(article);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String,String> parseLocalJSON(String s, String objectName) {
        HashMap<String, String> codes = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray(objectName);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                String code = jo.getString("code");
                String name = jo.getString("name");

                codes.put(code, name);
            }
            return codes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
