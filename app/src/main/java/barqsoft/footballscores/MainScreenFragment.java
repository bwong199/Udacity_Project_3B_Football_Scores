package barqsoft.footballscores;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.service.myFetchService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    public scoresAdapter mAdapter;
    public static final int SCORES_LOADER = 0;
    private String[] fragmentdate = new String[1];
    private int last_selected_item = -1;
    private SQLiteDatabase widgetScoreDatabase ;


    public MainScreenFragment()
    {
    }

    private void update_scores()
    {
        Intent service_start = new Intent(getActivity(), myFetchService.class);
        getActivity().startService(service_start);
    }
    public void setFragmentDate(String date)
    {
        fragmentdate[0] = date;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        update_scores();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final ListView score_list = (ListView) rootView.findViewById(R.id.scores_list);
        mAdapter = new scoresAdapter(getActivity(),null,0);
        score_list.setAdapter(mAdapter);
        getLoaderManager().initLoader(SCORES_LOADER,null,this);
        mAdapter.detail_match_id = MainActivity.selected_match_id;
        score_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                ViewHolder selected = (ViewHolder) view.getTag();
                mAdapter.detail_match_id = selected.match_id;
                MainActivity.selected_match_id = (int) selected.match_id;
                mAdapter.notifyDataSetChanged();
            }
        });

        try {
            //testing to fetch data from database
            boolean inEmulator = "generic".equals(Build.BRAND.toLowerCase());

            if(inEmulator){
                widgetScoreDatabase = SQLiteDatabase.openDatabase("/data/data/barqsoft.footballscores/databases/Scores.db", null, SQLiteDatabase.OPEN_READONLY);

            } else {
                widgetScoreDatabase = SQLiteDatabase.openDatabase("/data/data/barqsoft.footballscores/databases/Scores.db", null, SQLiteDatabase.OPEN_READONLY);
     //
            }

            String mDate;
            Date fragmentdate = new Date(System.currentTimeMillis());
            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
            mDate= mformat.format(fragmentdate);

//            Log.i("todayDate", mDate);

            Cursor c = widgetScoreDatabase.rawQuery("SELECT * FROM scores_table"  , null);

            int dateIndex = c.getColumnIndex("date");
            int leagueIndex = c.getColumnIndex("league");
            int homeIndex = c.getColumnIndex("home");
            int awayIndex = c.getColumnIndex("away");
            int homeGoalIndex = c.getColumnIndex("home_goals");
            int awayGoalIndex = c.getColumnIndex("away_goals");
            String league;

            if(c != null && c.moveToFirst()){


                do{
                    Log.i("gameinfo", c.getString(dateIndex) + " - "  + " - " + "home: " + c.getString(homeIndex) + " " + c.getString(homeGoalIndex) + " away: " + c.getString(awayIndex) + " " + c.getString(awayGoalIndex));

                } while(c.moveToNext());
            }
        } catch(Error e){
            e.printStackTrace();
        }


        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        return new CursorLoader(getActivity(),DatabaseContract.scores_table.buildScoreWithDate(),
                null,null,fragmentdate,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
    {
        //Log.v(FetchScoreTask.LOG_TAG,"loader finished");
        //cursor.moveToFirst();
        /*
        while (!cursor.isAfterLast())
        {
            Log.v(FetchScoreTask.LOG_TAG,cursor.getString(1));
            cursor.moveToNext();
        }
        */

        int i = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            i++;
            cursor.moveToNext();
        }
        //Log.v(FetchScoreTask.LOG_TAG,"Loader query: " + String.valueOf(i));
        mAdapter.swapCursor(cursor);
        //mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader)
    {
        mAdapter.swapCursor(null);
    }


}
