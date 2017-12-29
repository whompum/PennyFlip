package com.whompum.PennyFlip.Source.SourceDialog;


/**
 * Created by bryan on 12/21/2017.
 */

public class SourceWrapper {

    public static final String EMPTY = "EMPTY";

    private String sourceTitle;
    private TAG tag;

    public SourceWrapper(String title, final TAG tag){

        if(title.equals(""))
            title = EMPTY;

        this.sourceTitle = title;
        this.tag = tag;
    }

    public String getTitle(){
        return sourceTitle;
    }
    public TAG getTagType(){
        return tag;
    }


    public String getTag(final TAG tag){

        if(tag == TAG.BASE)
            return "BASE";
        else if(tag == TAG.SUBSOURCE)
            return "SUBSOURCE";

        else if(tag == TAG.NEW)
            return "NEW";

    return "DEFAULT";
    }
    public String getTag(){
        return getTag(tag);
    }


    public enum TAG {BASE, SUBSOURCE, NEW}


}
