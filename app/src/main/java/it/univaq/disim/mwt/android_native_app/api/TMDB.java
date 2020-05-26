package it.univaq.disim.mwt.android_native_app.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import it.univaq.disim.mwt.android_native_app.R;
import it.univaq.disim.mwt.android_native_app.utils.VolleyRequest;

public class TMDB {

    public static void getRemoteTvShowDetails(final Context context, long tvShowID){

        final String API_URL = context.getResources().getString(R.string.tmdb_api_url);
        final String API_KEY = context.getResources().getString(R.string.tmdb_api_key);
        final String LANGUAGE = context.getResources().getString(R.string.tmdb_api_language);

        String requestLink = API_URL + "tv/" + tvShowID + "?api_key=" + API_KEY + "&language=" + LANGUAGE;

        doRequest(context, requestLink);
    }

    public static void getRemoteTvShowsPopular(final Context context, long page){

        final String API_URL = context.getResources().getString(R.string.tmdb_api_url);
        final String API_KEY = context.getResources().getString(R.string.tmdb_api_key);
        final String LANGUAGE = context.getResources().getString(R.string.tmdb_api_language);

        String requestLink = API_URL + "tv/popular" + "?api_key=" + API_KEY + "&language=" + LANGUAGE + "&page=" + page;

        doRequest(context, requestLink);
    }

    public static void getRemoteTvShowsTopRated(final Context context, long page){

        final String API_URL = context.getResources().getString(R.string.tmdb_api_url);
        final String API_KEY = context.getResources().getString(R.string.tmdb_api_key);
        final String LANGUAGE = context.getResources().getString(R.string.tmdb_api_language);

        String requestLink = API_URL + "tv/top_rated" + "?api_key=" + API_KEY + "&language=" + LANGUAGE + "&page=" + page;

        doRequest(context, requestLink);
    }

    public static void getRemoteTvShowSimilars(final Context context, long tvShowID, long page){

        final String API_URL = context.getResources().getString(R.string.tmdb_api_url);
        final String API_KEY = context.getResources().getString(R.string.tmdb_api_key);
        final String LANGUAGE = context.getResources().getString(R.string.tmdb_api_language);

        String requestLink = API_URL + "tv/" + tvShowID + "/similar" + "?api_key=" + API_KEY + "&language=" + LANGUAGE + "&page=" + page;

        doRequest(context, requestLink);
    }

    public static void getRemoteTvShowSeason(final Context context, long tvShowID, int seasonNumber){

        final String API_URL = context.getResources().getString(R.string.tmdb_api_url);
        final String API_KEY = context.getResources().getString(R.string.tmdb_api_key);
        final String LANGUAGE = context.getResources().getString(R.string.tmdb_api_language);

        String requestLink = API_URL + "tv/" + tvShowID + "/season/" + seasonNumber + "?api_key=" + API_KEY + "&language=" + LANGUAGE;

        doRequest(context, requestLink);
    }

    public static void getRemoteTvShowEpisode(final Context context, long tvShowID, int seasonNumber, int episodeNumber){

        final String API_URL = context.getResources().getString(R.string.tmdb_api_url);
        final String API_KEY = context.getResources().getString(R.string.tmdb_api_key);
        final String LANGUAGE = context.getResources().getString(R.string.tmdb_api_language);

        String requestLink = API_URL + "tv/" + tvShowID + "/season/" + seasonNumber + "/episode/" + episodeNumber + "?api_key=" + API_KEY + "&language=" + LANGUAGE;

        doRequest(context, requestLink);
    }

    public static void getRemoteTvShowCredits(final Context context, long tvShowID){

        final String API_URL = context.getResources().getString(R.string.tmdb_api_url);
        final String API_KEY = context.getResources().getString(R.string.tmdb_api_key);
        final String LANGUAGE = context.getResources().getString(R.string.tmdb_api_language);

        String requestLink = API_URL + "tv/" + tvShowID + "/credits" + "?api_key=" + API_KEY + "&language=" + LANGUAGE;

        doRequest(context, requestLink);
    }

    public static void getRemoteTvShowsSearch(final Context context, String query, long page){

        final String API_URL = context.getResources().getString(R.string.tmdb_api_url);
        final String API_KEY = context.getResources().getString(R.string.tmdb_api_key);
        final String LANGUAGE = context.getResources().getString(R.string.tmdb_api_language);

        String requestLink = API_URL + "search/tv" + "?api_key=" + API_KEY + "&query=" + query +  "&page=" + page + "&language=" + LANGUAGE;

        doRequest(context, requestLink);
    }


    private static void doRequest(final Context context, String url){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callParserService(context, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleResponseError(error);
                    }
                });
        VolleyRequest.getInstance(context).getQueue().add(request);
    }

    private static void callParserService(final Context context, JSONObject response){
        // TODO: Parser service creation
        // Call service
        /*Intent intent = new Intent(context, MyThreadService.class);
        intent.putExtra(MyThreadService.KEY_ACTION, MyThreadService.ACTION_PARSE_DATA);
        intent.putExtra(MyThreadService.KEY_DATA, response.toString());
        startService(intent);*/
    }

    private static void handleResponseError(VolleyError error){
        // TODO: Volley error handling
    }
}
