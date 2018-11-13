package com.community.jboss.leadmanagement;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.community.jboss.leadmanagement.main.MainActivity;

import java.util.Arrays;

/**
 * Implementation of App Widget functionality.
 */
public class RecentContactsWidget extends AppWidgetProvider {

    public static final String TAG = "RecentContactsWidget";
    public static final String WIDGET_IDS_KEY = "recentcontactswidgetproviderid";
    public static final String LAUNCH_APP = "com.community.jboss.leadmanegement.LAUNCH_APP";
    public static final String VIEW_INDEX_CLICKED = "com.community.jboss.leadmanegement.VIEW_INDEX_CLICKED";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (LAUNCH_APP.equals(intent.getAction())) {
            //ListView item was clicked, launch app
            int viewIndex = intent.getIntExtra(VIEW_INDEX_CLICKED, -100);

            //create intent to launch app
            intent = new Intent(context, MainActivity.class);
            intent.putExtra(VIEW_INDEX_CLICKED, viewIndex);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        if (intent.hasExtra(WIDGET_IDS_KEY)) {
            //update widget's content
            int[] ids = intent.getIntArrayExtra(WIDGET_IDS_KEY);
            Log.d(TAG, "Broadcast received! Widgets Ids: " + Arrays.toString(ids));
            update(context, AppWidgetManager.getInstance(context), ids);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate()");
        update(context, appWidgetManager, appWidgetIds);

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public void update(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "update()");

        for (int appWidgetId : appWidgetIds) {
            Log.d(TAG, "Updating app widget with id " + String.valueOf(appWidgetId));

            //setting up the intent that launches a service
            Intent intent = new Intent(context, RecentContactsService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            //setting up the adapter
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recent_contacts_widget);
            views.setRemoteAdapter(R.id.listView_recentContacts, intent);

            //setting up the on-click intent
            Intent toastIntent = new Intent(context, RecentContactsWidget.class);
            toastIntent.setAction(RecentContactsWidget.LAUNCH_APP);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            toastIntent.setData(Uri.parse(toastIntent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.listView_recentContacts, toastPendingIntent);

            //update widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listView_recentContacts);
        }
    }

    /**
     * Use this to update widget from inside the app.
     *
     * @param context Current context
     */
    public static void updateContactsWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecentContactsWidget.class));
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(RecentContactsWidget.WIDGET_IDS_KEY, ids);

        context.sendBroadcast(updateIntent);
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "Widget enabled!");
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "Widget disabled!");
    }
}

