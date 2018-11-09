package com.sven.huinews.international.entity.response;

import java.io.Serializable;

public class HomeTab implements Serializable {
    private String tabName;
    private int tabType;

    public HomeTab(String tabName, int tabType) {
        this.tabName = tabName;
        this.tabType = tabType;
    }

    public HomeTab() {
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public int getTabType() {
        return tabType;
    }

    public void setTabType(int tabType) {
        this.tabType = tabType;
    }
}
