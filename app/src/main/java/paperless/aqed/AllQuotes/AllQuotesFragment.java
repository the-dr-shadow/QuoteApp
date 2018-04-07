package paperless.aqed.AllQuotes;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import paperless.aqed.QuotesMainActivity;
import paperless.aqed.R;

public class AllQuotesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String TAG = AllQuotesFragment.class.getSimpleName();

    private SearchView mSearchView;
    private ListView listView;
    public static String searchString;
    private ImageButton backKey;

    private QuoteListAdapter mListAdapter;

    public static final int QUOTE_LIST_LOADER = 0;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Log.d(TAG, "onCreateCalled");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        mListAdapter = new QuoteListAdapter(getActivity(), null);
        View view = inflater.inflate(R.layout.list_type_quotelist_fragment, container, false);

        mSearchView = (SearchView) view.findViewById(R.id.search_view);
        setUpSearchView();

        /*backKey = (ImageButton) view.findViewById(R.id.quote_list_home_key);
        backKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QuotesMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });*/

        listView = (ListView) view.findViewById(R.id.list);
        listView.requestFocus();
        listView.setAdapter(mListAdapter);
        //getLoaderManager().initLoader(QUOTE_LIST_LOADER, null, this).forceLoad();
        startLoader();
        return view;
    }

    public void startLoader() {
        getLoaderManager().initLoader(QUOTE_LIST_LOADER, null, AllQuotesFragment.this).forceLoad();
    }

    private void setUpSearchView() {
        mSearchView.clearFocus();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchString = newText;
                //getLoaderManager().initLoader(QUOTE_LIST_LOADER,null, (android.app.LoaderManager.LoaderCallbacks<Cursor>)this);
                startLoader();
                return true;
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.d(TAG, "Creating Loader");
        return new QuoteListLoader(getActivity(), null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished");
        mListAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        if (mListAdapter != null) {
            Cursor oldCursor = mListAdapter.swapCursor(null);
            if (oldCursor != null) {
                oldCursor.close();
            }
        }
    }

    public static String getSearchText() {
        return searchString;
    }
}
