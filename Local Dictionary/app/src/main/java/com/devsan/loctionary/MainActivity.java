package com.devsan.loctionary;

import android.content.*;
import android.content.pm.*;
import android.database.*;
import android.net.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.view.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import com.devsan.loctionary.*;
//import com.google.android.gms.ads.*;
import java.io.*;
import java.util.regex.*;
import de.cketti.library.changelog.*;


//DevSan : Sandipan

public class MainActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener
{

    private Toolbar toolbar;
    private DrawerLayout d;
	private NavigationView n;

	SearchView search;

    static DatabaseHelper myDbHelper;
    static boolean databaseOpened=false;

    SimpleCursorAdapter suggestionAdapter;

	//private AdView adView;

	//private InterstitialAd interstitialAd;

	DatabaseHelper db;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		myDbHelper = new DatabaseHelper(MainActivity.this);
        toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        d = findViewById(R.id.mainDrawerLayout);
		n = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		myDbHelper = new DatabaseHelper(MainActivity.this);
		//db.dbCheck();
        //long id = 0;
		//int profile_counts = myDbHelper.getTaskCount(id);
		//Log.d("DevSan", "Bro you have " + profile_counts + " words in your database");
        ActionBarDrawerToggle t = new ActionBarDrawerToggle(this, d, toolbar, R.string.app_name, R.string.app_name);
		d.setDrawerListener(t);
		t.syncState();
		n.setNavigationItemSelectedListener(this);
		n.setItemIconTintList(null); // menu item icons color set
        search =  (SearchView) findViewById(R.id.search_view);
		search.setBackgroundResource(R.drawable.custom_search_view);

		//change log
		ChangeLog cl = new ChangeLog(this);
        if (cl.isFirstRun())
		{
            cl.getLogDialog().show();
		}
		//Make package name folder in android folder 
		File file = this.getBaseContext().getExternalFilesDir("DevSan");
		if (!file.exists())
			file.mkdir();

        search.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					search.setIconified(false);

				}
			});


		myDbHelper = new DatabaseHelper(this);

        if (myDbHelper.checkDataBase())
        {
            openDatabase();

        }
        else
        {
            LoadDatabaseAsync task = new LoadDatabaseAsync(MainActivity.this);
            task.execute();
        }



        // setup SimpleCursorAdapter

        final String[] from = new String[] {"en_word"};
        final int[] to = new int[] {R.id.suggestion_text};

        suggestionAdapter = new SimpleCursorAdapter(MainActivity.this,
													R.layout.suggestion_row, null, from, to, 0){
            @Override
            public void changeCursor(Cursor cursor)
			{
                super.swapCursor(cursor);
            }

        };

		search.setSuggestionsAdapter(suggestionAdapter);


        search.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
				@Override
				public boolean onSuggestionClick(int position)
				{

					// Add clicked text to search box
					CursorAdapter ca = search.getSuggestionsAdapter();
					Cursor cursor = ca.getCursor();
					cursor.moveToPosition(position);
					String clicked_word =  cursor.getString(cursor.getColumnIndex("en_word"));
					search.setQuery(clicked_word, false);

					//search.setQuery("",false);

					search.clearFocus();
					search.setFocusable(false);

					Intent intent = new Intent(MainActivity.this, WordMeaningActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("en_word", clicked_word);
					intent.putExtras(bundle);
					startActivity(intent);

					return true;
				}

				@Override
				public boolean onSuggestionSelect(int position)
				{
					// Your code here
					return true;
				}
			});


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

				private java.lang.CharSequence out;

				private java.lang.CharSequence sugg;
				@Override
				public boolean onQueryTextSubmit(String query)
				{

					String text =  search.getQuery().toString();

					try
					{
						out = new String(text.getBytes("UTF-8"), "ISO-8859-1");
					}
					catch (UnsupportedEncodingException e)
					{}

					Pattern p = Pattern.compile("[^A-Za-z \\-.]{1,25}");
					Matcher m = p.matcher(out);

					if (m.matches())
					{
						Cursor c = myDbHelper.getMeaning(text);

						if (c.getCount() == 0)
						{
							showAlertDialog();
						}

						else
						{
							//search.setQuery("",false);
							search.clearFocus();
							search.setFocusable(false);

							Intent intent = new Intent(MainActivity.this, WordMeaningActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("en_word", text);
							intent.putExtras(bundle);
							startActivity(intent);

						}
					}

					else
					{
						showAlertDialog();
					}



					return false;
				}


				@Override
				public boolean onQueryTextChange(final String s)
				{
					search.setIconifiedByDefault(false); //Give Suggestion list margins

					try
					{
						sugg = new String(s.getBytes("UTF-8"), "ISO-8859-1");
					}
					catch (UnsupportedEncodingException e)
					{}

					Pattern p = Pattern.compile("[^A-Za-z \\-.]{1,25}");
					Matcher m = p.matcher(sugg);

					if (m.matches())
					{
						Cursor cursorSuggestion=myDbHelper.getSuggestions(s);
						suggestionAdapter.changeCursor(cursorSuggestion);
					}


					return false;
				}

			});	


	}
	protected static void openDatabase()
    {
        try
		{
            myDbHelper.openDataBase();
            databaseOpened = true;
        }
		catch (SQLException e)
		{
            e.printStackTrace();
        }
    }



	private void showAlertDialog()
    {
        search.setQuery("", false);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Word Not Found");
        builder.setMessage("Please search again");

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// positive button logic
				}
			});

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					search.clearFocus();
				}
			});

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();

    }

	@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
	{
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home)
		{
            // Handle the camera action
        }
		else if (id == R.id.dev)
		{

			Intent intent=new Intent(MainActivity.this, DeveloperActivity.class);
			startActivity(intent);
			return true;

        }
		else if (id == R.id.about)
		{
			Intent intent_a=new Intent(MainActivity.this, AboutActivity.class);
			startActivity(intent_a);
			return true; 

        }
		else if (id == R.id.contribute)
		{

			Intent intent_c=new Intent(MainActivity.this, ContributionActivity.class);
			startActivity(intent_c);
			return true;

		}
		else if (id == R.id.share)
		{

			ApplicationInfo api = getApplicationContext().getApplicationInfo();
			String filePath = api.sourceDir;

			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("application/vnd.android.package-archive");

			intent.putExtra(Intent.EXTRA_STREAM, 
							Uri.parse(filePath));
			startActivity(Intent.createChooser(intent, "Share Using..."));

		}
		else if (id == R.id.more)
		{

			Uri uri = Uri.parse("https://devsancom.blogspot.com");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}


        d.closeDrawer(GravityCompat.START);
        return true;
    }

	@Override
    public void onBackPressed()
	{

		super.onBackPressed();

	}

	public static class DarkThemeChangeLog extends ChangeLog
	{
        public static final String DARK_THEME_CSS =
		"body { color: #ffffff; background-color: #282828; }" + "\n" + DEFAULT_CSS;

        public DarkThemeChangeLog(Context context)
		{
            super(new ContextThemeWrapper(context, R.style.DarkTheme), DARK_THEME_CSS);
        }
	}}
