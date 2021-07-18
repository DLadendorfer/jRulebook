package aero.rb.rulebook

/**
 * A roolbook consists of one or more rules
 */
class Rulebook<T> private constructor(val rulesets: Array<out IRuleset<T>>) : IRulebook<T> {
    companion object {
        /**
         * Returns an instance of Rulebook consisting of the given rulesets.
         *
         * @param rulesets the rulessets; must not be null
         * @return a new instance of Rulebook
         */
        @JvmStatic
        @SafeVarargs
        fun <T> of(vararg rulesets: IRuleset<T>): Rulebook<T> {
            rulesets.forEach { rs -> if (rs == null) throw NullPointerException("Rulesets contains null object") }
            return Rulebook(rulesets)
        }
    }

    override fun invokeMatchingRules(obj: T) {
        rulesets.forEach { it.invokeMatchingRules(obj) }
    }

    override fun getMatchingRules(obj: T): List<IRule<T>> {
        val matches: MutableList<IRule<T>> = ArrayList()
        rulesets.forEach { matches.addAll(it.getMatchingRules(obj)) }
        return matches
    }

    override fun hasMatchingRule(obj: T): Boolean = rulesets.any { it.hasMatchingRule(obj) }
}