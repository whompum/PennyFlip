package com.whompum.PennyFlip.Data;

import android.os.Bundle;

/**
 * Created by bryan on 1/31/2018.
 */

public interface LoaderArgs {

    int DEFAULT_LOADER_ID = 1;

    String WHERE_KEY = "where.ky";
    String WHERE_ARGS_KEY = "whereArgs.ky";
    String SORT_ORDER_KEY = "sortOrder.ky";
    String PROJECTION_KEY = "projection.ky";

    Bundle loaderArgs = new Bundle();

    void generateDefaultLoaderArgs();

}
