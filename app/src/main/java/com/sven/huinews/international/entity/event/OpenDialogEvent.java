package com.sven.huinews.international.entity.event;

public class OpenDialogEvent {
    public OpenDialogEvent(int gold) {
        this.gold = gold;
    }

    private int gold;

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}
