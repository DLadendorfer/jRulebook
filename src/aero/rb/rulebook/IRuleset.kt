package aero.rb.rulebook

/**
 * A ruleset consists of one or more rules.
 */
interface IRuleset<T> {
    /**
     * Checks each rule against the given object and invokes the rule actions if it matches the rule condition.
     *
     * @param obj the object to check
     */
    fun invokeMatchingRules(obj: T)

    /**
     * Checks each rule against the given object and returns the rules which have a matching condition.
     *
     * @param obj the object to check
     * @return matching rules as a list; never null
     */
    fun getMatchingRules(obj: T): List<IRule<T>>

    /**
     * Returns true if at least one rule matches the given object.
     *
     * @param obj the object to check
     * @return true if at least one rule matches the given object
     */
    fun hasMatchingRule(obj: T): Boolean
}