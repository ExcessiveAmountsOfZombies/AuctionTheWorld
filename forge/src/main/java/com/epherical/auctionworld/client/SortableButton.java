package com.epherical.auctionworld.client;

import net.minecraft.client.gui.components.Button;

import java.util.Comparator;
import java.util.List;

public class SortableButton<T> {


    private final Button button;
    private boolean activated;
    private boolean reversed;
    private final Comparator<T> sorter;

    public SortableButton(boolean activated, Comparator<T> sorter, Button button) {
        this.activated = activated;
        this.sorter = sorter;
        this.button = button;
        this.reversed = false;
    }


    public Button getButton() {
        return button;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Comparator<T> getSorter() {
        return sorter;
    }

    public void sort(List<T> list) {
        if (activated) {
            list.sort(reversed ? sorter.reversed() : sorter);
        }
    }
}
