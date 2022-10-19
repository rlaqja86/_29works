package org._29cm.homework.product;

import org._29cm.homework.product.exception.SoldOutException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductInitializerTest {

    @InjectMocks
    ProductInitializer sut;


    @Test
    public void getAll_should_return_all_items_from_csv_after_init() {
        List<Product> result = sut.getAll();
        assertNotNull(result);
        assertEquals(19, result.size());
    }


    @Test(expected = SoldOutException.class)
    public void sold_out_exception_thrown_when_stock_is_lesser_then_order() {
        //sut.init();
        sut.order(778422L, 8L);
    }

    @Test(expected = SoldOutException.class)
    public void sold_out_exception_thrown_when_stock_is_lesser_then_order_in_multiThread() {
        //sut.init();

        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i =0; i < 10; i ++) {
            service.execute(() -> {
                sut.order(778422L, 2L);
            });
        }
    }

}