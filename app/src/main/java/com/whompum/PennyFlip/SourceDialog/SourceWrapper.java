package com.whompum.PennyFlip.SourceDialog;


/**
 * Created by bryan on 12/21/2017.
 */

public class SourceWrapper {

    private String sourceTitle;

    private TAG tag;

    public SourceWrapper(final String title, final TAG tag){
        this.sourceTitle = title;
        this.tag = tag;
    }

    public String getTitle(){
        return sourceTitle;
    }

    public String getTag(final TAG tag){

        if(tag == TAG.BASE)
            return "BASE";
        else if(tag == TAG.SUBSOURCE)
            return "SUBSOURCE";
        else return "NEW";


    }

    public String getTag(){
        return getTag(tag);
    }

    public TAG getTagType(){
        return tag;
    }

    public enum TAG {BASE, SUBSOURCE, NEW}



}
