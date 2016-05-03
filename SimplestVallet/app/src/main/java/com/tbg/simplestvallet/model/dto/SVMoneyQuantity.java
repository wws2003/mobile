package com.tbg.simplestvallet.model.dto;

import java.util.Currency;
import java.util.Locale;

/**
 * Created by wws2003 on 10/24/15.
 */
public class SVMoneyQuantity {
    private float mAmount;
    private Currency mCurrency = Currency.getInstance(Locale.getDefault());

    public SVMoneyQuantity(float amount) {
        mAmount = amount;
    }

    public SVMoneyQuantity(float amount, Currency currency) {
        mAmount = amount;
        mCurrency = currency;
    }

    public SVMoneyQuantity(SVMoneyQuantity rhs) {
        mAmount = rhs.getAmount();
        mCurrency = rhs.getCurrency();
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

    public SVMoneyQuantity add(float amount) {
        return new SVMoneyQuantity(mAmount + amount, mCurrency);
    }

    public SVMoneyQuantity multiply(float times) {
        return new SVMoneyQuantity(mAmount * times, mCurrency);
    }

    public Currency getCurrency() {
        return mCurrency;
    }

    public float getAmount() {
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
