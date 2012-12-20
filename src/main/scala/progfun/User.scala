package progfun

/** Survey responses for a single user registered for the course.
 */
case class User(
  age: String,
  country: String,
  degree: String,
  field: String,
  finishedCourse: Boolean,
  yearsProg: String,
  timeSpent: String,
  difficulty: Int,
  difficultyHW: Int,
  worthIt: Int,
  followupCourse: Int,
  whatInterestedYou: String,
  whereApply: String,
  prefEditor: String,
  courseEditor: String,
  javaExp: String,
  cExp: String,
  pythonExp: String,
  dotNetExp: String,
  jsExp: String,
  funcExp: String,
  lispExp: String
)
