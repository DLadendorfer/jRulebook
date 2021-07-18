package aero.rb.rulebook;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("ConstantConditions")
public class RulesetTest {

    Rule<String> rule = Rule.of(String::isEmpty, System.out::println);
    Rule<String> rule2 = Rule.of("abcdefg"::equals, System.out::println);
    Ruleset<String> ruleset = Ruleset.of(String::isEmpty, rule, rule2);

    @Test
    void ofNullParam() {
        assertThrows(NullPointerException.class, () -> Ruleset.of(null));
    }

    @Test
    void ofNullParams() {
        assertThrows(NullPointerException.class, () -> Ruleset.of(null, null));
    }

    @Test
    void ofNullInVarargsConsumers() {
        assertThrows(NullPointerException.class, () -> Ruleset.of(String::isEmpty, rule, null));
    }

    @Test
    void ofSingleRule() {
        Ruleset.of(String::isEmpty, rule);
    }

    @Test
    void ofMultipleRules() {
        Ruleset.of(String::isEmpty, rule, rule2);
    }

    @Test
    void getMatchingRulesNullPointerTest() {
        assertThrows(NullPointerException.class, () -> ruleset.getMatchingRules(null));
    }

    @Test
    void getMatchingRulesTrueTest() {
        List<IRule<String>> matchingRules = ruleset.getMatchingRules("");
        assertEquals(1, matchingRules.size());
        assertEquals(rule, matchingRules.get(0));
    }

    @Test
    void getMatchingRulesFalseTest() {
        List<IRule<String>> matchingRules = ruleset.getMatchingRules("123");
        assertEquals(0, matchingRules.size());
    }

    @Test
    void invokeMatchingNullPointerTest() {
        assertThrows(NullPointerException.class, () -> ruleset.invokeMatchingRules(null));
    }

    @Test
    void invokeMatchingSingleRuleTest() {
        CountDownLatch latch = new CountDownLatch(2);
        Ruleset<String> ruleset =
                Ruleset.of(str -> true,
                        Rule.of(String::isEmpty, str -> latch.countDown())
                );


        ruleset.invokeMatchingRules("");
        ruleset.invokeMatchingRules("123");
        assertEquals(1, latch.getCount());
    }

    @Test
    void invokeMatchingMultipleRulesTest() {
        CountDownLatch latch = new CountDownLatch(4);
        Ruleset<String> ruleset =
                Ruleset.of(str -> true,
                        Rule.of(str -> !str.isEmpty(), str -> latch.countDown()),
                        Rule.of(str -> !str.isEmpty(), str -> latch.countDown()),
                        Rule.of(str -> !str.isEmpty(), str -> latch.countDown())
                );

        ruleset.invokeMatchingRules("");
        ruleset.invokeMatchingRules("123");
        assertEquals(1, latch.getCount());
    }
}
