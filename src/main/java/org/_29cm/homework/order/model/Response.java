package org._29cm.homework.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response <T> {
    private T data;
    private String message;
}
