package com.example.lndonesiablend.bean;

import java.io.Serializable;

public class ApplyLoanBean implements Serializable {

    private String loanId;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    @Override
    public String toString() {
        return "ApplyLoanBean{" +
                "loanId='" + loanId + '\'' +
                '}';
    }
}
