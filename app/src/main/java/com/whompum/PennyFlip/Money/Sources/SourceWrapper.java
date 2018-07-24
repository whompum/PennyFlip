package com.whompum.PennyFlip.Money.Sources;

import java.io.Serializable;

public class SourceWrapper implements Serializable{

    /**
     * STATE DECLARATIONS
     *
     * @state EMPTY: Used so the adapter won't display the object if it the search is empty (Source can't have "" for a title)
     * @state sourceTitle: The literal title of the source object
     * @state sourceId: The _ID of the source object in the Database;
     * @state tag: The tag is the type of source this object represents. E.G. if the user wants to write a new source, for a new
     *             transaction, the 'NEW' tag is used for that purpose.
     */

    public static final String EMPTY = "EMPTY";

    private String sourceTitle;
    private long sourceId;
    private TAG tag;
    private int sourceType;
    private long originalAmount;




    /**
     * Used to create a Source wrapper that hasn't been in storage yet
     * @param title
     * @param tag
     * @param sourceType
     */
    public SourceWrapper(final String title, final TAG tag, final int sourceType){
        this(title, tag, sourceType, Integer.MIN_VALUE, 0L);
    }


    /**
     * This method should be used when creating a complete SourceWrapper object from a database
     * @param title
     * @param sourceId
     * @param tag
     * @param sourceType
     * @param originalAmount
     */
    public SourceWrapper(String title, final TAG tag, final int sourceType, final long sourceId, final long originalAmount){

        if(title.equals(""))
            title = EMPTY;

        this.sourceTitle = title;
        this.tag = tag;
        this.sourceId = sourceId;
        this.sourceType = sourceType;
        this.originalAmount = originalAmount;
    }

    public String getTitle(){
        return sourceTitle;
    }
    public TAG getTagType(){
        return tag;
    }

    public long getSourceId(){
        return sourceId;
    }

    public int getSourceType(){
        return sourceType;
    }

    public void setSourceId(final long id){
        this.sourceId = id;
    }

    public long getOriginalAmount(){
        return originalAmount;
    }

    public enum TAG {REGULAR, NEW}


}
