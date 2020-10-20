package pl.boringstuff.infrastructure


import static pl.boringstuff.infrastructure.APP_DEFAULTS.DATE_FROM
import static pl.boringstuff.infrastructure.APP_DEFAULTS.REPORT_FORMAT
import static pl.boringstuff.infrastructure.APP_DEFAULTS.REPO_PATH
import static pl.boringstuff.infrastructure.APP_DEFAULTS.RESULT_DIR
import spock.lang.Specification

class APP_DEFAULTSSpec extends Specification {

  def "should contains correct values"() {
    expect:
      REPORT_FORMAT.value == "HTML"
      RESULT_DIR.value == "reports"
      REPO_PATH.value == "projects.json"
      DATE_FROM.value == "1970-01-01"
  }
}
