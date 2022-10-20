package org._29cm.homework.order.command;

import lombok.extern.slf4j.Slf4j;
import org._29cm.homework.product.Product;
import org._29cm.homework.product.ProductInitializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import static org._29cm.homework.product.ProductConstraint.COMPLETE_ORDER_COMMAND;

@Component
@Slf4j
public class OrderListCommand implements Command {
    private final ProductInitializer productInitializer;

    public OrderListCommand(ProductInitializer productInitializer) {
        this.productInitializer = productInitializer;
    }
    @Override
    public void execute() {
        try {
            displayProductList();
            while(true) {
                BufferedReader buf = new BufferedReader (new InputStreamReader(System.in));
                System.out.println("상품번호 : ");
                String productId = buf.readLine();
                System.out.println(productId);
                System.out.println("수량 : ");
                String orderCount = buf.readLine();
                System.out.println(orderCount);

                if (isQuitOrder(productId, orderCount)) {
                    break;
                }

                productInitializer.order(Long.parseLong(productId), Long.parseLong(orderCount));

            }
        } catch (Exception exception) {
            System.out.println("soldout");
            log.error("Failed process during order", exception);
        }
    }

    @Override
    public boolean isMatch(String command) {
        return StringUtils.equalsIgnoreCase(command, "o");
    }

    private boolean isQuitOrder(String productId, String orderCount) {
        return (StringUtils.equalsIgnoreCase(productId, COMPLETE_ORDER_COMMAND) || StringUtils.equalsIgnoreCase(orderCount, COMPLETE_ORDER_COMMAND));
    }

    private void displayProductList() {
        List<Product> allProducts = productInitializer.getAll();
        for (Product product : allProducts) {
            System.out.println("상품번호    상품명                                                 판매가격        재고수");
            System.out.println(product.getId()+"    "+product.getTitle()+"             "+product.getPrice()+"        "+product.getStockCount());
        }
    }
}
