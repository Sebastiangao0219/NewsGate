package com.sebastian.newsgateway.tabbed;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sebastian.newsgateway.R;
import com.sebastian.newsgateway.models.NewsSource;
import com.sebastian.newsgateway.activity.MainActivity;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<NewsSource> {
    private final ArrayList<NewsSource> sourcesArrayList;
    private final int layoutResourceId;
    private final Context context;

    public NewsAdapter(Context context, int layoutResourceId,ArrayList<NewsSource> sourcesArrayList) {
        super(context,layoutResourceId,sourcesArrayList);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.sourcesArrayList = sourcesArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = ((Activity) context).getLayoutInflater().inflate(layoutResourceId, parent,false);
        TextView textView = convertView.findViewById(R.id.text1);
        NewsSource news = sourcesArrayList.get(position);
        textView.setText(news.getName());

        String category = news.getCategory();
        if (MainActivity.colorList.containsKey(category)){
            textView.setTextColor(MainActivity.colorList.get(category));
        }

        return convertView;
    }
}
