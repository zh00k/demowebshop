package com.github.zh00k.booking.config;

import org.aeonbits.owner.ConfigFactory;

public class BookingApiConfig {
    public static BookingConfig getBookingConfig() {
        return ConfigFactory.create(BookingConfig.class, System.getProperties());
    }
}
