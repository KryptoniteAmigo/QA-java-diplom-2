package ru.praktikum.yandex.model;

import java.util.List;

public class GetUserOrdersResponse {
    private boolean success;
    private List<OrderData> orders;
    private int total;
    private int totalToday;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public List<OrderData> getOrders() {
        return orders;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalToday() {
        return totalToday;
    }

    public String getMessage() {
        return message;
    }

    public static class OrderData {
        private List<String> ingredients;
        private String _id;
        private String status;
        private int number;
        private String createdAt;
        private String updatedAt;

        public List<String> getIngredients() { return ingredients; }
        public String get_id() { return _id; }
        public String getStatus() { return status; }
        public int getNumber() { return number; }
        public String getCreatedAt() { return createdAt; }
        public String getUpdatedAt() { return updatedAt; }
    }
}
