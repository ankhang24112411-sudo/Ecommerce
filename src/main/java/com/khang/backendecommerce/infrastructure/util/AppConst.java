package com.khang.backendecommerce.infrastructure.util;
public interface AppConst {
    String SEARCH_SPEC_OPERATOR = "(\\w+?)([<:>~!])(.*)(\\p{Punct}?)(\\p{Punct}?)";
    public static final long MAX_QUANTITY_PER_ITEM = 99 ;
    public static final int BUY_NOW_QUANTITY = 1 ;

}
