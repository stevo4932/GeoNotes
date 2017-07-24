package us.ststephens.geonotes.network;

import android.net.Uri;

import java.util.Locale;

public class BaseApi {
    private static final String HOST = "http://127.0.0.1:5000";
    private static final String VERSION = "v1.0";

    public static Uri parseUrl(String endpoint) {
        String url = String.format(Locale.getDefault(), "%s/api/%s/%s", HOST, VERSION, endpoint);
        return Uri.parse(url);
    }
}
