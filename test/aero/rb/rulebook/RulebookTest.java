package aero.rb.rulebook;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("ConstantConditions")
public class RulebookTest {

    Rule<String> rule = Rule.of(String::isEmpty, System.out::println);
    Rule<String> rule2 = Rule.of("abcdefg"::equals, System.out::println);
    Ruleset<String> ruleset = Ruleset.of(String::isEmpty, rule, rule2);

    @Test
    void ofNullParam() {
        assertThrows(NullPointerException.class, () -> Rulebook.of(null));
    }

    @Test
    void ofNullInVarargsConsumers() {
        assertThrows(NullPointerException.class, () -> Rulebook.of(ruleset, null));
    }

    @Test
    void ofSingleRuleset() {
        Rulebook.of(ruleset);
    }

    @Test
    void ofMultipleRulesets() {
        Rulebook.of(ruleset, ruleset);
    }

    @Test
    void getMatchingRulesNullPointerTest() {
        Rulebook<String> rulebook = Rulebook.of(ruleset);
        assertThrows(NullPointerException.class, () -> rulebook.getMatchingRules(null));
    }

    @Test
    void getMatchingRulesTrueTest() {
        Rulebook<String> rulebook = Rulebook.of(ruleset);
        List<IRule<String>> matchingRules = rulebook.getMatchingRules("");
        assertEquals(1, matchingRules.size());
        assertEquals(rule, matchingRules.get(0));
    }

    @Test
    void getMatchingRulesFalseTest() {
        Rulebook<String> rulebook = Rulebook.of(ruleset);
        List<IRule<String>> matchingRules = rulebook.getMatchingRules("123");
        assertEquals(0, matchingRules.size());
    }

    @Test
    void invokeMatchingNullPointerTest() {
        Rulebook<String> rulebook = Rulebook.of(ruleset);
        assertThrows(NullPointerException.class, () -> rulebook.invokeMatchingRules(null));
    }

    @Test
    void invokeMatchingSingleRulesetTest() {
        CountDownLatch latch = new CountDownLatch(4);
        Rulebook<String> rulebook =
                Rulebook.of(
                        Ruleset.of(str -> true,
                                Rule.of(String::isEmpty, str -> latch.countDown()),
                                Rule.of(String::isEmpty, str -> latch.countDown()),
                                Rule.of(String::isEmpty, str -> latch.countDown())
                        )
                );

        rulebook.invokeMatchingRules("");
        rulebook.invokeMatchingRules("123");
        assertEquals(1, latch.getCount());
    }

    @Test
    void invokeMatchingMultipleRulesetTest() {
        CountDownLatch latch = new CountDownLatch(7);
        Rulebook<String> rulebook =
                Rulebook.of(
                        Ruleset.of(str -> true,
                                Rule.of(str -> !str.isEmpty(), str -> latch.countDown()),
                                Rule.of(str -> !str.isEmpty(), str -> latch.countDown()),
                                Rule.of(str -> !str.isEmpty(), str -> latch.countDown())
                        ),
                        Ruleset.of(str -> str.length() == 3,
                                Rule.of("123"::equals, str -> latch.countDown()),
                                Rule.of("123"::equals, str -> latch.countDown()),
                                Rule.of("123"::equals, str -> latch.countDown())
                        )
                );

        rulebook.invokeMatchingRules("");
        rulebook.invokeMatchingRules("123");
        assertEquals(1, latch.getCount());
    }
}
