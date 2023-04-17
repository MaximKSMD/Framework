package zutils;

import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public final class FakerUtils {

    private static final Faker faker = new Faker(new Locale("en-US"));

    public static String notes() {

        return faker.lorem().paragraph();
    }

    public static String randomUrl() {

        return faker.internet().url();
    }

    public static String captionWithSuffix(String root) {

        return String.format("%s_%s", root, randomNumber());
    }

    public static String randomNumber() {

        return faker.random().nextInt(0, 1000000).toString();
    }

    public static Integer randomInteger(int start, int end) {

        return faker.random().nextInt(start, end);
    }

    public static String date() {

        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public static String randomMail(){

        return faker.internet().emailAddress();
    }

    public static Boolean randomBoolean() {

        return faker.random().nextBoolean();
    }

    public static String randomStringWithSize(int size) {

        return faker.lorem().characters(size);
    }
}