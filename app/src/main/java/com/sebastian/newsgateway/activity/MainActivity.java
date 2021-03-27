package com.sebastian.newsgateway.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.ListView;

import com.sebastian.newsgateway.R;
import com.sebastian.newsgateway.tabbed.NewsFragment;
import com.sebastian.newsgateway.dataProcessing.Utils;
import com.sebastian.newsgateway.tabbed.NewsAdapter;
import com.sebastian.newsgateway.tabbed.NewsPagerAdapter;
import com.sebastian.newsgateway.dataProcessing.DataFromAPIRunnable;
import com.sebastian.newsgateway.dataProcessing.DataManager;
import com.sebastian.newsgateway.models.Article;
import com.sebastian.newsgateway.models.NewsSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int TOPIC_MENU = 1;
    private static final int COUNTRIES_MENU = 2;
    private static final int LANGUAGES_MENU = 3;
    public static String selectedTopic = "all", selectedLanguage = "all", selectedCountry = "all";
    public static HashMap<String, Integer> colorList = new HashMap<>();
    private DrawerLayout drawerLayout;
    private ListView listView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private final ArrayList<NewsSource> currentSelectedList = new ArrayList<>();
    private DataManager dataManager;
    private NewsAdapter newsAdapter;
    private SubMenu topicMenu, countriesMenu, languagesMenu;
    private NewsPagerAdapter pageAdapter;
    private final List<Fragment> fragments = new ArrayList<>();
    private ViewPager pager;
    private String currentArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setDrawerLayout();
        setViewPager();

//        if (savedInstanceState != null) {
//            for (int i = 0; i < fragments.size(); ++i) {
//                Fragment currFragment = getSupportFragmentManager().getFragment(savedInstanceState, "fragments");
//                fragments.add(currFragment);
//            }
//        }
    }

    public void init() {
        dataManager = new DataManager(this, this);
        DataFromAPIRunnable data = new DataFromAPIRunnable(dataManager, Utils.sourceUrl);
        new Thread(data).start();

        drawerLayout = findViewById(R.id.drawerLayout);
        listView = findViewById(R.id.listView);
        pager = findViewById(R.id.viewpager);
    }

    public void setViewPager() {
        pageAdapter = new NewsPagerAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(pageAdapter);
    }

    public void setDrawerLayout() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            pager.setBackground(null);
            NewsSource news = currentSelectedList.get(position);
            String article = news.getId();
            currentArticle = news.getName();
            DataFromAPIRunnable data = new DataFromAPIRunnable(dataManager, Utils.getArticleUrl(article));
            new Thread(data).start();
            drawerLayout.closeDrawer(listView);
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        );

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        String selection = item.getTitle().toString();

        if (selection.equals("Topics") || selection.equals("Countries") || selection.equals("Languages")) {
            return false;
        }

        switch (item.getGroupId()) {
            case TOPIC_MENU:
                selectedTopic = selection;
                break;
            case COUNTRIES_MENU:
                selectedCountry = selection;
                break;
            case LANGUAGES_MENU:
                selectedLanguage = selection;
                break;
        }

        currentSelectedList.clear();
        currentSelectedList.addAll(dataManager.filterNews());
        if (currentSelectedList.isEmpty()) {
            Utils.dialogMessage(this);
        }
        newsAdapter.notifyDataSetChanged();
        setTitle(String.format("%s (%d)", "News Gateway", currentSelectedList.size()));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);

        topicMenu = menu.addSubMenu("Topics");
        countriesMenu = menu.addSubMenu("Countries");
        languagesMenu = menu.addSubMenu("Languages");

        return super.onCreateOptionsMenu(menu);
    }

    public void setTopicSubmenu(List<String> topics) {
        if (!topics.isEmpty()) {
            topicMenu.add(TOPIC_MENU, 1, Menu.NONE, "all");

            for (int i = 0; i < topics.size(); i++) {
                String topic = topics.get(i);
                if (i < 8) {
                    SpannableStringBuilder builder = new SpannableStringBuilder(topic);
                    builder.setSpan(new ForegroundColorSpan(Utils.colors[i]), 0, topic.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    colorList.put(topic, Utils.colors[i]);
                    topicMenu.add(TOPIC_MENU, 1, Menu.NONE, builder);
                } else {
                    topicMenu.add(TOPIC_MENU, 1, Menu.NONE, topics.get(i));
                }
            }
        }

    }

    public void setLanguageSubmenu(List<String> languageSet) {
        if (!languageSet.isEmpty()) {
            languagesMenu.add(LANGUAGES_MENU, 2, Menu.NONE, "all");
            for (String language : languageSet) {
                languagesMenu.add(LANGUAGES_MENU, 2, Menu.NONE, language);
            }
        }
    }

    public void setCountrySubmenu(List<String> countries) {
        if (!countries.isEmpty()) {
            countriesMenu.add(COUNTRIES_MENU, 3, Menu.NONE, "all");
            for (String country : countries) {
                countriesMenu.add(COUNTRIES_MENU, 3, Menu.NONE, country);
            }
        }
    }

    public void setNewsSourcesList(ArrayList<NewsSource> newsSourceArrayList) {
        runOnUiThread(() -> {
            currentSelectedList.addAll(newsSourceArrayList);
            newsAdapter = new NewsAdapter(MainActivity.this, R.layout.drawer_list, currentSelectedList);
            setTitle(String.format("%s (%d)", "News Gateway", currentSelectedList.size()));
            listView.setAdapter(newsAdapter);
        });

    }

    public void getArticleList(ArrayList<Article> articleArrayList) {
        runOnUiThread(() -> {
            setTitle(currentArticle);
            for (int i = 0; i < pageAdapter.getCount(); i++) {
                pageAdapter.notifyChangeInPosition(i);
            }
            fragments.clear();
            int num = articleArrayList.size();
            for (int i = 0; i < num; i++) {
                fragments.add(NewsFragment.newInstance(articleArrayList.get(i), i + 1, num));
            }
            pageAdapter.notifyDataSetChanged();
            pager.setCurrentItem(0);
        });
    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.put
//    }


}