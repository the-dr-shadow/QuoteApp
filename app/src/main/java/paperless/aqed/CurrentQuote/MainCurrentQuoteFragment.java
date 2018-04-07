package paperless.aqed.CurrentQuote;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import paperless.aqed.R;

public class MainCurrentQuoteFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_quote_layout,container,false);

        createInnerFragments();
        return view;
    }

    private void createInnerFragments() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        CurrentQuoteTextFragment tqf = new CurrentQuoteTextFragment();
        CurrentQuotePrimaryButtonsFragment pbv = new CurrentQuotePrimaryButtonsFragment();
        ft.add(R.id.todaysQuoteFragment, tqf, "TodaysQuoteFragment");
        ft.add(R.id.primary_button_view,pbv,"PrimaryButtonViews");
        ft.commit();
    }
}
