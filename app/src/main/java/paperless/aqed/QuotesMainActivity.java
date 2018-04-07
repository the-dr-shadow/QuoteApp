package paperless.aqed;

import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import paperless.aqed.AllQuotes.AllQuotesFragment;
import paperless.aqed.CurrentQuote.MainCurrentQuoteFragment;
import paperless.aqed.Database.DbUpdation;
import paperless.aqed.Util.QuotesUtil;

public class QuotesMainActivity extends AppCompatActivity {

    MainCurrentQuoteFragment mCurrentQuoteFragment;
    AllQuotesFragment mAllQuoteFragment;
    private static final String TAG = QuotesMainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Code to update Json File
        // Needed when the server starts functioning
        if (QuotesUtil.isConnected(this)) {
            QuotesUtil.checkJsonUpdate(this);
        }


        if (QuotesUtil.isFirstTime(this)) {
            // Launch a static screen with an awesome Quote
            // Done so that in the mean time our DB is initialized from the JSON file.

            // Make the DB from the JSON File
            DbUpdation.makeList(this);
        }
        //Toast.makeText(this,"DB Entries = "+ DatabaseHandler.getsInstance(QuotesMainActivity.this).getQuotesCount(),Toast.LENGTH_SHORT).show();


        setContentView(R.layout.quote_main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createFragments();

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.current_quote));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.all_quote));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter mAdapter = new PagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void createFragments() {
        if (mCurrentQuoteFragment == null) {
            mCurrentQuoteFragment = new MainCurrentQuoteFragment();
        }
        if (mAllQuoteFragment == null) {
            mAllQuoteFragment = new AllQuotesFragment();
        }
    }

    protected class PagerAdapter extends FragmentPagerAdapter {
        int numOfTabs;

        PagerAdapter(FragmentManager fm, int count) {
            super(fm);
            this.numOfTabs = count;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (mCurrentQuoteFragment == null) {
                        mCurrentQuoteFragment = new MainCurrentQuoteFragment();
                    }
                    return mCurrentQuoteFragment;
                case 1:
                    if (mAllQuoteFragment == null) {
                        mAllQuoteFragment = new AllQuotesFragment();
                    }
                    return mAllQuoteFragment;
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            return numOfTabs;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit: {
                this.finish();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
