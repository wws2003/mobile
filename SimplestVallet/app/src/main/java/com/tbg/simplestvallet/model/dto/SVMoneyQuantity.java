package com.tbg.simplestvallet.model.dto;

import java.util.Currency;
import java.util.Locale;

/**
 * Created by wws2003 on 10/24/15.
 */
public class SVMoneyQuantity {
    private double mAmount;
    private Currency mCurrency = Currency.getInstance(Locale.getDefault());

    public SVMoneyQuantity(double amount) {
        mAmount = amount;
    }

    public SVMoneyQuantity(double amount, Currency currency) {
        mAmount = amount;
        mCurrency = currency;
    }

    public SVMoneyQuantity add(SVMoneyQuantity quantity) {
        if(mCurrency == null) {
            mCurrency = quantity.mCurrency;
        }
        else {
            checkCurrency(quantity);
        }
        return new SVMoneyQuantity(mAmount + quantity.mAmount, mCurrency);
    }

    public SVMoneyQuantity add(double amount) {
        return new SVMoneyQuantity(mAmount + amount, mCurrency);
    }

    public SVMoneyQuantity multiply(double times) {
        return new SVMoneyQuantity(mAmount * times, mCurrency);
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

    private void checkCurrency(SVMoneyQuantity quantity) {
        if(quantity.mCurrency != mCurrency) {
            throw new RuntimeException("Sorry, currency conversion hasn't been supported yet");
        }
    }

}
