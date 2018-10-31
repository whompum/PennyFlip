package com.whompum.PennyFlip.Money.Contracts.Transaction;

import android.arch.persistence.room.Dao;

import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Contracts.MoneyDaoWriter;

@Dao
public interface TransactionDao extends TransactionQueryContract, MoneyDaoWriter<Transaction>{}
