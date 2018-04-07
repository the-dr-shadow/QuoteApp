package paperless.aqed.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import paperless.aqed.Database.DatabaseHandler;
import paperless.aqed.Database.DbUpdation;

public class QuotesUtil {

    private static final String TAG = QuotesUtil.class.getSimpleName();
    private static Boolean firstTime = null;

    public static void checkJsonUpdate(Context context) {
        // Fetch the version of the JSON File present at the server
        // If the fetched version is more the the one present in User Data Table
        if(!FloatingFeatureUtils.isServerFunctioning()) {
            return;
        }
        startJsonUpdateProcess(context);
    }

    private static void startJsonUpdateProcess(Context context) {
        downloadNewFileIfAvailable(context);
        DbUpdation.makeList(context);
    }

    private static void downloadNewFileIfAvailable(Context context) {
        int json_version = 0; //this value need to be fetched from Server side
        DatabaseHandler dbHandler = DatabaseHandler.getsInstance(context);
        if (json_version > dbHandler.getFieldFromUserDataTable(Constants.KEY_CURRENT_JSON_VERSION)) {
            //proceed with Downloading the new Json File
        }
    }

    public static boolean isFirstTime(Context context) {
        if (firstTime == null) {
            SharedPreferences mPreferences = context.getSharedPreferences(Constants.sharedPrefernce, Context.MODE_PRIVATE);
            firstTime = mPreferences.getBoolean(Constants.firstTimePrefernce, true);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(Constants.firstTimePrefernce, false);
                editor.apply();
            }
        }
        return firstTime;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}