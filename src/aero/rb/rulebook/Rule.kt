package aero.rb.rulebook

import java.util.function.Consumer
import java.util.function.Predicate

/**
 * A rule consists out of a condition and consumers which should be invoked if the test object matches the condition.
 */
class Rule<T> private constructor(val condition: Predicate<T>, val consumers: Array<out Consumer<T>>) : IRule<T> {
    companion object {
        /**
         * Returns an instance of Rule consisting of the given consumers.
         *
         * @param consumers the consumers; must not be null
         * @return a new instance of Rulebook
         */
        @JvmStatic
        @SafeVarargs
        fun <T> of(condition: Predicate<T>, vararg consumers: Consumer<T>): Rule<T> {
            consumers.forEach { c -> if (c == null) throw NullPointerException("Consumers contains null object") }
            return Rule(condition, consumers)
        }
    }

    override fun invokeIfMatching(obj: T) {
        if (obj == null) throw NullPointerException("Cannot check against null object")
        if (isMatching(obj)) {
            consumers.forEach { it.accept(obj) }
        }
    }

    override fun isMatching(obj: T): Boolean =
        if (obj == null) throw NullPointerException("Cannot check against null object") else condition.test(obj)
}