package org._29cm.homework.order.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org._29cm.homework.order.model.OrderResponse;
import org._29cm.homework.order.model.Response;
import org._29cm.homework.product.model.Product;
import org._29cm.homework.product.exception.NoMatchedProductException;
import org._29cm.homework.product.exception.SoldOutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static org._29cm.homework.product.ProductConstraint.*;

@Component
@Slf4j
public class OrderService {
    private final LinkedHashMap<Long, Product> productRepositoryMap;

    @Autowired
    public OrderService() {
        try {
            this.productRepositoryMap = getProductsFromCsv();
        } catch (CsvValidationException | IOException e) {
            log.error("[OrderService] initialization failed due to exception :", e);
            throw new RuntimeException(e);
        }
    }

    public List<Product> getAll() {
        return new ArrayList<>(productRepositoryMap.values());
    }

    public synchronized Response<OrderResponse> proceed(Map<Long, Long> productMap) {
        long orderedPrice = 0L;
        System.out.println("-----------------------------------------");

        for (Map.Entry<Long, Long> entry : productMap.entrySet()) {
            orderedPrice += proceed(entry.getKey(), entry.getValue());
        }

        Long totalPrice = orderedPrice;


        if (orderedPrice < DELIVERY_FEE_FREE_THRESHOLD && orderedPrice > 0L) {
            totalPrice += DELIVERY_FEE;
        }


        System.out.println("-----------------------------------------");
        System.out.println(String.format(ORDERED_PRICE_DISPLAY_FORMAT, orderedPrice));
        System.out.println("-----------------------------------------");
        System.out.println(String.format(TOTAL_PRICE_DISPLAY_FORMAT, totalPrice));
        System.out.println("-----------------------------------------");

        return new Response<OrderResponse>(OrderResponse.builder()
                .isSuccess(true)
                .orderedPrice(orderedPrice)
                .totalPrice(totalPrice)
                .build(), "success");
    }

    private Long proceed(Long productId, Long orderAmount) {
        Product product = productRepositoryMap.get(productId);
        if (product == null) {
            throw new NoMatchedProductException("No matched Product found!");
        }

        long remainingAmount = product.getStockCount() - orderAmount;

        if (remainingAmount < SOLD_OUT) {
            throw new SoldOutException("Stock is all Soldout!");
        }
        product.setStockCount(remainingAmount);

        System.out.println(String.format(ORDERED_PRODUCT_DISPLAY_FORMAT, product.getTitle(), orderAmount));

        return product.getPrice() * orderAmount;
    }

    private LinkedHashMap<Long, Product> getProductsFromCsv() throws CsvValidationException, IOException {
        ClassPathResource resource = new ClassPathResource("data/items.csv");
        CSVReader reader = new CSVReader(new FileReader(resource.getFile()));

        LinkedHashMap<Long, Product> productMap = new LinkedHashMap<>();
        String [] next;
        while((next = reader.readNext()) != null) {
            Product product = Product.builder()
                    .id(Long.parseLong(next[PRODUCT_ID]))
                    .title(next[PRODUCT_TITLE])
                    .price(Long.parseLong(next[PRODUCT_PRICE]))
                    .stockCount(Long.parseLong(next[PRODUCT_STOCK_COUNT]))
                    .build();
            productMap.put(product.getId(), product);
        }

        return productMap;
    }
}
