package com.ericdevke.corebankingtransactionprocessingapi.entity;

public enum Currency {
    KES("1001"),
    USD("1002"),
    GBP("1003"),
    EUR("1004");

    private final String productCode;

    Currency(String productCode) {
        this.productCode = productCode;
    }

    public String getProductCode() {
        return productCode;
    }
}
