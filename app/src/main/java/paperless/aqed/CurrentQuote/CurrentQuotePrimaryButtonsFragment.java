package paperless.aqed.CurrentQuote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import paperless.aqed.R;
import paperless.aqed.Util.Quote;

public class CurrentQuotePrimaryButtonsFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = CurrentQuotePrimaryButtonsFragment.class.getSimpleName();

    View setAsWallpaperView, changeNotiSettingView, browseAllView;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Log.d(TAG, "onCreateCalled");
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        Log.d(TAG, "Activity Created");
        super.onActivityCreated(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View view = inflater.inflate(R.layout.primary_buttons_view, container, false);
        setAsWallpaperView = view.findViewById(R.id.set_as_wallpaper_view);
        changeNotiSettingView = view.findViewById(R.id.change_noti_settings_view);
        //browseAllView = view.findViewById(R.id.browse_all_view);

        setAsWallpaperView.setOnClickListener(this);
        changeNotiSettingView.setOnClickListener(this);
        //browseAllView.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set_as_wallpaper_view:
                //Launch setAsWallpaperActivity with customizations possible
                /*Intent i = new Intent(getActivity(), MainWallpaperActivity.class);
                Quote quote = TodaysQuoteFragment.getTodaysQuote();
                i.putExtra(Constants.KEY_QUOTE, quote.getQuoteText());
                i.putExtra(Constants.KEY_AUTHOR, quote.getAuthor());
                this.startActivity(i);*/

                break;
            case R.id.change_noti_settings_view:
                //Launch Notification Change Settings and update the DB as per the same
                break;
            /*case R.id.browse_all_view:
                //Launch all Quotes activity having a search option too
                //this.startActivity(new Intent(getActivity(), QuoteListActivity.class));
                *//*FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.listquotefragment, new ListQuoteFragment());
                ft.remove(getFragmentManager().findFragmentById(R.id.todaysQuoteFragment));
                ft.remove(getFragmentManager().findFragmentById(R.id.primary_button_view));
                ft.commit();*//*

                break;*/
            default:
        }
    }
}
