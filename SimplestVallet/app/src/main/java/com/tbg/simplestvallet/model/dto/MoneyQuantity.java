package com.tbg.simplestvallet.model.dto;

import java.util.Currency;
import java.util.Locale;

/**
 * Created by wws2003 on 10/24/15.
 */
public class MoneyQuantity {
    private double mAmount;
    private Currency mCurrency = Currency.getInstance(Locale.getDefault());

    public MoneyQuantity(double amount) {
        mAmount = amount;
    }

    public MoneyQuantity(double amount, Currency currency) {
        mAmount = amount;
        mCurrency = currency;
    }

    public MoneyQuantity add(MoneyQuantity quantity) {
        if(mCurrency == null) {
            mCurrency = quantity.mCurrency;
        }
        else {
            checkCurrency(quantity);
        }
        return new MoneyQuantity(mAmount + quantity.mAmount, mCurrency);
    }

    public MoneyQuantity add(double amount) {
        return new MoneyQuantity(mAmount + amount, mCurrency);
    }

    public MoneyQuantity multiply(double times) {
        return new MoneyQuantity(mAmount * times, mCurrency);
    }

    public Currency getCurrency() {
        return mCurrency;
    }

    public double getAmount() {
        return mAmount;
    }

    public String toString() {
        return mAmount + mCurrency.toString();
    }

    private void checkCurrency(MoneyQuantity quantity) {
        if(quantity.mCurrency != mCurrency) {
            throw new RuntimeException("Sorry, currency conversion hasn't been supported yet");
        }
    }

}
