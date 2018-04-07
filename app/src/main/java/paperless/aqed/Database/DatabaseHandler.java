package paperless.aqed.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import paperless.aqed.Util.Quote;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHandler.class.getSimpleName();
    private static final int DATABASE_VERSION = 2;

    private static int quotesDbCountOpen;

    // Database Name
    private static final String DATABASE_NAME = "quotesManager.db";

    private static final String TABLE_QUOTES = "quotes";
    private static final String TABLE_USERDATA = "user_data";

    // Quotes Table Columns
    private static final String KEY_ID = "_id";
    private static final String KEY_QUOTE = "quote";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_WIKILINK = "wikiLink";
    private static final String KEY_IS_READ = "is_read";


    // User Data Table Columns
    private static final String KEY_IS_PREMIUM = "isPremium";
    private static final String KEY_CURRENT_JSON_VERSION = "current_jsonVersion";
    private static final String KEY_DURATION_TO_CHANGE_NOTIFICATION = "notification_change_duration";
    private static final String KEY_ALWAYS_PIN_TO_NOTIFICATION = "always_pin_to_notification";
    private static final String KEY_TODAYS_QUOTE_ID = "todays_quote_id";


    private String CREATE_QUOTES_TABLE = "CREATE TABLE " + TABLE_QUOTES + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_QUOTE + " TEXT, "
            + KEY_AUTHOR + " TEXT, "
            + KEY_WIKILINK + " TEXT, "
            + KEY_IS_READ + " INTEGER);";

    private String CREATE_USER_DATA_TABLE = "CREATE TABLE " + TABLE_USERDATA + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_IS_PREMIUM + " INTEGER,"
            + KEY_CURRENT_JSON_VERSION + " INTEGER,"
            + KEY_DURATION_TO_CHANGE_NOTIFICATION + " INTEGER,"
            + KEY_ALWAYS_PIN_TO_NOTIFICATION + " INTEGER,"
            + KEY_TODAYS_QUOTE_ID + " INTEGER" + ")";

    private static DatabaseHandler sInstance;

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHandler getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHandler(context);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_DATA_TABLE);
        sqLiteDatabase.execSQL(CREATE_QUOTES_TABLE);
        initializeInitialUserData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUOTES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERDATA);
        onCreate(sqLiteDatabase);
    }

    //Adding new Quote
    void addQuote(Quote quote) {
        Log.d(TAG, "Add Quote Called");
        try {
            if (quoteAlreadyPresent(quote.getQuoteText())) {
                Log.d(TAG, "Quote Already Present");
                return;
            }
        }catch(IllegalStateException e) {
            e.printStackTrace();
        }

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUOTE, quote.getQuoteText());
        values.put(KEY_AUTHOR, quote.getAuthor());
        values.put(KEY_WIKILINK, quote.getWikiLink());
        values.put(KEY_IS_READ, String.valueOf(0));

        db.insert(TABLE_QUOTES, null, values);
        close();
    }

    //Getting Single random Unread Quote
    public Quote getRandomQuote() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_QUOTES, new String[]{KEY_ID, KEY_QUOTE,
                        KEY_AUTHOR, KEY_WIKILINK, KEY_IS_READ}, KEY_IS_READ + "=0", null, null,
                null, null, String.valueOf(1));

        if (c != null) {
            c.moveToFirst();
            Log.d(TAG,"Cursor Count = "+c.getCount()+" ,Id = "+c.getInt(c.getColumnIndex(KEY_ID)));
            Quote quote = new Quote(c.getInt(c.getColumnIndex(KEY_ID)),c.getString(c.getColumnIndex(KEY_QUOTE)),
                    c.getString(c.getColumnIndex(KEY_AUTHOR)), c.getString(c.getColumnIndex(KEY_WIKILINK)));
            updateReadStatus(quote);

            /*Cursor c1 = db.query(TABLE_QUOTES, new String[]{KEY_IS_READ}, KEY_ID + "=" + 0, null, null,
                    null, null, String.valueOf(1));*/
            c.close();
            return quote;
        }
        return null;
    }

    //Getting Quote of a specific author
    public List<Quote> getQuoteForAuthor(String authorName) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Quote> quoteList = new ArrayList<>();
        Cursor c = db.query(TABLE_QUOTES, new String[]{KEY_QUOTE,
                        KEY_AUTHOR, KEY_WIKILINK}, KEY_AUTHOR + "=?", new String[]{authorName}, null,
                null, null, null);

        if (c != null) {
            c.moveToFirst();

            do {
                Quote quote = new Quote();
                quote.setQuoteText(c.getString(0));
                quote.setAuthor(c.getString(1));
                quote.setWikiLink(c.getString(2));

                quoteList.add(quote);
            } while (c.moveToNext());
            c.close();
        }
        //db.close();
        return quoteList;
    }

    //Getting ALl Quotes
    public List<Quote> getAllQuotes() {
        List<Quote> quoteList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_QUOTES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Quote contact = new Quote();
                Quote quote = new Quote();
                quote.setQuoteText(c.getString(1));
                quote.setAuthor(c.getString(2));
                quote.setWikiLink(c.getString(3));

                quoteList.add(contact);
            } while (c.moveToNext());
            c.close();
        }
        //db.close();
        return quoteList;
    }

    public Cursor getAllQuotesCursor() {
        String selectQuery = "SELECT * FROM " + TABLE_QUOTES;
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    public Cursor getQuotesCursorBySearch(String queryText) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c1 = db.query(TABLE_QUOTES, new String[]{KEY_ID, KEY_QUOTE, KEY_AUTHOR,
                        KEY_WIKILINK, KEY_IS_READ},
                KEY_QUOTE + " LIKE ?", new String[]{"%" + queryText + "%"}, null, null, null);

        Cursor c2 = db.query(TABLE_QUOTES, new String[]{KEY_ID, KEY_QUOTE, KEY_AUTHOR,
                KEY_WIKILINK, KEY_IS_READ}, KEY_AUTHOR + " LIKE ?", new String[]{
                "%" + queryText + "%",}, null, null, null);
        Cursor c = new MergeCursor(new Cursor[]{c1, c2});
        c1.close();
        c2.close();
        return c;
    }

    // Getting Quotes Count
    public int getQuotesCount() {
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABLE_QUOTES;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            count = c.getCount();
            c.close();
            Log.d(TAG, "Quotes Count = " + count);
        }
        return count;
    }

    // Update Any Quote by id
    public void updateQuote(int id, Quote newQuote) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUOTE, newQuote.getQuoteText());
        values.put(KEY_AUTHOR, newQuote.getAuthor());
        values.put(KEY_WIKILINK, newQuote.getWikiLink());
        values.put(KEY_IS_READ, String.valueOf(0));

        db.update(TABLE_QUOTES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        //db.close();
    }

    //Deleting single Quote
    public void deleteQoute(Quote quote) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUOTES, KEY_ID + " = ?",
                new String[]{String.valueOf(quote.getID())});
        //db.close();
    }

    //Deleting allQuotes
    public void deleteAllQuotes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUOTES, null, null);
        //db.close();
    }

    //update Read Status of a Quote
    private void updateReadStatus(Quote quote) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUOTE, quote.getQuoteText());
        values.put(KEY_AUTHOR, quote.getAuthor());
        values.put(KEY_WIKILINK, quote.getWikiLink());
        values.put(KEY_IS_READ, String.valueOf(1));

        Log.d(TAG,"ID To be Updated = "+quote.getID());

        db.update(TABLE_QUOTES, values, KEY_ID + "=" + quote.getID(), null);
    }

    private boolean quoteAlreadyPresent(String quoteText) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_QUOTES, new String[]{KEY_ID}, KEY_QUOTE + "=?", new String[]{quoteText}, null,
                null, null, null);
        if (c != null && c.getCount() > 0) {
            c.close();
            return true;
        }
        return false;
    }

    private void initializeInitialUserData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(KEY_IS_PREMIUM, 0);
        values.put(KEY_CURRENT_JSON_VERSION, 0);
        values.put(KEY_DURATION_TO_CHANGE_NOTIFICATION, 24);
        values.put(KEY_ALWAYS_PIN_TO_NOTIFICATION, 0);
        values.put(KEY_TODAYS_QUOTE_ID, -1);

        db.insert(TABLE_USERDATA, null, values);
        Log.d(TAG, "DB Insertion Done");
    }

    public int getFieldFromUserDataTable(String field) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USERDATA;
        Cursor c = db.rawQuery(selectQuery, null);
        //db.query(TABLE_USERDATA, new String[] {field},null,null,null, null,null);
        int val = 0;
        if (c != null) {
            Log.d(TAG, "Cursor Count = " + c.getCount());
            c.moveToFirst();
            val = c.getInt(c.getColumnIndex(KEY_CURRENT_JSON_VERSION));
            c.close();
        }
        return val;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        quotesDbCountOpen ++;
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase(){
        quotesDbCountOpen++;
        return super.getReadableDatabase();
    }

    @Override
    public void close(){
        if(quotesDbCountOpen > 1) {
            quotesDbCountOpen--;
            super.close();
        }
    }
}
