package com.whompum.PennyFlip.DialogSourceChooser;


import java.util.ArrayList;

/**
 * Tiny helper class for when we're selecting items from the RecyclerView, and need to cache which item is selected
 */

public class AdapterSelecteable<E> extends ArrayList<E> {

    private E selecteable;

    public AdapterSelecteable(){
        super();
    }

    public void removeSelected(){
        selecteable = null;
    }

    public void setSelected(final E e){
        this.selecteable = e;
    }

    public boolean isSelected(final E e){
        return e.equals(selecteable);
    }
}
