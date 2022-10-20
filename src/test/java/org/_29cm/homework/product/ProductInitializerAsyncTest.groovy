package org._29cm.homework.product

import org._29cm.homework.product.exception.SoldOutException
import spock.lang.Specification

class ProductInitializerAsyncTest extends Specification {

    ProductInitializer sut;

    def "async test" () {

        given:
        Long productId = 778422L
        when:
        sut.order(productId, 12L)
        then:
        thrown SoldOutException
    }
}
