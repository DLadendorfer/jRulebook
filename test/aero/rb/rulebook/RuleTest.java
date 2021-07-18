package aero.rb.rulebook;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ConstantConditions")
public class RuleTest {

    @Test
    void ofNullBothParams() {
        assertThrows(NullPointerException.class, () -> Rule.of(null, null));
    }

    @Test
    void ofNullCondition() {
        assertThrows(NullPointerException.class, () -> Rule.of(null, System.out::println));
    }

    @Test
    void ofSingleNullConsumer() {
        assertThrows(NullPointerException.class, () -> Rule.of(Objects::nonNull, null));
    }

    @Test
    void ofNullInVarargsConsumers() {
        assertThrows(NullPointerException.class, () -> Rule.of(Objects::nonNull, System.out::println, null));
    }

    @Test
    void ofSingleConsumer() {
        Rule.of(Objects::nonNull, System.out::println);
    }

    @Test
    void ofMultipleConsumers() {
        Rule.of(Objects::nonNull, System.out::println, System.out::println);
    }

    @Test
    void isMatchingNullPointerTest() {
        Rule<Object> rule = Rule.of(Objects::nonNull, System.out::println);
        assertThrows(NullPointerException.class, () -> rule.isMatching(null));
    }

    @Test
    void isMatchingTrueTest() {
        Rule<String> rule = Rule.of(String::isEmpty, System.out::println);
        assertTrue(rule.isMatching(""));
    }

    @Test
    void isMatchingFalseTest() {
        Rule<String> rule = Rule.of(String::isEmpty, System.out::println);
        assertFalse(rule.isMatching("abc"));
    }

    @Test
    void invokeMatchingNullPointerTest() {
        Rule<Object> rule = Rule.of(Objects::nonNull, System.out::println);
        assertThrows(NullPointerException.class, () -> rule.invokeIfMatching(null));
    }

    @Test
    void invokeMatchingSingleConsumerTest() {
        CountDownLatch latch = new CountDownLatch(2);
        Rule<String> rule = Rule.of(String::isEmpty, str -> latch.countDown());
        rule.invokeIfMatching("nonMatch");
        rule.invokeIfMatching(""); // match
        assertEquals(1, latch.getCount());
    }

    @Test
    void invokeMatchingMultipleConsumersTest() {
        CountDownLatch latch = new CountDownLatch(2);
        Rule<String> rule = Rule.of(String::isEmpty, str -> latch.countDown(), str -> latch.countDown());
        rule.invokeIfMatching("nonMatch");
        rule.invokeIfMatching(""); // match
        assertEquals(0, latch.getCount());
    }
}
