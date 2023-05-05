package pl.orczykowski.infrastructure.utils

import nl.jqno.equalsverifier.EqualsVerifier
import spock.lang.Specification

class PairSpec extends Specification {

    def "should create pair of two any type objects"() {
        when:
        def pari = Pair.of(first, second)
        then:
        pari.first() == first
        pari.second() == second

        where:
        first | second
        1     | 2
        "a"   | Object
        "abc" | 1
        1     | true
    }

    def "hashCode equals contract is fulfilled"() {
        expect:
        EqualsVerifier.forClass(Pair).usingGetClass().verify()
    }
}
