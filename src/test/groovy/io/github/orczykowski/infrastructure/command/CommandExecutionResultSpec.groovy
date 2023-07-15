package io.github.orczykowski.infrastructure.command

import spock.lang.Specification

class CommandExecutionResultSpec extends Specification {
    def "should create Failure execution result with executed command and failure reason"() {
        when:
        def failureResult = new CommandExecutionResult.Failure("some command", "something went wrong")

        then:
        failureResult.result() == "Problem with execute command [some command], message[something went wrong]"
    }

    def "should create Successful execution result with string result"() {
        when:
        def failureResult = new CommandExecutionResult.Success("everything gonna be all right")

        then:
        failureResult.result() == "everything gonna be all right"
    }
}
