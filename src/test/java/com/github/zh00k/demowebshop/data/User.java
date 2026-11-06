package com.github.zh00k.demowebshop.data;

public record User(
        Gender gender,
        String firstName,
        String lastName,
        String email,
        String password
) {
}
