package paperless.aqed.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import paperless.aqed.Util.Quote;

public class DbUpdation {

    private static final String TAG = DbUpdation.class.getSimpleName();

    public static void makeList(Context context) {
        DbUpdateTask dbUpdateTask = new DbUpdateTask();
        dbUpdateTask.execute(context);
    }

    private static class DbUpdateTask extends AsyncTask<Context, String, Integer> {

        @Override
        protected Integer doInBackground(Context... contexts) {
            DatabaseHandler dbHandler = DatabaseHandler.getsInstance(contexts[0]);
            Log.d(TAG,"Previous DB Count = "+dbHandler.getQuotesCount());
            try {
                JSONObject obj = new JSONObject(loadJSONFromAsset(contexts[0]));
                fillQuotesDb(obj, dbHandler);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return dbHandler.getQuotesCount();
        }

        @Override
        protected void onPostExecute(Integer count) {
            Log.d(TAG, "New DB Count = " + count);
        }
    }

    private static String loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("quotesList.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private static void fillQuotesDb(JSONObject obj, DatabaseHandler dbHandler) throws JSONException {
        JSONArray quotesArray = obj.getJSONArray("Quotes");
        for (int i = 0; i < quotesArray.length(); i++) {
            JSONObject jo_inside = quotesArray.getJSONObject(i);
            String quoteText = jo_inside.getString("Quote");
            String author = jo_inside.getString("Author");
            String wikiLink = jo_inside.getString("Wiki");

            Quote newQuote = new Quote(quoteText, author, wikiLink, 0);
            dbHandler.addQuote(newQuote);
        }
    }
}
