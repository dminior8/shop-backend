package pl.dminior8.cart_service.domain.entity;

public enum CartStatus {
    ACTIVE,
    CHECKED_OUT,
    EXPIRED       // timeout 15 min
}
