package com.community.jboss.leadmanagement;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.community.jboss.leadmanagement.data.daos.ContactNumberDao;
import com.community.jboss.leadmanagement.data.entities.Contact;
import com.community.jboss.leadmanagement.main.MainActivity;
import com.community.jboss.leadmanagement.utils.DbUtil;

import java.util.ArrayList;
import java.util.List;

public class RecentContactsService extends RemoteViewsService {

    private static final String TAG = "RecentContactsService";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "The service is returning RemoteViewsFactory...");
        return new RecentContactsViewsFactory(getApplicationContext(), intent);
    }

    class RecentContactsViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private static final String TAG = "RecentContactsViewsFactory";
        private Context context;
        private int appWidgetId;

        private List<Contact> contactList;
        private List<String> numberList;

        RecentContactsViewsFactory(Context applicationContext, Intent intent) {
            this.context = applicationContext;

            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            getContacts();
            getContactNumbers();

            log();
        }

        @Override
        public void onCreate() {
            getContacts();
            getContactNumbers();
        }

        @Override
        public void onDataSetChanged() {
            Log.d(TAG, "onDataSetChanged()");
            getContacts();
            getContactNumbers();
        }

        @Override
        public void onDestroy() {
            contactList.clear();
            numberList.clear();
        }

        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Log.d(TAG, "Creating RemoteView with index " + position + "...");

            Contact contact = contactList.get(position);
            Bitmap bitmap = BitmapFactory.decodeByteArray(contact.getImage(), 0, contact.getImage().length);
            String name = contact.getName();
            String number = numberList.get(position);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.contact_cell_widget);
            views.setTextViewText(R.id.contact_name_widget, name);
            views.setTextViewText(R.id.contact_number_widget, number);
            views.setImageViewBitmap(R.id.contact_avatar_widget, bitmap);

            // Next, set a fill-intent, which will be used to fill in the pending intent template
            // that is set on the collection view in StackWidgetProvider.
            Bundle extras = new Bundle();
            extras.putInt(RecentContactsWidget.VIEW_INDEX_CLICKED, position);
            Intent fillInIntent = new Intent(context, MainActivity.class);
            fillInIntent.putExtras(extras);
            // Make it possible to distinguish the individual on-click
            // action of a given item
            views.setOnClickFillInIntent(R.id.temp_name, fillInIntent);
            views.setOnClickFillInIntent(R.id.contact_avatar_widget, fillInIntent);
            return views;
        }


        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        /**
         * @return The list of Contacts
         */
        private List<Contact> getContacts() {
            contactList = DbUtil.contactDao(context).getContacts();

            return contactList;
        }

        /**
         * Gets the Contact's first number (with 0 index). *MUST* be called after {@link #getContacts()}.
         *
         * @return List of Contact's numbers whose index corresponds to the indexes of the contact list
         */
        private List<String> getContactNumbers() {
            numberList = new ArrayList<>();

            for (Contact contact : contactList) {
                final ContactNumberDao dao = DbUtil.contactNumberDao(context);
                String contactNumber = dao.getContactNumbers(contact.getId()).get(0).getNumber();
                numberList.add(contactNumber);
            }

            return numberList;
        }

        /**
         * Just print the list of contacts and their numbers to the console
         */
        private void log() {
            for (int i = 0; i < contactList.size(); i++) {
                Contact contact = contactList.get(i);
                String number = numberList.get(i);
                Log.d(TAG, "Name: " + contact.getName() + ", number: " + number);
            }
        }
    }
}
