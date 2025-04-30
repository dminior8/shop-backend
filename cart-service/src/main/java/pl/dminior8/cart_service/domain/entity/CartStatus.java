package pl.dminior8.cart_service.domain.entity;

public enum CartStatus {
    NEW,
    ACTIVE,
    CHECKED_OUT,
    EXPIRED       // timeout 15 min
}
