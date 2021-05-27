# metrics-scala

## Project metrics

  - In-process  
    [![Scala CI](https://img.shields.io/github/workflow/status/LoyolaChicagoCode/metrics-scala/Scala%20CI)](https://github.com/LoyolaChicagoCode/metrics-scala/actions)
    [![codecov](https://img.shields.io/codecov/c/github/LoyolaChicagoCode/metrics-scala)](https://codecov.io/gh/LoyolaChicagoCode/metrics-scala)
    ![Commit Activity](https://img.shields.io/github/commit-activity/m/LoyolaChicagoCode/metrics-scala)
    [![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/LoyolaChicagoCode/metrics-scala.svg)](http://isitmaintained.com/project/LoyolaChicagoCode/metrics-scala "Average time to resolve an issue")
    [![Percentage of issues still open](http://isitmaintained.com/badge/open/LoyolaChicagoCode/metrics-scala.svg)](http://isitmaintained.com/project/LoyolaChicagoCode/metrics-scala "Percentage of issues still open")
  
  - Complexity  
    ![Code Size](https://img.shields.io/github/languages/code-size/LoyolaChicagoCode/metrics-scala)
    [![Codacy Badge](https://img.shields.io/codacy/grade/20f5854f50c94a448968683ad33a687f)](https://www.codacy.com/gh/LoyolaChicagoCode/metrics-scala/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=LoyolaChicagoCode/metrics-scala&amp;utm_campaign=Badge_Grade)
    [![Maintainability](https://img.shields.io/codeclimate/maintainability/LoyolaChicagoCode/metrics-scala)](https://codeclimate.com/github/LoyolaChicagoCode/metrics-scala/maintainability)
    [![Technical Debt](https://img.shields.io/codeclimate/tech-debt/LoyolaChicagoCode/metrics-scala)](https://codeclimate.com/github/LoyolaChicagoCode/metrics-scala/trends/technical_debt)
    [![CodeFactor](https://img.shields.io/codefactor/grade/github/LoyolaChicagoCode/metrics-scala)](https://www.codefactor.io/repository/github/LoyolaChicagoCode/metrics-scala)
 
  - Other  
    [![License](http://img.shields.io/:license-mit-blue.svg)](http://doge.mit-license.org)

## Overview

`metrics-scala` contains some ongoing Scala-based experiments for the [Loyola SSL metrics pipeline](https://ssl.cs.luc.edu/projects/metricsDashboard). 

## Description

```default
./target/universal/stage/bin/metrics-scala --help    
git-import
Git importer for Scala-based metrics experiments - currently just displays code size per commit
  -r --repo <path>      local repository path (defaults to .)
  -d --database <path>  local SQLite (defaults to ./git-import.sqlite)
```

## Requirements

- sbt 1.x (for building)
- Java 11 or newer  (for building and deploying)

## Distribution

The task

```sbt universal:packageBin```

creates a universal zip file

```target/universal/metrics-scala-<version>.zip```

that can be distributed and deployed by unzipping it in a suitable directory. 

To run, invoke

```<install-dir>/bin/metrics-scala```

using the options listed above.

To create other distribution formats, follow the [sbt-native-packager documentation](https://www.scala-sbt.org/sbt-native-packager/gettingstarted.html#packaging-formats).

## References

- https://wiki.eclipse.org/JGit/User_Guide
- https://download.eclipse.org/jgit/site/5.11.1.202105131744-r/apidocs/index.html
- https://stackoverflow.com/questions/12342152/jgit-and-finding-the-head
- http://shafiul.github.io/gitbook/1_the_git_object_model.html
- https://www.vogella.com/tutorials/JGit/article.html <- good
- https://github.com/centic9/jgit-cookbook <- very detailed
- https://github-api.kohsuke.org/
- command-line SLOC tools: cloc, sloccount, tokei, scc https://github.com/XAMPPRocky/tokei
- https://en.wikipedia.org/wiki/Source_lines_of_code
