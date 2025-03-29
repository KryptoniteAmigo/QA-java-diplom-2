package ru.praktikum.yandex.model;

public class OrderResponse {
    private boolean success;
    private String name;
    private OrderBody order;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public String getName() {
        return name;
    }

    public OrderBody getOrder() {
        return order;
    }

    public String getMessage() {
        return message;
    }

    public static class OrderBody {
        private int number;

        public int getNumber() {
            return number;
        }
    }
}
