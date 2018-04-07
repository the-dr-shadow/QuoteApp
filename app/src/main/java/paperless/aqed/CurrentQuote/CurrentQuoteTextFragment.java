package paperless.aqed.CurrentQuote;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import paperless.aqed.Database.DatabaseHandler;
import paperless.aqed.R;
import paperless.aqed.Util.Quote;

public class CurrentQuoteTextFragment extends Fragment {
    public static final String TAG = CurrentQuoteTextFragment.class.getSimpleName();

    private TextView mQuoteText;
    private Button mAuthorName;
    private String mWikiLink;
    private static Quote mQuote;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Log.d(TAG,"onCreateCalled");
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        Log.d(TAG,"Activity Created");
        super.onActivityCreated(savedInstance);
        displayTodaysQuote();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View view = inflater.inflate(R.layout.today_quote_layout,container,false);
        mQuoteText = (TextView) view.findViewById(R.id.quoteText);
        mAuthorName = (Button) view.findViewById(R.id.authorName);

        mAuthorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(mWikiLink));
                startActivity(launchBrowser);
            }
        });
        return view;
    }

    public void displayTodaysQuote() {
        DatabaseHandler dbHandler = DatabaseHandler.getsInstance(getActivity().getApplicationContext());
        while(dbHandler.getQuotesCount()<1){
            // Just in case new DB has not been created, wait till atleast one quote is inserted
            // into the db

            // V Bad approach
        }
        Quote quote = dbHandler.getRandomQuote();
        setTodaysQuote(quote);
        mQuoteText.setText(quote.getQuoteText());
        SpannableString content = new SpannableString(quote.getAuthor());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        mAuthorName.setText(content);
        mWikiLink = quote.getWikiLink();
    }

    public void setTodaysQuote(Quote quote) {
        mQuote = quote;
    }

    public static Quote getTodaysQuote() {
        return mQuote;
    }
}
