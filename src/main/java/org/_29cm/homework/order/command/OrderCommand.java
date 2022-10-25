package org._29cm.homework.order.command;

import lombok.extern.slf4j.Slf4j;
import org._29cm.homework.product.model.Product;
import org._29cm.homework.order.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org._29cm.homework.product.ProductConstraint.COMPLETE_ORDER_COMMAND;

@Component
@Slf4j
public class OrderCommand implements Command {
    private final OrderService orderService;

    public OrderCommand(OrderService orderService) {
        this.orderService = orderService;
    }
    @Override
    public void execute() {
        try {
            displayProductList();
            Map<Long, Long> orderMap = new HashMap<>();
            while(true) {
                BufferedReader buf = new BufferedReader (new InputStreamReader(System.in));
                System.out.println("상품번호 : ");
                String productId = buf.readLine();
                System.out.println(productId);
                System.out.println("수량 : ");
                String orderAmount = buf.readLine();
                System.out.println(orderAmount);

                if (StringUtils.isNumeric(productId) && StringUtils.isNumeric(orderAmount)) {
                    orderMap.put(Long.parseLong(productId), Long.parseLong(orderAmount));
                }

                if (proceedOrder(productId, orderAmount)) {
                    if (!orderMap.isEmpty()) {
                        orderService.proceed(orderMap);
                    }
                    break;
                }
            }
        } catch (Exception exception) {
            log.error("Failed process during order", exception);
        }
    }

    @Override
    public boolean match(String command) {
        return StringUtils.equalsIgnoreCase(command, "o");
    }

    private boolean proceedOrder(String productId, String orderCount) {
        return (StringUtils.equalsIgnoreCase(productId, COMPLETE_ORDER_COMMAND) ||
                StringUtils.equalsIgnoreCase(orderCount, COMPLETE_ORDER_COMMAND));
    }

    private void displayProductList() {
        List<Product> allProducts = orderService.getAll();
        for (Product product : allProducts) {
            System.out.println("상품번호    상품명                                                 판매가격                재고수");
            System.out.println(product.getId()+"    "+product.getTitle()+"             "+product.getPrice()+"        "+product.getStockCount());
        }
    }
}
