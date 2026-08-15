package com.khang.backendecommerce.infrastructure.util;

import java.util.UUID;

public interface AppConst {
    String SEARCH_SPEC_OPERATOR = "(\\w+?)([<:>~!])(.*)(\\p{Punct}?)(\\p{Punct}?)";
    public static final long MAX_QUANTITY_PER_ITEM = 99;
    public static final int BUY_NOW_QUANTITY = 1;
    public static String paymentReference =
            "PAY-" + UUID.randomUUID();
}
