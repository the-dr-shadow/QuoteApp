package paperless.aqed.AllQuotes;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.Log;

import paperless.aqed.Database.DatabaseHandler;

class QuoteListLoader extends AsyncTaskLoader<Cursor> {
    private static final String TAG = QuoteListLoader.class.getSimpleName();

    private Context mContext;
    private static String mqueryText;

    QuoteListLoader(Context context, String queryText) {
        super(context);
        mContext = context;
        mqueryText = queryText;
    }

    @Override
    public Cursor loadInBackground() {
        Log.d(TAG, "Load in Background");
        mqueryText = AllQuotesFragment.getSearchText();
        Cursor c;
        if (TextUtils.isEmpty(mqueryText)) {
            c = DatabaseHandler.getsInstance(mContext).getAllQuotesCursor();
        } else {
            Log.d(TAG, "Query Text = " + mqueryText);
            c = DatabaseHandler.getsInstance(mContext).getQuotesCursorBySearch(mqueryText);
        }
        if (c != null) {
            Log.d(TAG, "Count = " + c.getCount());
        }
        return c;
    }
}
