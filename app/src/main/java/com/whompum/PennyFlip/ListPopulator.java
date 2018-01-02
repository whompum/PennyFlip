package com.whompum.PennyFlip;

import com.whompum.PennyFlip.Statistics.Populator;

import java.util.List;

/**
 * Created by bryan on 12/31/2017.
 */

public interface ListPopulator<T> extends Populator<List<T>> {
        void populate(final List<T> data);
}
