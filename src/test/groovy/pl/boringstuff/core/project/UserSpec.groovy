package pl.boringstuff.core.project

import nl.jqno.equalsverifier.EqualsVerifier
import spock.lang.Specification

class UserSpec extends Specification {
  def "shod remove white space from name"() {
    when:
      def user = new User(name)

    then:
      user.name() == "hulk"
    where:
      name << [" hulk", " hulk "]
  }

  def "shod transform name to lower case"() {
    when:
      def user = new User(name)

    then:
      user.name() == "hulk"
    where:
      name << ["HULK", "Hulk"]
  }

  def "should set default name when name is blank"() {
    when:
      def user = new User(name)

    then:
      user.name() == "<unknown user>"
    where:
      name << ["", null, " "]
  }

  def "hashCode equals contract is fulfilled"() {
    expect:
      EqualsVerifier.forClass(User).usingGetClass().verify()
  }
}
