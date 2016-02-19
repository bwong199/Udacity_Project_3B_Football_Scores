package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by benwong on 2016-02-11.
 */
public class ScoresWidget extends AppWidgetProvider {

    private SQLiteDatabase widgetScoreDatabase ;
    int dateIndex;
    int leagueIndex;
    int homeIndex;
    int awayIndex;
    int homeGoalIndex;
    int awayGoalIndex;
    String league;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;


        RemoteViews views  = new RemoteViews(context.getPackageName(), R.layout.scores_widget);
//        Intent intent = new Intent(context, ScoresWidget.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
//                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        views.setOnClickPendingIntent(R.id.wUpdateScores,pendingIntent );

        try {
            //testing to fetch data from database
            widgetScoreDatabase = SQLiteDatabase.openDatabase("/data/data/barqsoft.footballscores/databases/Scores.db", null, SQLiteDatabase.OPEN_READONLY);
            Log.i("dbpath", widgetScoreDatabase.getPath());
            String mDate;
            Date fragmentdate = new Date(System.currentTimeMillis());
            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
            mDate= mformat.format(fragmentdate);

            Log.i("todayDate", mDate);

            Cursor c = widgetScoreDatabase.rawQuery("SELECT * FROM scores_table"  , null);

            dateIndex = c.getColumnIndex("date");
            leagueIndex = c.getColumnIndex("league");
            homeIndex = c.getColumnIndex("home");
            awayIndex = c.getColumnIndex("away");
            homeGoalIndex = c.getColumnIndex("home_goals");
            awayGoalIndex = c.getColumnIndex("away_goals");

            if(c != null && c.moveToFirst()){

                do{
                    Log.i("gameinfo", c.getString(dateIndex) + " - " + getLeague(c.getString(leagueIndex)) + " - " + "home: " + c.getString(homeIndex) + " " + c.getString(homeGoalIndex) + " away: " + c.getString(awayIndex) + " " + c.getString(awayGoalIndex));
                    views.setTextViewText(R.id.wLeague, getLeague(c.getString(leagueIndex)));
                    views.setTextViewText(R.id.wDate, c.getString(dateIndex)  );
                    views.setTextViewText(R.id.homeTeam, c.getString(homeIndex));
                    views.setTextViewText(R.id.awayTeam, c.getString(awayIndex));
                    views.setTextViewText(R.id.homeScore, c.getString(homeGoalIndex) == null ? "0" :c.getString(homeGoalIndex));
                    views.setTextViewText(R.id.awayScore, c.getString(awayGoalIndex)== null? "0" :c.getString(awayGoalIndex));
                    if (c != null && c.moveToNext()){
                        views.setTextViewText(R.id.wLeague2, getLeague(c.getString(leagueIndex)));
                        views.setTextViewText(R.id.wDate2, c.getString(dateIndex));
                        views.setTextViewText(R.id.homeTeam2, c.getString(homeIndex));
                        views.setTextViewText(R.id.awayTeam2, c.getString(awayIndex));
                        views.setTextViewText(R.id.homeScore2, c.getString(homeGoalIndex)== null? "0" :c.getString(homeGoalIndex));
                        views.setTextViewText(R.id.awayScore2, c.getString(awayGoalIndex)== null? "0" :c.getString(awayGoalIndex));
                    }
                    if (c != null && c.moveToNext()){
                        views.setTextViewText(R.id.wLeague3, getLeague(c.getString(leagueIndex)));
                        views.setTextViewText(R.id.wDate3, c.getString(dateIndex));
                        views.setTextViewText(R.id.homeTeam3, c.getString(homeIndex));
                        views.setTextViewText(R.id.awayTeam3, c.getString(awayIndex));
                        views.setTextViewText(R.id.homeScore3, c.getString(homeGoalIndex)== null? "0" :c.getString(homeGoalIndex));
                        views.setTextViewText(R.id.awayScore3, c.getString(awayGoalIndex)== null? "0" :c.getString(awayGoalIndex));
                    }
                } while(c.moveToNext());
                c.close();
            }
        } catch(Error e){
            e.printStackTrace();
        }

        for (int i = 0; i < count; i++) {

//            String text = "Data: " + new Random().nextInt(1000);



//            int widgetId = appWidgetIds[i];
//            String number = String.format("%03d", (new Random().nextInt(900) + 100));
//
//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
//                    R.layout.scores_widget);
//            remoteViews.setTextViewText(R.id.textView, number);
//
//            Intent intent = new Intent(context, ScoresWidget.class);
//            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
//                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
    }

    public String getLeague(String leagueNumber){
        switch (leagueNumber){
            case "394": return "BUNDESLIGA1";

            case "395": return "BUNDESLIGA2";

            case "396": return "LIGUE1";

            case "397": return "LIGUE2";

            case "398": return "PREMIER_LEAGUE";

            case "399": return "PRIMERA_DIVISION";

            case "400": return "SEGUNDA_DIVISION";

            case "401": return "SERIE_A";

            case "402": return "PRIMERA_LIGA";

            case "403": return "Bundesliga3";

            case "404": return "EREDIVISIE";

            default:
                return "no league found";
        }
    }
}
