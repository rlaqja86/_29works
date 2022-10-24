package org._29cm.homework.product;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org._29cm.homework.order.dto.OrderResponse;
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
    private final String ORDERED_PRODUCT_DISPLAY_FORMAT = "%s - %s개";
    private final String ORDERED_PRICE_DISPLAY_FORMAT = "주문금액: %s원";
    private final String TOTAL_PRICE_DISPLAY_FORMAT = "지불금액: %s원";

    @Autowired
    public OrderService() {
        try {
            this.productRepositoryMap = getProductsFromCsv();
        } catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> getAll() {
        return new ArrayList<>(productRepositoryMap.values());
    }

    public synchronized OrderResponse order(Map<Long, Long> productMap) {
        Long orderedPrice = 0L;
        Long deliveryFee = 2500L;

        System.out.println("-----------------------------------------");

        for (Map.Entry<Long, Long> entry : productMap.entrySet()) {
            orderedPrice += processOrder(entry.getKey(), entry.getValue());
        }

        Long totalPrice = orderedPrice;
        if (orderedPrice < 50000L) {
            totalPrice += deliveryFee;
        }

        System.out.println("-----------------------------------------");
        System.out.println(String.format(ORDERED_PRICE_DISPLAY_FORMAT, orderedPrice));
        System.out.println("-----------------------------------------");
        System.out.println(String.format(TOTAL_PRICE_DISPLAY_FORMAT, totalPrice));
        System.out.println("-----------------------------------------");

        return OrderResponse.builder()
                .isSuccess(true)
                .orderedPrice(orderedPrice)
                .totalPrice(totalPrice)
                .build();
    }

    private Long processOrder(Long productId, Long orderCount) {
        Product product = productRepositoryMap.get(productId);
        if (product == null) {
            throw new NoMatchedProductException("No matched Product found!");
        }

        long remainCount = product.getStockCount() - orderCount;

        if (remainCount < 0) {
            throw new SoldOutException("Stock is all Soldout!!");
        }
        product.setStockCount(remainCount);

        System.out.println(String.format(ORDERED_PRODUCT_DISPLAY_FORMAT, product.getTitle(), orderCount));

        return product.getPrice() * orderCount;
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
