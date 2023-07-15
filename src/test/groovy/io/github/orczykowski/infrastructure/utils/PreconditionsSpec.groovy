package io.github.orczykowski.infrastructure.utils

import spock.lang.Specification

class PreconditionsSpec extends Specification {
    def "should throw illegal argument exception when passed value is null"() {
        when:
        Preconditions.checkRequiredArgument(null)
        then:
        thrown(IllegalArgumentException)
    }

    def "should do nothing when argument is pass"() {
        when:
        Preconditions.checkRequiredArgument(value)

        then:
        notThrown(IllegalArgumentException)
        where:
        value << [0, 1, "", " ", new Object()]
    }

    def "should throw exception when condition in not satisfied for value"() {
        when:
        Preconditions.checkState((a) -> a < 1, 2)
        then:
        thrown(IllegalStateException)
    }

    def "should do nothing when condition is satisfied"() {
        when:
        Preconditions.checkState((a) -> a < 1, 0)

        then:
        notThrown(IllegalArgumentException)
    }

    def "should throw custom exception when  condition in not satisfied for value"() {
        when:
        Preconditions.check((a) -> a < 1, 2, () -> new CustomTestException())
        then:
        thrown(CustomTestException)
    }

    def "should do nothing when condition in satisfied for value"() {
        when:
        Preconditions.check((a) -> a < 1, 0, () -> new CustomTestException())
        then:
        notThrown(CustomTestException)
    }


    static class CustomTestException extends RuntimeException {
    }

}
