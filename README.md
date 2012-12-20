# Progfun-stats

This repository contains the data from the "progfun" survey, and the source code for many of the plots seen in the [_Functional Programming Principles in Scala: Impressions and Statistics_](http://docs.scala-lang.org/news/functional-programming-principles-in-scala-impressions-and-statistics.html) article on scala-lang.org.

We encourage you to checkout the repository, to generate the plots shown in the article yourself, and to explore the data further. Given sufficient interest, we plan on writing a follow-up article, and we'd love to hear from you! If you've found anything interesting in the data, or otherwise have a cool/interesting visualization to share, please don't hesitate to let us know! We'll include as many as we can in our follow-up article.

(_Please note:_ we plan on adding a few more graph types and doing some organizational work in this repository, so keep an eye out for improvements!)

Pull requests are also welcome. Please contact [Heather](http://people.epfl.ch/heather.miller), post in the comments [in the article](http://docs.scala-lang.org/news/functional-programming-principles-in-scala-impressions-and-statistics.html)

## The Survey

The survey was emailed to the ~50,000 students enrolled in the [_Functional Programming Principles in Scala_](https://www.coursera.org/course/progfun) MOOC on [Coursera](http://coursera.org). Out of those students, 7,492 responded.

The following questions (with their available choices) were on the survey:

- **What's your age group?**
  - Possible choices: 10-17, 18-24, 25-34, 35-44, 45-54, 55-64, 65+
- **What country do you live in?**
- **What's your highest degree?**
  - Possible choices: No High School (or equivalent), Some High School (or equivalent), High School (or equivalent), Some College (or equivalent), Bachelor's Degree (or equivalent), Master's Degree (or equivalent), Doctorate Degree (or equivalent), Other (fill in)
- **If applicable, what field of study was your highest degree in?**
  - Possible choices: Computer Science, Computer/Software Engineering, Electrical Engineering, Mechanical Engineering, Statistics/Mathematics, Business/Marketing, Physics, Life Sciences, Liberal Arts, Fine Arts, Other
- **How many years have you been programming?**
  - Possible choices: 0-1 years, 1-2 years, 3-5 years, 5-7 years, 7-10 years, 10-15 years, 15-20 years, 20+ years
- **Did you finish the course?**
  - Possible choices: Yes, No
- **How many hours, on average, did you spend per week on the course?**
  - Possible choices: 1-2 hours, 2-4 hours, 4-6 hours, 6-8 hours, 8-10 hours, 10-12 hours, 12+ hours
- **How difficult did you find the course overall?**
  - Possible choices: 1 - Too Easy, 2, 3, 4, 5 - Too Hard
- **How difficult did you find the homework assignments?**
  - Possible choices: 1 - Too Easy, 2, 3, 4, 5 - Too Hard
- **Do you feel the course was worth the time you invested in it?**
  - Possible choices: 1 - Disagree Completely, 2, 3, 4, 5 - Agree Completely
- **Would you take a follow-up functional programming in Scala course, if it were offered?**
  - Possible choices: 1 - Not Interested, 2, 3, 4, 5 - Absolutely!
- **What interested you in the Functional Programming Principles in Scala course?**
  - Possible choices: Personal Interest/Curiosity, University Studies, Helps With Profession
- **Where do you plan to apply what you've learned in this course?**
  - Possible choices: Personal projects, Individual project at work, Team project at work, University projects, No application plans, attended for general interest
- **What is your preferred editor, for use outside of this course?**
  - Possible choices: Eclipse, IntelliJ, Sublime Text, Visual Studio, XCode, TextMate, emacs, vim, Other
- **What editor did you end up using for the majority of the course?**
  - Possible choices: Eclipse, IntelliJ, Sublime Text, TextMate, emacs, vim, Other
- **What experience do you have with other programming languages or paradigms?** (asked for Java, C/C++/Objective-C, Python / Ruby / Perl / other scripting language, C#/.NET, JavaScript, Haskell/OCaml/ML/F#, /Scheme/Clojure)
  - Possible choices: No experience / not seen it at all, I've seen and understand some code, I have some experience writing code, I'm fluent, I'm an expert

## Dependencies

You need SBT 0.12 installed. If you don't already have SBT, [make sure to grab it](https://github.com/harrah/xsbt/wiki/Getting-Started-Setup).

If you've got a mac, you can use either [Macports](http://macports.org/):

    $ sudo port install sbt

Or [HomeBrew](http://mxcl.github.com/homebrew/):

    $ brew install sbt

For Linux users, you can use APT (other package managers are available [on the getting started page]([make sure to grab it](https://github.com/harrah/xsbt/wiki/Getting-Started-Setup)))

    apt-get install sbt

Windows users can either install the [msi](http://scalasbt.artifactoryonline.com/scalasbt/sbt-native-packages/org/scala-sbt/sbt/0.12.0/sbt.msi) or [create a batch file](https://github.com/harrah/xsbt/wiki/Getting-Started-Setup).

## Running

The code in this repository generates HTML/JavaScript visualizations of some of the data collected in the above survey.

To run,

1. Start SBT in the root directory of progfun-stats.
2. `run`
3. Select the plot that you wish to generate. Or select `ProgfunStats` to generate them all.
4. All HTML/JavaScript is generated in the html/ directory.

## Explore the data with Scala!

To generate a new graph, define a new singleton object in [ProgfunStats.scala](https://github.com/heathermiller/progfun-stats/blob/master/src/main/scala/progfun/ProgfunStats.scala), and make sure to extend the proper `GraphFactory` (current choices are `GroupedBarGraphFactory`, `SimpleBarGraphFactory`, `PieChartFactory`, `WorldMapFactory`).

In all cases, there are a few fields that you need to implement-- for example, the `name` field, which corresponds to the name of the HTML file you wish to output, as well as the `data` field, which represents that data that you would like to display on a graph.

Have a look at the visualizations already implemented (the ones visible [in the article](http://docs.scala-lang.org/news/functional-programming-principles-in-scala-impressions-and-statistics.html)) in [ProgfunStats.scala](https://github.com/heathermiller/progfun-stats/blob/master/src/main/scala/progfun/ProgfunStats.scala)

## LICENSE

The source code and data within this repository are open source under the Apache License V2.

A summary of the license can be found [here](http://www.oss-watch.ac.uk/resources/apache2).
Like all licences the Apache License v2 grants certain rights under certain conditions. In brief a licensee of Apache Licensed V2 software can:

- copy, modify and distribute the covered software in source and/or binary forms
- exercise patent rights that would normally only extend to the licensor provided that:
- all copies, modified or unmodified, are accompanied by a copy of the licence
- all modifications are clearly marked as being the work of the modifier

See the [V2 Apache License](http://www.apache.org/licenses/LICENSE-2.0) itself, or the [summary](http://www.oss-watch.ac.uk/resources/apache2) for more information.

### Apache License V2

Copyright 2012 École Polytechnique Fédérale de Lausanne (EPFL)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


