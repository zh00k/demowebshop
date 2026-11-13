package com.github.zh00k.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookingId(@JsonProperty("bookingid") Integer bookingId) {}
