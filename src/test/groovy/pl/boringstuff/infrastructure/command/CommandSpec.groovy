package pl.boringstuff.infrastructure.command

import static ExecutableSystemCommand.newCommand
import spock.lang.Specification

class CommandSpec extends Specification {

  def "should throw exception when try create command from blank string"() {
    given:
      def command = ExecutableSystemCommand.newCommand(commandStr)

    when:
      command.buildSystemCommand()

    then:
      thrown(IllegalStateException)

    where:
      commandStr << ["", " ", null]
  }

  def "should create command without extra params"() {
    given:
      def command = ExecutableSystemCommand.newCommand("sh")

    when:
      def executableCommand = command.buildSystemCommand()

    then:
      executableCommand.command() == "sh"
  }

  def "should concat main command with extra params"() {
    given:
      def command = ExecutableSystemCommand.newCommand("cmd")
              .withArgs(["--verbose", "someValue=2", "--anotherValue=3", "lastVal 4"])

    when:
      def executableCommand = command.buildSystemCommand()

    then:
      executableCommand.command() == "cmd --verbose someValue=2 --anotherValue=3 lastVal 4"
  }

  def "should filter blank args"() {
    given:
      def command = ExecutableSystemCommand.newCommand("cmd")
              .withArgs(["--verbose", "", " ", null])

    when:
      def executableCommand = command.buildSystemCommand()

    then:
      executableCommand.command() == "cmd --verbose"
  }

  def "should create folder from dictionary path"() {
    given:
      def command = ExecutableSystemCommand.newCommand("cmd")
              .inDictionary("/tmp")
    when:
      def executableCommand = command.buildSystemCommand()

    then:
      executableCommand.directory().path == "/tmp"
  }


  def "should execute command and return successful"() {
    given:
      def command = newCommand("ls").buildSystemCommand()

    when:
      def executionResult = command.execute()

    then:
      executionResult != null
  }
}
