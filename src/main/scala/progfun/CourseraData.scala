package progfun

import scala.io.Source

object CourseraData extends Utilities {

  private val lines = Source.fromFile("dat/progfun.tsv").getLines.toList

  val users: List[User] = lines.tail.map { line =>
    val xs = line.split("\t").toList
    User(
      xs(1),           // age
      xs(23),          // country
      xs(2),           // degree, can be "other" & optionally filled in by user
      xs(3),           // field of study, can be "other" & optionally filled in by user
      xs(4) == "Yes",  // finishedCourse?
      xs(5),           // years programming
      xs(6),           // time spent per week working on course
      xs(7).toInt,     // difficulty of course
      xs(21).toInt,    // difficulty of homeworks (programming assignments)
      xs(8).toInt,     // was the course worth it?
      xs(9).toInt,     // level of interest in a follow-up course?
      xs(19),          // what interested in you in taking the course?
      xs(20),          // where do you plan to apply what you have learned?
      xs(10),          // what is your preferred editor in general? can be "other"
      xs(11),          // what editor did you use in the course? can be "other"
      xs(12),          // experience with Java?
      xs(13),          // experience with C/C++/Objective-C?
      xs(14),          // experience with Python or other scripting language?
      xs(15),          // experience with .NET?
      xs(17),          // experience with JavaScript?
      xs(18),          // experience with purely functional languages?
      xs(22)           // experience with Lisp/Scheme dialects?
    )
  }

  val total = users.length

  val didntFinish = users.filter(user => !user.finishedCourse)

  val ages              = users.map(_.age)
  val countries         = users.map(_.country)
  val degrees           = users.map(_.degree)
  val fields            = users.map(_.field)
  val finishedCourse    = users.map(_.finishedCourse)
  val yearsProg         = users.map(_.yearsProg)
  val timeSpent         = users.map(_.timeSpent)
  val difficulty        = users.map(_.difficulty)
  val difficultyHW      = users.map(_.difficultyHW)
  val worthIt           = users.map(_.worthIt)
  val followupCourse    = users.map(_.followupCourse)
  val whatInterestedYou = users.map(_.whatInterestedYou)
  val whereApply        = users.map(_.whereApply)
  val prefEditor        = users.map(_.prefEditor)
  val courseEditor      = users.map(_.courseEditor)
  val javaExp           = users.map(_.javaExp)
  val cExp              = users.map(_.cExp)
  val pythonExp         = users.map(_.pythonExp)
  val dotNetExp         = users.map(_.dotNetExp)
  val jsExp             = users.map(_.jsExp)
  val funcExp           = users.map(_.funcExp)
  val lispExp           = users.map(_.lispExp)

  def byCourseEditor(users: List[User] = users): Map[String, List[User]] = users.groupBy(_.courseEditor.toLowerCase)
  def byPrefEditor(users: List[User] = users): Map[String, List[User]] = users.groupBy(_.prefEditor.toLowerCase)
  def byCountry(users: List[User] = users) = users.groupBy(_.country)

}
