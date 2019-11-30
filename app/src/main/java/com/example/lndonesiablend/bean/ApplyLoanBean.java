package com.example.lndonesiablend.bean;

public class ApplyLoanBean {

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
