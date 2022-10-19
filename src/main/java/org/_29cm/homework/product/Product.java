package org._29cm.homework.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
    private Long id;
    private String title;
    private Long price;
    private Long stockCount;
}
