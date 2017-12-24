package com.whompum.PennyFlip.SourceDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryan on 12/21/2017.
 */

public class StaticList {



    private static SourceWrapper.TAG N = SourceWrapper.TAG.NEW;
    private static SourceWrapper.TAG B = SourceWrapper.TAG.BASE;
    private static SourceWrapper.TAG S = SourceWrapper.TAG.SUBSOURCE;

    private static AdapterSelecteable<SourceWrapper> dataSet = new AdapterSelecteable<>();


    static{
        dataSet.add(new SourceWrapper("Lorem", S));
        dataSet.add(new SourceWrapper("BaseBall", S));
        dataSet.add(new SourceWrapper("Caption Jack", B));
        dataSet.add(new SourceWrapper("Bitch", B));
        dataSet.add(new SourceWrapper("Boats n' hoes", S));
        dataSet.add(new SourceWrapper("more boats and hoes", S));
        dataSet.add(new SourceWrapper("uno", B));
        dataSet.add(new SourceWrapper("dos", B));
        dataSet.add(new SourceWrapper("cuidado", B));
        dataSet.add(new SourceWrapper("vehetariano", S));
        dataSet.add(new SourceWrapper("salchicha", S));
        dataSet.add(new SourceWrapper("salsa", B));
        dataSet.add(new SourceWrapper("i need a job", B));
        dataSet.add(new SourceWrapper("bored", S));
        dataSet.add(new SourceWrapper("damn", S));
        dataSet.add(new SourceWrapper("hello", B));
        dataSet.add(new SourceWrapper("hello again.", B));
        dataSet.add(new SourceWrapper("is this the real life", S));
        dataSet.add(new SourceWrapper("or is this just fantasy", S));
        dataSet.add(new SourceWrapper("caught in a landslide", B));
        dataSet.add(new SourceWrapper("whyd you have to go", B));
        dataSet.add(new SourceWrapper("and make things so complicated", B));
        dataSet.add(new SourceWrapper("bored", B));
        dataSet.add(new SourceWrapper("blah", S));
        dataSet.add(new SourceWrapper("almost done", S));
        dataSet.add(new SourceWrapper("random", S));
        dataSet.add(new SourceWrapper("bonita", B));
        dataSet.add(new SourceWrapper("pennyFlip", B));
    }


    public static AdapterSelecteable<SourceWrapper> get(){
        return dataSet;
    }

}
