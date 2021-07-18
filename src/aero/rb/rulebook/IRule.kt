package aero.rb.rulebook

/**
 * A rule consists out of a condition and consumers which should be invoked if the test object matches the condition.
 */
interface IRule<T> {
    /**
     * Checks if the given object matches the rule condition and invokes the rule actions.
     *
     * @param obj the object to check
     */
    fun invokeIfMatching(obj: T)

    /**
     * Returns whether the given object matches the rule's condition.
     *
     * @param obj the object to check
     * @return true if the obj matches the rule
     */
    fun isMatching(obj: T): Boolean
}