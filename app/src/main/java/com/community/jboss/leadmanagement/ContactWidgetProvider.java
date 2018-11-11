package com.community.jboss.leadmanagement;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.community.jboss.leadmanagement.main.contacts.editcontact.EditContactActivity;

/**
 * App Widget that acts as a shortcut to EditContactActivity.
 * It enables user to add contact with a single tap.
 */
public class ContactWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, EditContactActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.contact_widget);
            views.setOnClickPendingIntent(R.id.widgetImage, pendingIntent);

            // For an unknown reason, setting the android:src attribute in the layout file
            // so we need to set it programatically
            views.setImageViewResource(R.id.widgetImage, R.mipmap.ic_person_add);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}

