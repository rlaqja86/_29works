package org._29cm.homework.product;

import org._29cm.homework.order.dto.OrderResponse;
import org._29cm.homework.product.exception.SoldOutException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @InjectMocks
    OrderService sut;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getAll_should_return_all_items_from_csv_after_init() {
        List<Product> result = sut.getAll();
        assertNotNull(result);
        assertEquals(19, result.size());
    }

    @Test
    public void deliveryFee_is_added_when_total_order_is_lesser_than_50000() {
        Map<Long,Long> orderMap = new HashMap<>();
        orderMap.put(778422L, 1L);
        OrderResponse response = sut.order(orderMap);

        Long deliveryFee = 2500L;
        Long orderedPriceWithDeliveryFee = response.getOrderedPrice() + deliveryFee;

        assertEquals(orderedPriceWithDeliveryFee, response.getTotalPrice());
    }

    @Test
    public void deliveryFee_is_not_added_when_total_order_is_over_50000() {
        Map<Long,Long> orderMap = new HashMap<>();
        orderMap.put(778422L, 2L);
        OrderResponse response = sut.order(orderMap);
        assertEquals(response.getOrderedPrice(), response.getTotalPrice());
    }

    @Test(expected = SoldOutException.class)
    public void sold_out_exception_thrown_when_stock_is_lesser_then_order() {
        Map<Long,Long> orderMap = new HashMap<>();
        orderMap.put(778422L, 8L);
        sut.order(orderMap);
    }

    @Test
    public void sold_out_exception_thrown_when_stock_is_lesser_then_order_in_multiThread() {

        try {
            CountDownLatch countDownLatch = new CountDownLatch(8);
            ExecutorService executorService = Executors.newFixedThreadPool(8);
            List<RuntimeException> result = new ArrayList<>();

            /*상품번호    상품이름           가격      재고
            * 778422    캠핑덕 우드롤테이블  45000     7
            * */

            for (int i = 0; i < 8; i ++) {
                executorService.execute(() -> {
                    try {
                        Map<Long,Long> orderMap = new HashMap<>();
                        orderMap.put(778422L, 1L);
                        sut.order(orderMap);
                    } catch (SoldOutException soldOutException) {
                        result.add(soldOutException);
                        System.out.println("Soldout Exception thrown from : " + Thread.currentThread().getName());
                    }
                    countDownLatch.countDown();
                });
            }
            countDownLatch.await();

            assertFalse(result.isEmpty());
            assertEquals(1, result.size());
            assertTrue(result.get(0) instanceof SoldOutException);

            executorService.shutdown();

        }  catch (InterruptedException interruptedException) {
        }

    }

}