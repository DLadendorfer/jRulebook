package aero.rb.rulebook

import java.util.function.Predicate

/**
 * A ruleset consists of one or more rules.
 */
class Ruleset<T> private constructor(val condition: Predicate<T>, val rules: Array<out IRule<T>>) : IRuleset<T> {
    companion object {
        /**
         * Returns an instance of Ruleset consisting of the given rules.
         *
         * @param condition the rule's condition
         * @param rules the rules; must not be null
         * @return a new instance of Ruleset
         */
        @JvmStatic
        @SafeVarargs
        fun <T> of(condition: Predicate<T>, vararg rules: IRule<T>): Ruleset<T> {
            rules.forEach { r -> if (r == null) throw NullPointerException("Rulesets contains null object") }
            return Ruleset(condition, rules)
        }
    }

    override fun invokeMatchingRules(obj: T) {
        if(condition.test(obj)) {
            rules.forEach { it.invokeIfMatching(obj) }
        }
    }

    override fun getMatchingRules(obj: T): List<IRule<T>> =
        if (condition.test(obj)) rules.filter { it.isMatching(obj) }.toList() else emptyList()

    override fun hasMatchingRule(obj: T): Boolean = condition.test(obj) && rules.any { it.isMatching(obj) }
}