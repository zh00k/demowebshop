package com.github.zh00k.data;

public record User(
        Gender gender,
        String firstName,
        String lastName,
        String email,
        String password
) { }
