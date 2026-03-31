package com.sa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("SaApplication Tests")
class SaApplicationTests {

    @Test
    @DisplayName("Should load application context")
    void contextLoads() {
        // This test verifies that the Spring application context loads successfully
    }

    @Test
    @DisplayName("Should run main method")
    void shouldRunMainMethod() {
        // This test verifies the main method can be invoked
        // The actual application won't start fully in test context
        SaApplication.main(new String[] {});
    }

    @Test
    void testStreamApi1() {
        List<Integer> inputList = List.of(1, 2, 3, 4)
                .stream()
                .filter(i -> i > 2)
                .toList();

        boolean result = inputList.containsAll(List.of(3, 4));
        assertTrue(result);
    }

    @Test
    void testStreamApi2() {
        List<Integer> inputList = List.of(1, 2, 3, 4, 5, 6)
                .stream()
                .filter(i -> i % 2 == 0)
                .toList();

        boolean result = inputList.containsAll(List.of(2, 4, 6));
        assertTrue(result);
    }

    @Test
    void testStreamApi3() {
        List<String> inputList = List.of("java", "stream")
                .stream()
                .map(String::toUpperCase)
                .toList();

        boolean result = inputList.containsAll(List.of("JAVA", "STREAM"));
        assertTrue(result);
    }

    @Test
    void testStreamApi4() {
        List<String> inputList = List.of("a", "abc", "abcd")
                .stream()
                .filter(i -> i.length() > 3)
                .toList();

        boolean result = inputList.contains("abcd");
        assertTrue(result);
    }

    @Test
    void testStreamApi5() {
        List<Integer> inputList = List.of(5, 1, 3, 2)
                .stream()
                .sorted()
                .toList();

        boolean result = inputList.containsAll(List.of(1, 2, 3, 5));
        assertTrue(result);
    }

    @Test
    void testStreamApi6() {
        List<Integer> inputList = List.of(1, 2, 2, 3, 3)
                .stream()
                .distinct()
                .toList();

        boolean result = inputList.containsAll(List.of(1, 2, 3));
        assertTrue(result);
    }

    @Test
    void testStreamApi7() {
        List<Integer> inputList = List.of("Java", "API")
                .stream()
                .map(String::length)
                .toList();

        boolean result = inputList.containsAll(List.of(4, 3));
        assertTrue(result);
    }

    @Test
    void testStreamApi8() {
        Integer inputList = List.of(10, 20, 30)
                .stream()
                .findFirst()
                .orElse(0);

        assertEquals(10, inputList);
    }

    @Test
    void testStreamApi9() {
        Integer inputList = List.of(1, 2, 3, 4)
                .stream()
                .reduce(Integer::sum)
                .orElse(0);


        assertEquals(10, (int) inputList);
    }

    @Test
    void testStreamApi10() {
        Integer inputList = List.of(10, 5, 8, 20)
                .stream()
                .min(Comparator.comparingInt(integer -> integer))
                .orElse(0);


        assertEquals(5, (int) inputList);
    }

    @Test
    void testStreamApi11() {
        List<Integer> inputList = List.of(1, 2, 3, 4)
                .stream()
                .filter(i -> i % 2 == 0)
                .map(i -> i * i)
                .toList();


        boolean result = inputList.containsAll(List.of(4, 16));
        assertTrue(result);
    }

    @Test
    void testStreamApi12() {
        List<String> inputList = List.of("Apple", "Banana", "Ant")
                .stream()
                .filter(i -> i.startsWith("A"))
                .toList();


        boolean result = inputList.containsAll(List.of("Apple", "Ant"));
        assertTrue(result);
    }

    @Test
    void testStreamApi13() {
        long inputList = List.of(5, 12, 8, 15, 3)
                .stream()
                .filter(i -> i > 10)
                .count();

        assertEquals(2, inputList);
    }

    @Test
    void testStreamApi14() {
        double inputList = List.of(1, 2, 3, 4, 5)
                .stream()
                .mapToInt(n -> n)
                .average()
                .orElse(0);


        assertEquals(3.0, inputList);
    }

    @Test
    void testStreamApi15() {
        List<Integer> inputList = List.of(1, 2, 3, 4, 5)
                .stream()
                .skip(2)
                .toList();


        boolean result = inputList.containsAll(List.of(3, 4, 5));
        assertTrue(result);
    }

    @Test
    void testStreamApi16() {
        List<Integer> inputList = List.of(1, 2, 3, 4, 5)
                .stream()
                .limit(3)
                .toList();


        boolean result = inputList.containsAll(List.of(1, 2, 3));
        assertTrue(result);
    }

    @Test
    void testStreamApi17() {
        String inputList = List.of("X", "Y", "Z")
                .stream()
                .collect(Collectors.joining(", "));;

        assertEquals("X, Y, Z", inputList);
    }

    @Test
    void testStreamApi18() {
        boolean anyMatch = List.of(1, 3, 5, 8)
                .stream()
                .anyMatch(x -> x % 2 == 0);

        assertTrue(anyMatch);
    }

    @Test
    void testStreamApi19() {
        List<String> inputList = List.of("a", "c", "b")
                .stream()
                .sorted(Comparator.reverseOrder())
                .toList();

        assertEquals(inputList, List.of("c", "b", "a"));
    }

    @Test
    void testStreamApi20() {
        Integer inputList = List.of(1, 2, 3, 4)
                .stream()
                .reduce((first, second) -> first * second)
                .orElse(0);

        assertEquals(24, inputList);
    }

    @Test
    void testStreamApi21() {
        Map<Integer, List<String>> collect = List.of("hi", "hello", "abc")
                .stream()
                .collect(Collectors.groupingBy(e -> e.length()));

        System.out.println(collect);
    }

    @Test
    void testStreamApi22() {
        Map<Boolean, List<Integer>> collect = List.of(1, 2, 3, 4)
                .stream()
                .collect(Collectors.groupingBy(e -> e % 2 == 0));

        System.out.println(collect);
    }

    @Test
    void testStreamApi23() {
        List<Integer> result = List.of(1, 2, 2, 3, 4, 4)
                .stream()
                .filter(e -> Collections.frequency(List.of(1, 2, 2, 3, 4, 4), e) > 1)
                .distinct()
                .toList();

        System.out.println(result);
    }

    @Test
    void testStreamApi24() {
        List<String> result = Arrays.asList("a", null, "b")
                .stream()
                .filter(Objects::nonNull)
                .toList();

        System.out.println(result);
    }

    @Test
    void testStreamFromCI() {
        List<User> users = new ArrayList<>();
        users.add(new User("Игорь", "Брусникин", 28));
        users.add(new User("Игорь", "Ватников", 21));
        users.add(new User("Маша", "Птачек", 34));
        users.add(new User("Маша", "Брусникова", 25));
        users.add(new User("Маша", "Птачек2", 33));
        users.add(new User("Петр", "Кашевич", 33));
        users.add(new User("Петр", "Артемов", 33));
        users.add(new User("Петр", "Юргель", 33));

        Map<String, Optional<User>> collect = users.stream()
                .collect(Collectors.groupingBy(
                        u -> u.firstName,
                        Collectors.minBy(Comparator.comparing((User u) -> u.age))
                ));

        List<String> resultList = users.stream()
                .collect(Collectors.toMap(
                        u -> u.firstName,
                        u -> u,
                        (existObj, newObj) -> {
                            Integer existAge = existObj.age;
                            Integer newAge = newObj.age;
                            if (!Objects.equals(existAge, newAge)) {
                                return existAge < newAge ? existObj : newObj;
                            }

                            return existObj.secondName.compareTo(newObj.secondName) < 0 ? existObj : newObj;
                        },
                        LinkedHashMap::new
                )).entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getValue().age, Comparator.reverseOrder()))
                .map(user -> user.getValue().age + " - " + user.getValue().firstName + " " + user.getValue().secondName)
                .toList();


        System.out.println(collect);
        System.out.println(resultList);
    }

//    @Test
//    void testStreamFromCI() {
//        List<User> users = new ArrayList<>();
//        users.add(new User("Игорь", "Брусникин", 28));
//        users.add(new User("Игорь", "Ватников", 21));
//        users.add(new User("Маша", "Птачек", 34));
//        users.add(new User("Маша", "Брусникова", 25));
//        users.add(new User("Маша", "Птачек2", 33));
//        users.add(new User("Петр", "Кашевич", 33));
//        users.add(new User("Петр", "Артемов", 33));
//        users.add(new User("Петр", "Юргель", 33));
//
//        String collect = users.stream()
//                .collect(Collectors.groupingBy(
//                        u -> u.firstName,
//                        Collectors.minBy(Comparator
//                                                 .comparingInt((User x) -> x.age)
//                                                 .thenComparing(x -> x.secondName))
//                ))
//                .entrySet().stream()
//                .map(entry -> entry.getKey() + " - " + entry.getValue().get().secondName + " - " + entry.getValue().get().age)
//                .collect(Collectors.joining(", "));
//
//        String collect1 = users.stream()
//                .collect(Collectors.toMap(
//                        u -> u.firstName,
//                        u -> u,
//                        (existing, replacement) -> {
//                            if (!Objects.equals(existing.age, replacement.age)) {
//                                return existing.age < replacement.age ? existing : replacement;
//                            }
//                            return (existing.secondName.compareTo(replacement.secondName)) <= 0 ? existing : replacement;
//                        }
//                ))
//                .entrySet().stream()
//                .map(entry -> entry.getKey() + " - " + entry.getValue().secondName + " - " + entry.getValue().age)
//                .collect(Collectors.joining(", "));
//
//
//        System.out.println(collect);
//        System.out.println(collect1);
//    }

    public static class User {
        public String firstName;
        public String secondName;
        public Integer age;

        public User(String firstName, String secondName, Integer age) {
            this.firstName = firstName;
            this.secondName = secondName;
            this.age = age;
        }
    }
}
