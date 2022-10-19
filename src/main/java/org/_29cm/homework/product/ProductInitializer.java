package org._29cm.homework.product;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org._29cm.homework.product.exception.NoMatchedProductException;
import org._29cm.homework.product.exception.SoldOutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static org._29cm.homework.product.ProductConstraint.*;

@Component
@Slf4j
public class ProductInitializer {
    private final LinkedHashMap<Long, Product> PRODUCTS;

    @Autowired
    public ProductInitializer() {
        try {
            this.PRODUCTS = getProductsFromCsv();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> getAll() {
       return new ArrayList<>(PRODUCTS.values());
    }

    public void order(Long productId, Long sellCount) {
      Product product = PRODUCTS.get(productId);

      if (product == null) {
          throw new NoMatchedProductException("No matched Product found!");
      }

      long remainCount = product.getStockCount() - sellCount;

      if (remainCount < 0) {
          throw new SoldOutException("Stock is all Soldout!!");
      }

      product.setStockCount(remainCount);
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
