package org._29cm.homework.order.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderResponse {
    private boolean isSuccess;
    private Long orderedPrice;
    private Long totalPrice;
}
