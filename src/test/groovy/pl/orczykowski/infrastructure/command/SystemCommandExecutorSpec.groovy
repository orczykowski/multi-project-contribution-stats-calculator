package pl.orczykowski.infrastructure.command

import spock.lang.Specification

import static ExecutableSystemCommand.newCommand

class SystemCommandExecutorSpec extends Specification {

    def "should execute command and return successful result"() {
        given:
        def command = newCommand("ls").buildSystemCommand()

        when:
        def executionResult = command.execute()

        then:
        executionResult instanceof CommandExecutionResult.Success
    }

    def "should contains string result with all content of test resource"() {
        given:
        def command = newCommand('echo test')
                .buildSystemCommand()

        when:
        def executionResult = command.execute()

        then:
        executionResult instanceof CommandExecutionResult.Success
        executionResult.result().trim() == "test"
    }

    def "should return failure info with message when execution is failed"() {
        given:

        def commandString = "someNonExistingCommandInRegularSystem"
        def command = newCommand(commandString).buildSystemCommand()

        when:
        def executionResult = command.execute()

        then:
        executionResult instanceof CommandExecutionResult.Failure
        !executionResult.result().isEmpty()
    }

}
