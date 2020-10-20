## Multi Project Stats Calculator
###### made by [BORINGSTUFF](http://boringstuff.pl)


It is project for fun and teach new features of current java version java (now it's 15). It calculates contribution statistics for passed project repositories and produce report with:
- aggregated contribution statistics for project
- user contribution for per project
- user aggregated statistics for all projects

Statistics are calculated begin date from argument (if not pass it is 1970-01-01), current master code version and repositories defined in repository file.
Report will be save in reports dictionary in root of projec.

### requirements
- installed [JAVA 15](https://openjdk.java.net/projects/jdk/15/)
- installed GIT 
- installed [GIT FAME](https://github.com/oleander/git-fame-rb)
- added your [SSH KEY](https://www.ssh.com/ssh/keygen/) to git ([example for github](https://docs.github.com/en/free-pro-team@latest/github/authenticating-to-github/adding-a-new-ssh-key-to-your-github-account)) or pass user and password 

### how to run
using gradlew wrapper ```./gradlew -q generateReport ARGS``` or short ```./gradlew -q gR ARGS```
where ARGS is:
 - [optional] date from in format `yyyy-mm-dd` if not pass it will be set on `1970-01-01` 
 - [optional] path to directory where will be save report default its `reports` in root of project
 - [optional] path to json file where you have lister project repositories, default is `resources/projects.json`

tip: add `--enable-preview` to run args

### projects repositories file 
Json structure of project definition
```
{
  "projects": [
    {
      "url": "ssh://git@boringstuff/prjects/some-project",
      "excludePaths": ["/path_to_genereted_big_cources"]
    },
    ...
  ]
}
```
