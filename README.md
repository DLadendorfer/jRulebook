# jRulebook
 Invoke Consumers based on a rulebook. A rulebook consists out of one or more rulesets which consist out of rules.
 A rule consists out of a Predicate and consumers which will be invoked if the rule's condition is true.

# Usage 
Simple rule:

```java
Rule<String> stringRule = Rule.of(String::isEmpty, str -> System.out.println("String is empty!"));

stringRule.invokeIfMatching(""); // sysout = "String is empty!"
stringRule.invokeIfMatching("abc"); // nothing happens
stringRule.isMatching(""); // true
```

Group rules into rulesets:

```java
Rule<String> stringNotEmptyRule = Rule.of(str -> !str.isEmpty(), str -> System.out.println("String is not empty!"));
Rule<String> stringIsAbc = Rule.of("abc"::equals, str -> System.out.println("String is equal to abc!"));

Ruleset<String> ruleset = Ruleset.of(Objects::nonNull, stringNotEmptyRule, stringIsAbc);

ruleset.invokeMatchingRules(""); // nothing happens
ruleset.invokeMatchingRules("abc"); // sysout = "String is not empty!" & "String is equal to abc!"
ruleset.invokeMatchingRules("123"); // sysout = "String is not empty!"
```

Group rulesets into rulebooks:

```java
Rule<String> stringNotEmptyRule = Rule.of(str -> !str.isEmpty(), str -> System.out.println("String is not empty!"));
Rule<String> stringIsAbc = Rule.of("abc"::equals, str -> System.out.println("String is equal to abc!"));

Ruleset<String> ruleset1 = Ruleset.of(Objects::nonNull, stringNotEmptyRule, stringIsAbc);
Ruleset<String> ruleset2 = Ruleset.of(String::isEmpty, Rule.of(String::isEmpty, str -> System.out.println("String is empty")));

Rulebook<String> rulebook = Rulebook.of(ruleset1, ruleset2);

rulebook.invokeMatchingRules(""); // sysout = "String is empty!"
rulebook.invokeMatchingRules("abc"); // sysout = "String is not empty!" & "String is equal to abc!"
rulebook.invokeMatchingRules("123"); // sysout = "String is not empty!"
```

# Example
 This is the output ...
 
 ```
[NEGATIVE, POSITIVE, NEGATIVE, NEGATIVE] List size too big! Expected 2!
[NEGATIVE, POSITIVE] -+
[UNCERTAIN, UNCERTAIN] ??
[NEGATIVE, POSITIVE] -+
[POSITIVE] List size too small! Expected 2!
The given list is empty.
[] List size too small! Expected 2!
```

of the following code:
 ```java 
import static aero.rb.rulebook.ExampleTest.States.*;

enum States{
    NEGATIVE,
    POSITIVE,
    UNCERTAIN
}

Rulebook<List<States>> rulebook =
        Rulebook.of(
                Ruleset.of(list -> list.isEmpty(),
                        Rule.of(list -> true, list -> System.out.println("The given list is empty."))
                ),
                Ruleset.of(list -> list.size() != 2,
                        Rule.of(Objects::nonNull, list -> System.out.print(list + " ")),
                        Rule.of(list -> list.size() < 2, list -> System.out.println("List size too small! Expected 2!")),
                        Rule.of(list -> list.size() > 3, list -> System.out.println("List size too big! Expected 2!"))
                ),
                Ruleset.of(list -> list.size() == 2,
                        Rule.of(Objects::nonNull, list -> System.out.print(list + " ")),
                        Rule.of(Arrays.asList(NEGATIVE, POSITIVE)::equals, list -> System.out.println("-+")),
                        Rule.of(Arrays.asList(POSITIVE, NEGATIVE)::equals, list -> System.out.println("+-")),
                        Rule.of(Arrays.asList(NEGATIVE, NEGATIVE)::equals, list -> System.out.println("--")),
                        Rule.of(Arrays.asList(POSITIVE, POSITIVE)::equals, list -> System.out.println("++")),
                        Rule.of(Arrays.asList(UNCERTAIN, UNCERTAIN)::equals, list -> System.out.println("??")),
                        Rule.of(Arrays.asList(UNCERTAIN, POSITIVE)::equals, list -> System.out.println("?+")),
                        Rule.of(Arrays.asList(UNCERTAIN, NEGATIVE)::equals, list -> System.out.println("?-")),
                        Rule.of(Arrays.asList(POSITIVE, UNCERTAIN)::equals, list -> System.out.println("+?")),
                        Rule.of(Arrays.asList(NEGATIVE, UNCERTAIN)::equals, list -> System.out.println("-?"))
                )
        );

rulebook.invokeMatchingRules(Arrays.asList(NEGATIVE, POSITIVE, NEGATIVE, NEGATIVE));
rulebook.invokeMatchingRules(Arrays.asList(NEGATIVE, POSITIVE));
rulebook.invokeMatchingRules(Arrays.asList(UNCERTAIN, UNCERTAIN));
rulebook.invokeMatchingRules(Arrays.asList(NEGATIVE, POSITIVE));
rulebook.invokeMatchingRules(Arrays.asList(POSITIVE));
rulebook.invokeMatchingRules(new ArrayList<>());
 ```