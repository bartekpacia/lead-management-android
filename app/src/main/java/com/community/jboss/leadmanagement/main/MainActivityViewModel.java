package com.community.jboss.leadmanagement.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;


class MainActivityViewModel extends ViewModel {
    private MutableLiveData<NavigationItem> mSelectedNavItem = new MutableLiveData<>();

    LiveData<NavigationItem> getSelectedNavItem() {
        return mSelectedNavItem;
    }

    void setSelectedNavItem(NavigationItem item) {
        mSelectedNavItem.setValue(item);
    }

    enum NavigationItem {
        CONTACTS,
        GROUPS,
        SETTINGS,
        TOGGLE_THEME,
        LIGHT_THEME
    }
}
