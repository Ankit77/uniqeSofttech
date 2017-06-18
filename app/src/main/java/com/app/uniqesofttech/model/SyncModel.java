package com.app.uniqesofttech.model;

/**
 * Created by chandnichudasama on 18/06/17.
 */

public class SyncModel {
    private String id;
    private String dealercode;
    private String cashmemono;
    private String paymentmode;
    private String amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDealercode() {
        return dealercode;
    }

    public void setDealercode(String dealercode) {
        this.dealercode = dealercode;
    }

    public String getCashmemono() {
        return cashmemono;
    }

    public void setCashmemono(String cashmemono) {
        this.cashmemono = cashmemono;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
