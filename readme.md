![alt text](https://boringstuff.pl/wp-content/uploads/2020/12/cropped-more-blue-logo.png "boringstuff.pl")
## Multi Project Stats Calculator
made by [BORINGSTUFF](http://boringstuff.pl)

### description
It is project for fun and teach new features of current java version (now it's 15). It calculates contribution statistics for passed project repositories and produce report with:
- aggregated contribution statistics for project
- user contribution for per project
- user aggregated statistics for all projects
Possible report formats: CSV, HTML PDF

The statistics will be calculated from begin date from argument (if not pass it is 1970-01-01), current master code version and repositories defined in repository file.
Report will be store in reports dictionary in root of project.

### requirements
- installed [JAVA 15](https://openjdk.java.net/projects/jdk/15/)
- installed GIT 
- installed [GIT FAME](https://github.com/oleander/git-fame-rb)
- added your [SSH KEY](https://www.ssh.com/ssh/keygen/) to git ([example for github](https://docs.github.com/en/free-pro-team@latest/github/authenticating-to-github/adding-a-new-ssh-key-to-your-github-account)) or pass user and password 

### how to run
using gradlew wrapper ```./gradlew clean run ARGS``` or short ```./gradlew -q gR ARGS```
where ARGS is:

parameter name | is require | description | default value   
--- | --- | --- | ---
dateFrom | false | start calculation date in format `yyyy-mm-dd` | `1970-01-01`
resultDir | false |  path to directory where will be save report | `reports`
repoPath | false | path to json file where you have project repositories | `projects.json`
reportFormat | false | report format available HTML,  CSV,  PDF | `HTML`
timout | false |  calculation task timeout in seconds | `1h`
workingDir | false |  place where will be pull repos | `/tmp/multi-project-contributions-stats-calculator-working-dir/` 
numberOfThreads | false |  number of concurrent threads | `10`
queueSize | false |  size of thread pool queue | `40`

tip: when you run app using plain "java" command remember to add `--enable-preview` to run args

### test
- run `./gradlew clean test`
- test coverage `./gradlew clean jacocoTestReport` 

### projects repositories file 
JSon structure of projects definitions
```json
{
  "projects": [
    {
      "url": "ssh://git@boringstuff/prjects/some-project",
      "excludePaths": ["/path_to_exclude"]
    }, 
    {
       "url": "ssh://git@boringstuff/prjects/enother-project",
       "excludePaths": []
    }   
  ]
}
```

