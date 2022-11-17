/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.untils;

import org.openqa.selenium.NoSuchElementException;

import com.github.javafaker.Faker;

/**
 * @author Gabriela Zaranek
 */
public class FakeGenerator {
    private static Faker faker = new Faker();

    public static String getAddress() {
        return faker.address().fullAddress();
    }

    public static String getCity() {
        return faker.address().city();
    }

    public static String getIdNumber() {
        return faker.idNumber().valid();
    }

    public static int getRandomInt() {
        return faker.random().nextInt(500);
    }

    public static String getRandomName() {
        return faker.name().name();
    }

    public static String getLocation(FilmTitle filmTitle) {
        switch (filmTitle) {
            case HARRY_POTTER:
                return faker.harryPotter().location();
            case LORD_OF_THE_RING:
                return faker.lordOfTheRings().location();
            case HOBBIT:
                return faker.hobbit().location();
            case FRIENDS:
                return faker.friends().location();
            default:
                throw new NoSuchElementException("Can't find film title: " + filmTitle);
        }
    }

    public static String getCharacter(FilmTitle filmTitle) {
        switch (filmTitle) {
            case HARRY_POTTER:
                return faker.harryPotter().character();
            case LORD_OF_THE_RING:
                return faker.lordOfTheRings().character();
            case HOBBIT:
                return faker.hobbit().character();
            case FRIENDS:
                return faker.friends().character();
            default:
                throw new NoSuchElementException("Can't find film title: " + filmTitle);
        }
    }

    public enum FilmTitle {
        HARRY_POTTER, LORD_OF_THE_RING, HOBBIT, FRIENDS
    }

}
