package com.github.zh00k.data;

import net.datafaker.Faker;

import java.util.Locale;

public final class DataGenerator {
    private static final Faker faker = new Faker(Locale.ENGLISH);

    private DataGenerator() {}

    public static String firstName() {
        return faker.name().firstName();
    }

    public static String lastName() {
        return faker.name().lastName();
    }

    public static String email() {
        return faker.internet().emailAddress("zh00k_Test_User" + System.currentTimeMillis());
    }

    public static String password() {
        return faker.credentials().password(8, 10);
    }

    public static Gender gender() {
        return faker.options().option(Gender.class);
    }

    public static User randomUser() {
        return new User(gender(), firstName(), lastName(), email(), password());
    }
}
