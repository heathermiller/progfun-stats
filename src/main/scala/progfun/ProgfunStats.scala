package progfun

/** object representing a *grouped* bar graph of students'
 *  experience with other programming languages and paradigms
 */
object ExperienceBarGraph extends GroupedBarGraphFactory with App {
  import CourseraData._

  /* file name to output to */
  val name = "experience.html"

  /* label for the y-axis */
  def label: String = "Percentage"

  /* the captions represent the legend (the colors for each bar).
     for the moment, it must begin with a string called "Key". this will be removed shortly */
  val captions = List("Key",
                      "No experience / not seen it at all",
                      "I've seen and understand some code",
                      "I have some experience writing code",
                      "I'm fluent",
                      "I'm an expert")

  private val langs = List(javaExp, cExp, pythonExp, dotNetExp, jsExp, funcExp, lispExp)

 /* this is effectively a List[(String, List[Int])]
  * it represents the data that goes into your bar graph
  * where the String is the label on the x-axis, and the
  * List[Int] is the list of values for each bar */
  val data =
    List("Java", "C/C++/Objective-C", "Python/Ruby/Perl", "C#/.NET",
         "JavaScript", "Haskell/OCaml/ML/F#", "Lisp/Scheme/Clojure").zipWithIndex map { case (lang, langNum) =>
      val percentages = 1 to 5 map { i =>
        val num = langs(langNum).filter(_ == captions(i)).size
        (num.toDouble / total * 100).round.toInt
      }
      (lang, percentages.toList)
    }

  writeHtml()
}

/** object representing a *grouped* bar graph of students'
 *  reported difficulty relative to Java/C/C++/FP expertise
 */
object ExperienceDifficultyBarGraph extends GroupedBarGraphFactory with App {
  import CourseraData.users

  /* file name to output to */
  val name = "experience-vs-difficulty.html"

  /* width and height of final plot. Overrides default of 960 x 480 */
  override val width = 450
  override val height = 240

  /* the captions represent the legend (the colors for each bar).
     for the moment, it must begin with a string called "Key". this will be removed shortly */
  val captions = List("Key","1","2","3","4","5")

  /* label for the y-axis */
  def label = "Percentage"

  def getExperts(selector: User => String) =
    users.filter(user => selector(user) == "I'm an expert" || selector(user) == "I'm fluent")

  val cers    = getExperts(_.cExp)
  val javaers = getExperts(_.javaExp)
  val fpers   = getExperts(_.funcExp)

  def makeDifficultyMap(usrs: List[User]): Map[Int, Long] =
    usrs.groupBy(user => user.difficultyHW)
        .map(kv => (kv._1, (kv._2.length.toDouble/usrs.length*100).round))

  val difficultyC    = makeDifficultyMap(cers)
  val difficultyJava = makeDifficultyMap(javaers)
  val difficultyFP   = makeDifficultyMap(fpers)

 /* this effectively returns a List[(String, Int)]
  * it represents the data that goes into your bar graph
  * where the String is the label on the x-axis, and the
  * Int is the value for each bar */
  def data =
    List(
      ("C/C++/Objective-C",   difficultyC.toList.sorted.map(_._2)),
      ("Java",                difficultyJava.toList.sorted.map(_._2)),
      ("Haskell/OCaml/ML/F#", difficultyFP.toList.sorted.map(_._2))
    )

  writeHtml()
}

/** object representing a *grouped* bar graph of students'
 *  reported difficulty relative to educational background
 */
object BackgroundDifficultyBarGraph extends GroupedBarGraphFactory with App {
  import CourseraData.users

  /* file name to output to */
  val name = "background-vs-difficulty.html"

  /* width and height of final plot. Overrides default of 960 x 480 */
  override val width = 450
  override val height = 240

  /* the captions represent the legend (the colors for each bar).
   for the moment, it must begin with a string called "Key". this will be removed shortly */
  val captions = List("Key","1","2","3","4","5")

  /* label for the y-axis */
  def label = "Percentage"

  val nonCSFields = List(
    "Electrical Engineering",
    "Mechanical Engineering",
    "Business/Marketing",
    "Physics",
    "Life Sciences",
    "Liberal Arts",
    "Fine Arts"
  )

  val nonCSUsers = users.filter(user => nonCSFields.contains(user.field))
  val csUsers    = users.filter(user => user.field == "Computer Science" || user.field == "Computer/Software Engineering")

  def makeDifficultyMap(usrs: List[User]): Map[Int, Long] =
    usrs.groupBy(user => user.difficultyHW)
        .map(kv => (kv._1, (kv._2.length.toDouble/usrs.length*100).round))

  val difficultyNonCS = makeDifficultyMap(nonCSUsers)
  val difficultyCS    = makeDifficultyMap(csUsers)
  val difficultyAll   = makeDifficultyMap(users)

 /* this effectively returns a List[(String, Int)]
  * it represents the data that goes into your bar graph
  * where the String is the label on the x-axis, and the
  * Int is the value for each bar */
  def data =
    List(
      ("Non-CS Field",    difficultyNonCS.toList.sorted.map(_._2)),
      ("All Respondents", difficultyAll.toList.sorted.map(_._2)),
      ("CS or CE",        difficultyCS.toList.sorted.map(_._2))
    )

  writeHtml()
}

/** object representing a *grouped* bar graph of students'
 *  reported difficulty relative to highest level of education
 */
object EducationDifficultyBarGraph extends GroupedBarGraphFactory with App {
  import CourseraData.users

  /* file name to output to */
  val name = "education-vs-difficulty.html"

  /* width and height of final plot. Overrides default of 960 x 480 */
  override val width = 800
  override val height = 320

  /* the captions represent the legend (the colors for each bar).
   for the moment, it must begin with a string called "Key". this will be removed shortly */
  val captions = List("Key","1","2","3","4","5")

  /* label for the y-axis */
  def label = "Percentage"

  def makeDifficultyByDegree(deg: String): Map[Int, Long] = {
    val usrsByDeg = users.filter(user => user.degree == deg)
    val perc = usrsByDeg.groupBy(user => user.difficultyHW)
                        .map(kv => (kv._1, (kv._2.length.toDouble/usrsByDeg.length*100).round))

    val withAllKeys = (List(1, 2, 3, 4, 5) map { key =>
      if (perc.isDefinedAt(key)) (key, perc(key))
      else (key, 0L)
    }).toMap

    withAllKeys
  }

  val difficultyNoHs      = makeDifficultyByDegree("No High School (or equivalent)")
  val difficultySomeHs    = makeDifficultyByDegree("Some High School (or equivalent)")
  val difficultyHs        = makeDifficultyByDegree("High School (or equivalent)")
  val difficultySomeColl  = makeDifficultyByDegree("Some College (or equivalent)")
  val difficultyBachelor  = makeDifficultyByDegree("Bachelor's Degree (or equivalent)")
  val difficultyMaster    = makeDifficultyByDegree("Master's Degree (or equivalent)")
  val difficultyDoctorate = makeDifficultyByDegree("Doctorate Degree (or equivalent)")

 /* this effectively returns a List[(String, Int)]
  * it represents the data that goes into your bar graph
  * where the String is the label on the x-axis, and the
  * Int is the value for each bar */
  def data =
    List(
      ("No HS",     difficultyNoHs.toList.sorted.map(_._2)),
      ("Some HS",   difficultySomeHs.toList.sorted.map(_._2)),
      ("HS",        difficultyHs.toList.sorted.map(_._2)),
      ("Some Univ", difficultySomeColl.toList.sorted.map(_._2)),
      ("Bachelor",  difficultyBachelor.toList.sorted.map(_._2)),
      ("Master",    difficultyMaster.toList.sorted.map(_._2)),
      ("PhD",       difficultyDoctorate.toList.sorted.map(_._2))
    )

  writeHtml()
}

/** object representing the plot of population density vs the world map
 */
 // please note that WorldMapFactory needs a bit of reorganization
 // so be aware that it might soon be revised a bit.
 // after reorganization, you will be provided with a list of ISOs,
 // and you will have to provide values (AnyVal) associated with each
 // and implement a field called `data` as with other Factories
object WorldMapDensityGraph extends WorldMapFactory with App {

  /* file name to output to */
  val name = "worldmap-density.html"
  writeHtml()
}

/** object representing a *simple* bar graph of students'
 *  highest level of education
 */
object EducationBarGraph extends SimpleBarGraphFactory with App {
  import CourseraData.degrees

  /* file name to output to */
  val name = "education.html"

  /* width and height of final plot. Overrides default of 960 x 480 */
  override val width = 450
  override val height = 500
  override val color = "#CF5300"
  override val margin = "{top: 20, right: 20, bottom: 60, left: 40}"

  /* the label on the y axis */
  val label = "Percentage"

  val degreeLabels = List(
      "No HS",
      "Some HS",
      "HS",
      "Some Univ",
      "Bachelor",
      "Master",
      "PhD",
      "Other"
    )

 /* this is effectively a List[(String, Int)]
  * it represents the data that goes into your bar graph
  * where the String is the label on the x-axis, and the
  * Int is the value for each bar */
  val data = {
    val degreeOpts = List(
      "No High School (or equivalent)",
      "Some High School (or equivalent)",
      "High School (or equivalent)",
      "Some College (or equivalent)",
      "Bachelor's Degree (or equivalent)",
      "Master's Degree (or equivalent)",
      "Doctorate Degree (or equivalent)"
    )
    val toShort = (degreeOpts zip degreeLabels).toMap
    val (listed, other) = degrees.partition(degree => degreeOpts.contains(degree))
    listed.groupBy(deg => deg)
          .map { case (k, v) => (toShort(k), v.length) }
          .toList
          .sortWith((p1, p2) => degreeLabels.indexOf(p1._1) < degreeLabels.indexOf(p2._1))
          .map { case (deg, count) => (deg, (count.toDouble / degrees.length * 100).round.toInt) }
  }

  writeHtml()
}

/** object representing a pie chart which represents students'
 *  what interested students in the course
 */
object WhatInterestedYouPieChart extends PieChartFactory with App {
  /* file name to output to */
  val name = "what-interested-you.html"

 /* this represents the data that goes into your pie chart
  * where the String is the label, and the Int is the
  * value for each slice of the chart */
  def data: List[(String, Int)] = getFreqs(CourseraData.whatInterestedYou)
  writeHtml()
}

/** object representing a pie chart which represents students'
 *  plan to apply what they have learned in the course
 */
object WhereApplyPieChart extends PieChartFactory with App {
  /* file name to output to */
  val name = "where-apply.html"

 /* this represents the data that goes into your pie chart
    where the String is the label, and the Int is the
    value for each slice of the chart */
  def data: List[(String, Int)] = getFreqs(CourseraData.whereApply)
  writeHtml()
}

/** object representing a *grouped* bar graph of preferred
 *  editor use outside of the course versus editor use for
 *  the course
 */
object EditorGroupedBarGraph extends GroupedBarGraphFactory with App {
  import CourseraData.{ prefEditor, courseEditor, total }

  /* file name to output to */
  val name = "editors.html"

  /* width and height of final plot. Overrides default of 960 x 480 */
  override val width = 500
  override val height = 240

  /* the captions represent the legend (the colors for each bar).
     for the moment, it must begin with a string called "Key". this will be removed shortly */
  val captions = List("Key","Preferred for use outside of the course","Used for majority of course")

  /* the label on the y axis */
  val label = "Percentage"

  def edtitorFreqs(choices: List[String], xs: List[String]) = {
    val (editors, other) = getFreqWithOther(choices, xs)
    val (netbeans, trueOther) = other.map(_.toLowerCase)
                                     .partition(_ == "netbeans")
    val editorCount = editors ++ List(("Netbeans", netbeans.length),("Other", trueOther.length))
    editorCount
  }
  val prefEditorChoices = List("Eclipse","IntelliJ","Sublime Text","Visual Studio","XCode","TextMate","emacs","vim")
  val prefEditorCount = edtitorFreqs(prefEditorChoices, prefEditor)

  val prefEditorToGraph = {
    val (vsOrXcode, wrongOther) = prefEditorCount.partition { case (ed, cnt) => ed == "Visual Studio" || ed == "XCode" }
    val toAddToOther = vsOrXcode(0)._2 + vsOrXcode(1)._2
    wrongOther.map { case (ed, cnt) => if (ed == "Other") (ed, cnt + toAddToOther) else (ed, cnt) }
  }

  val courseEditorChoices = List("Eclipse","IntelliJ","Sublime Text","TextMate","emacs","vim")
  val courseEditorCount = edtitorFreqs(courseEditorChoices, courseEditor)

 /* this effectively returns a List[(String, Int)]
  * it represents the data that goes into your bar graph
  * where the String is the label on the x-axis, and the
  * Int is the value for each bar */
  def data = {
    val (courseEds, courseCnts) = courseEditorCount.unzip
    val (prefEds, prefCnts) = prefEditorToGraph.unzip
    val unsorted = prefEds.zip(courseCnts.zip(prefCnts).map { case (courseCount, prefCount) =>
      val coursePerc = percentOf(courseCount, total)
      val prefPerc   = percentOf(prefCount, total)
      List(prefPerc, coursePerc)
    })
    val refList = courseEditorChoices ++ List("Netbeans", "Other")
    unsorted.sortWith((p, q) => refList.indexOf(p._1) < refList.indexOf(q._1))
            .map(p => if (p._1.startsWith("Sublime")) ("Sublime", p._2) else p)
  }

  writeHtml()
}

/** object representing a *simple* bar graph of
 *  students' interest in a follow-up course
 */
object FollowupCourseBarGraph extends SimpleBarGraphFactory with App {
  import CourseraData.followupCourse

  /* file name to output to */
  val name = "followup-course.html"

  /* width and height of final plot. Overrides default of 960 x 480 */
  override val width = 250
  override val height = 250
  override val maxy = 70
  override val color = "#5E4175"

  /* the label on the y axis */
  val label = "Percentage"

 /* this represents the data that goes into your bar graph
  * where the String is the label on the x-axis, and the
  * Int is the value for each bar */
  def data: List[(String, Int)] = {
    val counts =
      getFreqs(followupCourse)
      .sortBy(_._1)
      .map { case (name, value) =>
            (name.toString, (value.toDouble / followupCourse.length * 100).round.toInt)
      }
    val correctedLabels: List[(String, Int)] =
      List(("1 Not Interested", counts(0)._2)) ++ counts.drop(1).take(3) ++ List(("5 Absolutely!",counts(4)._2))
    correctedLabels
  }

  writeHtml()
}

/** object representing a *simple* bar graph which
 *  represents how "worth it" the course was for students
 */
object WorthItBarGraph extends SimpleBarGraphFactory with App {
  import CourseraData.worthIt

  /* file name to output to */
  val name = "worth-it.html"

  /* width and height of final plot. Overrides default of 960 x 480 */
  override val width = 250
  override val height = 250
  override val maxy = 70

  /* the label on the y axis */
  val label = "Percentage"

 /* this represents the data that goes into your bar graph
  * where the String is the label on the x-axis, and the
  * Int is the value for each bar */
  def data: List[(String, Int)] = {
    val counts =
      getFreqs(worthIt)
      .sortBy(_._1)
      .map { case (name, value) =>
            (name.toString, (value.toDouble / worthIt.length * 100).round.toInt)
      }
    val correctedLabels: List[(String, Int)] =
      List(("1 Disagree", counts(0)._2)) ++ counts.drop(1).take(3) ++ List(("5 Agree",counts(4)._2))
    correctedLabels
  }

  writeHtml()
}

/** object representing a *grouped* bar graph of students'
 *  programming languages experience relative to their age
 */
object LanguageAgeGraph extends GroupedBarGraphFactory with App {
  import CourseraData.{ ages, javaExp, cExp, pythonExp, dotNetExp, jsExp,
                       funcExp, lispExp}

  /* file name to output to */
  val name = "lang-age.html"

  /* width and height of final plot are default*/
  /* the label on the y axis */
  val label = "Percentage"

  /* the captions represent the legend (the colors for each bar) */
  val captions = List("Key") ++ ages.filter(_.length < 6).distinct.sorted

  // Extracting age of users and experience in specified programming language
  def getLangAge(ages: List[String], langExp: List[String]): List[String] =
    (ages zip langExp).filter(_._1.length < 6) // Cleaning incorrect ages
                      .filter(_._2 != "No experience / not seen it at all")
                      .filter(_._2 != "I've seen and understand some code")
                      .map(_._1)

  val langAge = Map("Java"                  -> getLangAge(ages, javaExp),
                    "C/C++/Objective-C"     -> getLangAge(ages, cExp),
                    "Python/Ruby/Perl"      -> getLangAge(ages, pythonExp),
                    "C#/.NET"               -> getLangAge(ages, dotNetExp),
                    "JavaScript"            -> getLangAge(ages, jsExp),
                    "Haskell/OCaml/ML/F#"   -> getLangAge(ages, funcExp),
                    "Lisp/Scheme/Clojure"    -> getLangAge(ages, lispExp))

  // Calculating the percentages of users' age groups with experience in
  // specified programming language
  def calculatePercentage(lst: List[String]): Map[String, Float] =
    lst.groupBy(x => x).map(x => (x._1, (x._2.size.toFloat * 100 / lst.length)))

  val langAgePercentage = langAge transform ((k, v) => calculatePercentage(v))

  def data = {
    val resultsMap = for ((k, v) <- langAgePercentage)
                     yield (k, v.toList.sortBy(_._1).map(_._2))
    resultsMap.toList
  }

  writeHtml()
}

/** object representing a *grouped* bar graph showing spent time by users relative
 *  to the experience in programming languages
 */
object LanguageSpentTime extends GroupedBarGraphFactory with App {
  import CourseraData.{ timeSpent, javaExp, cExp, pythonExp, dotNetExp, jsExp,
                       funcExp, lispExp}

  /* file name to output to */
  val name = "lang-time.html"

  /* the captions represent the legend (the colors for each bar)
     Sorting by spent time */
  val captions = List("Key") ++ timeSpent.distinct.sortBy(_.split("-|\\+")(0).toInt)

  /* width and height of final plot are default*/
  /* the label on the y axis */
  val label = "Percentage"

  // Extracting spent time and experience in specified programming language
  def getLangTimeSpent(ages: List[String], langExp: List[String]): List[String] =
    (timeSpent zip langExp).filter(_._2 != "No experience / not seen it at all")
                           .filter(_._2 != "I've seen and understand some code")
                           .map(_._1)

  val langTimeSpent = Map("Java"                  -> getLangTimeSpent(timeSpent, javaExp),
                          "C/C++/Objective-C"     -> getLangTimeSpent(timeSpent, cExp),
                          "Python/Ruby/Perl"      -> getLangTimeSpent(timeSpent, pythonExp),
                          "C#/.NET"               -> getLangTimeSpent(timeSpent, dotNetExp),
                          "JavaScript"            -> getLangTimeSpent(timeSpent, jsExp),
                          "Haskell/OCaml/ML/F#"   -> getLangTimeSpent(timeSpent, funcExp),
                          "Lisp/Scheme/Clojure"    -> getLangTimeSpent(timeSpent, lispExp))

  // Calculating the percentages in groups divided by spent time
  def calculatePercentage(lst: List[String]): Map[String, Float] =
    lst.groupBy(x => x).map(x => (x._1, (x._2.size.toFloat * 100 / lst.length)))

  val langTimePercentage = langTimeSpent transform ((k, v) => calculatePercentage(v))

  def data = {
    val resultsMap = for ((k, v) <- langTimePercentage)
                     yield (k, v.toList.sortBy(_._1.split("-|\\+")(0).toInt)
                                .map(_._2))
    resultsMap.toList
  }
  writeHtml()
}

/** Shortcut for generating all graphs.
 *  Add any new graph to this list, and in sbt,
 *  run progfun.ProgfunStats to generate all graphs
 */
object ProgfunStats extends App {
  List[GraphFactory with App](
    ExperienceBarGraph,
    ExperienceDifficultyBarGraph,
    BackgroundDifficultyBarGraph,
    EducationDifficultyBarGraph,
    WorldMapDensityGraph,
    EducationBarGraph,
    WhereApplyPieChart,
    WhatInterestedYouPieChart,
    EditorGroupedBarGraph,
    FollowupCourseBarGraph,
    WorthItBarGraph,
    LanguageAgeGraph,
    LanguageSpentTime
    ).foreach { graph =>
      graph.main(Array())
      println("generated " + graph.name)
    }
}
