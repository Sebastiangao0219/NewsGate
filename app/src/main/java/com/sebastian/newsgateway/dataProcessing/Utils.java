package com.sebastian.newsgateway.dataProcessing;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import com.sebastian.newsgateway.activity.MainActivity;
import static android.graphics.Color.rgb;

public class Utils {
    public static int[] colors = {rgb(67, 159, 171), rgb(232, 166, 90),
            rgb(175, 101, 207), rgb(77, 158, 100), rgb(204, 96, 99),
            rgb(112, 2, 181), rgb(49, 131, 245), rgb(97, 66, 212)};

    public static String sourceUrl = "https://newsapi.org/v2/sources?apiKey=2eaf31a32aa24b2c98fbd0d924b9f0da";

    public static String getArticleUrl(String articleId) {
        return "https://newsapi.org/v2/top-headlines?sources=" + articleId + "&apiKey=2eaf31a32aa24b2c98fbd0d924b9f0da";
    }

    public static void dialogMessage(Context context) {
        String messages = getDialogMessage();
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("No Sources Match:")
                .setMessage(messages)
                .setPositiveButton("OK", (dialog1, which) -> {})
                .create();
        dialog.show();
    }

    private static String getDialogMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!MainActivity.selectedTopic.equals("all")) {
            stringBuilder.append("Topics: ").append(MainActivity.selectedTopic).append("\n");
        }
        if (!MainActivity.selectedCountry.equals("all")) {
            stringBuilder.append("Countries: ").append(MainActivity.selectedCountry).append("\n");
        }
        if (!MainActivity.selectedLanguage.equals("all")) {
            stringBuilder.append("Languages: ").append(MainActivity.selectedLanguage).append("\n");
        }
        return stringBuilder.toString();

    }
}
