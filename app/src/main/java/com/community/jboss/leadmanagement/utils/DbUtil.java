package com.community.jboss.leadmanagement.utils;

import android.content.Context;

import com.community.jboss.leadmanagement.data.LeadDatabase;
import com.community.jboss.leadmanagement.data.daos.ContactDao;
import com.community.jboss.leadmanagement.data.daos.ContactNumberDao;

public class DbUtil {
    private DbUtil() {
        // Prevent this util class from being instantiated
    }

    /**
     * @param context Current context
     * @return Data Access Object for Contact
     */
    public static ContactDao contactDao(Context context) {
        final LeadDatabase database = LeadDatabase.getInstance(context);
        return database.getContactDao();
    }

    /**
     * @param context Current context
     * @return Data Access Object for Contact Number
     */
    public static ContactNumberDao contactNumberDao(Context context) {
        final LeadDatabase database = LeadDatabase.getInstance(context);
        return database.getContactNumberDao();
    }
}
