package com.sebastian.newsgateway.dataProcessing;

import android.content.Context;

import com.sebastian.newsgateway.models.Article;
import com.sebastian.newsgateway.models.NewsSource;
import com.sebastian.newsgateway.activity.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DataManager {
    private final ArrayList<NewsSource> newsSourceList = new ArrayList<>();
    private final MainActivity mainActivity;
    private final HashMap<String, String> countryCodes;
    private final HashMap<String, String> languageCodes;

    public DataManager(MainActivity mainActivity, Context context) {
        this.mainActivity = mainActivity;
        countryCodes = DataFromRawJson.getCountriesCode(context);
        languageCodes = DataFromRawJson.getLanguageCode((context));
    }

    public void getArticleInfo(ArrayList<Article> articleArrayList) {
        if (articleArrayList == null)
            return;
        mainActivity.getArticleList(articleArrayList);
    }

    public void getSourceInfo(ArrayList<NewsSource> newsSourceArrayList) {
        if (newsSourceArrayList == null){
            return;
        }
        ArrayList<NewsSource> list = new ArrayList<>();
        for (NewsSource news : newsSourceArrayList) {

            String language = news.getLanguage().toUpperCase();
            String country = news.getCountry().toUpperCase();

            if (languageCodes.containsKey(language) && countryCodes.containsKey(country)) {
                news.setLanguage(languageCodes.get(language));
                news.setCountry(countryCodes.get(country));
                list.add(news);
            }
        }

        newsSourceList.addAll(list);
        mainActivity.setNewsSourcesList(list);
        getDataForMenu(list);
    }

    public void getDataForMenu(ArrayList<NewsSource> newsSourceArrayList) {

        HashSet<String> topicSet = new HashSet<>();
        HashSet<String> languageSet = new HashSet<>();
        HashSet<String> countrySet = new HashSet<>();

        for (NewsSource news : newsSourceArrayList) {
            topicSet.add(news.getCategory());
            languageSet.add(news.getLanguage());
            countrySet.add(news.getCountry());
        }

        List<String> tempTopicList = new ArrayList<>(topicSet);
        Collections.sort(tempTopicList);
        mainActivity.setTopicSubmenu(tempTopicList);

        List<String> tempLanguageList = new ArrayList<>(languageSet);
        Collections.sort(tempLanguageList);
        mainActivity.setLanguageSubmenu(tempLanguageList);

        List<String> tempCountryList = new ArrayList<>(countrySet);
        Collections.sort(tempCountryList);
        mainActivity.setCountrySubmenu(tempCountryList);
    }

    public ArrayList<NewsSource> filterNews() {
        ArrayList<NewsSource> filteredNews = (ArrayList<NewsSource>) newsSourceList.clone();

        if (!MainActivity.selectedTopic.equals("all")) {
            ArrayList<NewsSource> tempArray = new ArrayList<>();
            for (NewsSource news : filteredNews) {
                if (!news.getCategory().equals(MainActivity.selectedTopic)) {
                    tempArray.add(news);
                }
            }
            filteredNews.removeAll(tempArray);
        }

        if (!MainActivity.selectedCountry.equals("all")) {
            ArrayList<NewsSource> tempArray = new ArrayList<>();
            for (NewsSource news : filteredNews) {
                if (!news.getCountry().equals(MainActivity.selectedCountry)) {
                    tempArray.add(news);
                }
            }
            filteredNews.removeAll(tempArray);
        }

        if (!MainActivity.selectedLanguage.equals("all")) {
            ArrayList<NewsSource> tempArray = new ArrayList<>();
            for (NewsSource news : filteredNews) {
                if (!news.getLanguage().equals(MainActivity.selectedLanguage)) {
                    tempArray.add(news);
                }
            }
            filteredNews.removeAll(tempArray);
        }

        return filteredNews;
    }
}
