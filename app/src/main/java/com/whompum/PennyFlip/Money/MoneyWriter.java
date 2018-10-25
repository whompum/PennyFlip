package com.whompum.PennyFlip.Money;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

/**
 *  Defines the available write operations on {@link Transaction}, {@link Source}, and
 * {@link Wallet} data. These are client facing methods and their actual implementation
 * should be abstracted away from any outside party.
 *
 */
public interface MoneyWriter {

    /**
     * Saves a new Transaction object
     * @param transaction the object to save
     */
    void saveTransaction(@NonNull final Transaction transaction);
    void saveSource(@NonNull final Source source);
    void deleteSource(@NonNull final String sourceId);
    void updateSourceTotal(@NonNull final String sourceId, final long amount);
    void updateWalletTotal(final long amount);

}
