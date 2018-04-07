package paperless.aqed.AllQuotes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import paperless.aqed.R;
import paperless.aqed.Util.Constants;

class QuoteListAdapter extends CursorAdapter {

    private static final String TAG = QuoteListAdapter.class.getSimpleName();

    QuoteListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    private TextView quoteText;
    private Button authorName;
    private String wikiLink, queryText;
    private Context thisContext;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.all_quote_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        queryText = AllQuotesFragment.getSearchText();
        thisContext = context;
        quoteText = (TextView) view.findViewById(R.id.quoteText);
        authorName = (Button) view.findViewById(R.id.authorName);

        String quote = cursor.getString(cursor.getColumnIndexOrThrow(Constants.KEY_QUOTE));
        String author = cursor.getString(cursor.getColumnIndexOrThrow(Constants.KEY_AUTHOR));
        wikiLink = cursor.getString(cursor.getColumnIndex(Constants.KEY_WIKILINK));

        quoteText.setText(quote);
        SpannableString content = new SpannableString(author);
        content.setSpan(new UnderlineSpan(),0,content.length(),0);
        authorName.setText(content);

        if (queryText != null && !TextUtils.isEmpty(queryText)){
            quoteText.setText(highlightString(queryText.toLowerCase(), quote));
            authorName.setText(highlightString(queryText.toLowerCase(), author));
        }
        authorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(wikiLink));
                thisContext.startActivity(launchBrowser);
            }
        });

    }

    private SpannableString highlightString(String input, String text) {
        //Get the text from text view and create a spannable string
        SpannableString spannableString = new SpannableString(text);
/*
        //Get the previous spans and remove them
        BackgroundColorSpan[] backgroundSpans = spannableString.getSpans(0, spannableString.length(), BackgroundColorSpan.class);

        for (BackgroundColorSpan span : backgroundSpans) {
            spannableString.removeSpan(span);
        }*/

        //Search for all occurrences of the keyword in the string
        int indexOfKeyword = spannableString.toString().indexOf(input);

        while (indexOfKeyword > 0) {
            Log.d(TAG, "String matched = "+input+", "+text);
            spannableString.setSpan(new BackgroundColorSpan(Color.GREEN), indexOfKeyword, indexOfKeyword + input.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //Get the next index of the keyword
            indexOfKeyword = spannableString.toString().indexOf(input, indexOfKeyword + input.length());
        }
        //Set the final text on TextView
        return spannableString;
    }
}