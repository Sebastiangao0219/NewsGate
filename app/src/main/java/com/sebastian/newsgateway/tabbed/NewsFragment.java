package com.sebastian.newsgateway.tabbed;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.sebastian.newsgateway.R;
import com.sebastian.newsgateway.models.Article;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsFragment extends Fragment {
    private static String title;
    private static String date;
    private static String author;
    private static String description;
    private static String webUrl;
    private static String imageUrl;
    boolean isStateChanged = false;
    public static NewsFragment newInstance(Article article, int index, int max) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle(1);
        args.putSerializable("ARTICLE", article);
        args.putInt("INDEX", index);
        args.putInt("MAX", max);

        args.putString("title", article.getTitle());
        args.putString("date", article.getPublishedAt());
        args.putString("author", article.getAuthor());
        args.putString("description", article.getDescription());
        args.putString("webUrl", article.getUrl());
        args.putString("imageUrl", article.getUrlToImage());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.setRetainInstance(true);
        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null) {
//            title = savedInstanceState.getString("title");
//            date = savedInstanceState.getString("date");
//            author = savedInstanceState.getString("author");
//            description = savedInstanceState.getString("description");
//            webUrl = savedInstanceState.getString("webUrl");
//            imageUrl = savedInstanceState.getString("imageUrl");
//        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        if (getArguments() == null) {
            return rootView;
        }

        Article article = (Article) getArguments().getSerializable("ARTICLE");

        if (article == null) {
            return rootView;
        }

        int index = getArguments().getInt("INDEX");
        int max = getArguments().getInt("MAX");
        if (isStateChanged) {
            webUrl = getArguments().getString("webUrl");
        } else {
            webUrl = article.getUrl();
        }

        TextView headline = rootView.findViewById(R.id.headline);
        headline.setOnClickListener(v1 -> clickFragment(webUrl));

        if (isStateChanged) {
            title = getArguments().getString("title");
        } else {
            title = article.getTitle();
        }

        if (title != null && !title.equals("null")) {
            headline.setText(title);
        }

        TextView dateView = rootView.findViewById(R.id.date);

        if (isStateChanged) {
            date = getArguments().getString("date");
        } else {
            date = article.getPublishedAt();
        }

        if (date != null && !date.equals("null")) {
            String formatDate;
            try {
                formatDate = formatDateString1(date);
            } catch (ParseException e) {
                try {
                    formatDate = formatDateString2(date);
                } catch (ParseException ex) {
                    try {
                        formatDate = formatDateString3(date);
                    } catch (ParseException exx) {
                        formatDate = date;
                    }
                }
            }
            dateView.setText(formatDate);

        } else {
            dateView.setVisibility(View.GONE);
        }

        TextView authorView = rootView.findViewById(R.id.author);

        if (isStateChanged) {
            author = getArguments().getString("author");
        } else {
            author = article.getAuthor();
        }
        if (author != null && !author.equals("null")) {
            authorView.setText(article.getAuthor());
        } else {
            authorView.setVisibility(View.GONE);
        }

        ImageView image = rootView.findViewById(R.id.imageView);
        image.setOnClickListener(v13 -> clickFragment(webUrl));

        if (isStateChanged) {
            imageUrl = getArguments().getString("imageUrl");
        } else {
            imageUrl = article.getUrlToImage();
        }

        if (imageUrl != null && !imageUrl.equals("null")) {
            Picasso.get().load(imageUrl).placeholder(R.drawable.loading).error(R.drawable.brokenimage).into(image);
        }

        TextView text = rootView.findViewById(R.id.articleText);
        text.setOnClickListener(v12 -> clickFragment(webUrl));

        if (isStateChanged) {
            description = getArguments().getString("description");
        } else {
            description = article.getDescription();
        }

        if (description != null && !description.equals("null")) {
            text.setText(description);
        } else {
            text.setVisibility(View.GONE);
        }

        TextView slideCount = rootView.findViewById(R.id.articleCount);
        slideCount.setText(String.format("%d of %d", index, max));

        return rootView;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isStateChanged = true;
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle args) {
//        super.onSaveInstanceState(args);
//        isStateChanged = true;
//    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if (savedInstanceState != null) {
//            title = savedInstanceState.getString("title");
//            date = savedInstanceState.getString("date");
//            author = savedInstanceState.getString("author");
//            description = savedInstanceState.getString("description");
//            webUrl = savedInstanceState.getString("webUrl");
//            imageUrl = savedInstanceState.getString("imageUrl");
//        }
//    }

    public void clickFragment(String url) {
        if (url == null) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }

    private String formatDateString1(String inputDate) throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'");
        Date date = dateFormat.parse(inputDate);

        DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        assert date != null;
        return formatter.format(date);
    }

    private String formatDateString2(String inputDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = dateFormat.parse(inputDate);

        DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        assert date != null;
        return formatter.format(date);
    }

    private String formatDateString3(String inputDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date date = dateFormat.parse(inputDate);

        DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        assert date != null;
        return formatter.format(date);
    }
}
