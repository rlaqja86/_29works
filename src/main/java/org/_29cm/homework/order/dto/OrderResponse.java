package org._29cm.homework.order.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderResponse {
    private boolean isSuccess;
    private Long orderedPrice;
    private Long totalPrice;
}
