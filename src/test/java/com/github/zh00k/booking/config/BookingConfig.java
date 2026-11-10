package com.github.zh00k.booking.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/booking.properties")
@Config.LoadPolicy(Config.LoadType.MERGE)
public interface BookingConfig extends Config {
    @Key("username")
    String username();

    @Key("password")
    String password();

    @Key("bookingUrl")
    String bookingUrl();
}
