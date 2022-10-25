package org._29cm.homework.product;

public class ProductConstraint {
    public final static int PRODUCT_ID = 0;
    public final static int PRODUCT_TITLE = 1;
    public final static int PRODUCT_PRICE = 2;
    public final static int PRODUCT_STOCK_COUNT = 3;

    public final static String ORDERED_PRODUCT_DISPLAY_FORMAT = "%s - %s개";
    public final static String ORDERED_PRICE_DISPLAY_FORMAT = "주문금액: %s원";
    public final static String TOTAL_PRICE_DISPLAY_FORMAT = "지불금액: %s원";
    public final static Long DELIVERY_FEE_FREE_THRESHOLD = 50000L;
    public final static Long DELIVERY_FEE = 2500L;
    public final static Long SOLD_OUT = 0L;

    public final static String COMPLETE_ORDER_COMMAND = " ";
}
