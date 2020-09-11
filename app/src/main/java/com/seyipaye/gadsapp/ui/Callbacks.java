package com.seyipaye.gadsapp.ui;


import com.seyipaye.gadsapp.ui.main.MainRVItem;

import java.util.List;

public class Callbacks {
    public interface Leaders {
        void onFetchStarted();
        void onFetchSuccess(List<MainRVItem> mainRVItemList);
        void onFetchFailure(String message);
    }

    public interface Form {
        void onSuccess(String message);
        void onFailure(String message);
    }

    public interface Yes {
        void onClicked();
    }
}
