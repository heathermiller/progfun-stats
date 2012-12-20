package progfun

import java.io.File
import scala.io.Source

/* Required data input files:
 * dat/countries.dat, dat/allCountries.tsv, dat/populationByIso3.tsv
 *
 * Output files:
 * html/worldmap-density.js, html/worldmap-density-count.js,
 * html/worldmap-density-pop.js
 */
abstract class WorldMapFactory extends GraphFactory with Utilities {
  import CourseraData._

  // a list of iso codes and a list of and corresponding country names
  private def countriesAndIsos(file: String): (List[String], List[String]) = {
    val lines = Source.fromFile(file).getLines.toList
    lines.map { line =>
      val a = line.split("""\|""")
      (a(0), a(1))
    }.unzip
  }

  val (fullIsos, fullCountries) = countriesAndIsos("dat/countries.dat")
  val countriesMap = fullCountries zip fullIsos toMap

  // a sorted list of pairs of countries and frequencies, sorted in descending order by frequency
  val countryCount = {
    val xs = getFreqs(countries)
    xs.sortBy(_._2)
      .reverse
      .map { case (country, freq) => (country, countriesMap(country), freq) }
  }

  // for the jvectormap world map
  val countryCountWithZeros = {
    val uninvolved = fullIsos.filter {
      iso => !countryCount.exists { case (country, iso2, count) => iso == iso2 }
    }.map(iso => ("", iso, 0))
    (countryCount ++ uninvolved).sortBy(_._2)
  }

  // make a map of ISO vs ISO3 codes, for population lookup
  val lines2 = Source.fromFile("dat/allCountries.tsv").getLines.toList
  val isos = lines2.map(line => line.take(2))
  val iso3s = lines2.map(line => line.take(7).drop(4))
  val isoMap = Map((iso3s zip isos): _*)

  // read in population data and do lookup
  val lines3 = Source.fromFile("dat/populationByIso3.tsv").getLines.toList
  val popName = getColumn(0, lines3)
  val popCode = getColumn(1, lines3).map(code => isoMap getOrElse (code, "N/A"))
  val pop2011 = getColumn(2, lines3).map(_ toLong)
  val popMap = Map((popCode zip pop2011): _*)

  // takes a list of pairs of country ISO and population and writes it to a file
  def worldMapToJs(data: List[(String, Any)], total: Int, varname: String, outputLoc: String) = {
    printToFile(new File(outputLoc)) { p =>
      val objArray = ("var "+varname+" = {" :: data.map { case (iso, count) => "\""+iso+"\": "+count+"," })
      val noComma = objArray.last.replace(",", "")
      val out = objArray.dropRight(1) ++ List(noComma) ++ List("};\nvar tot ="+total+";")
      out.foreach(p.println)
    }
  }
  val isosCounts: List[(String, Int)] =
    countryCountWithZeros.map { case (country, iso, count) => (iso, count) }
  worldMapToJs(isosCounts, countries.length, "studentData", "dat/worldmap-counts.js")

  val (densCode, densPop) = countryCountWithZeros.map { case (country, iso, count) =>
    val pop = popMap getOrElse (iso, 1L)
    (iso, pop)
  }.unzip
  val normalize = popMap("US")
  val (densCount, dens) = countryCountWithZeros.map { case (country, iso, count) =>
    val pop = popMap getOrElse (iso, 1L)
    if (pop == 1) (count, 0)
    else (count, count / pop.toDouble)
  }.unzip

  // output to directory "html"
  /* Required files: jquery-jvectormap, ../dat/worldmap.js,
   *                 resources/javascript/vectormap.js
   */
  def writeHtml(): Unit = {
    worldMapToJs(densCode zip dens, total, "density", "html/worldmap-density.js")
    worldMapToJs(densCode zip densCount, total, "count", "html/worldmap-density-count.js")
    worldMapToJs(densCode zip densPop, total, "population", "html/worldmap-density-pop.js")

    val html =
      <html>
	     <head>
          <title>World Map</title>
          <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
	        <script src="resources/javascript/jquery-jvectormap-1.1.1.min.js"></script>
          <script src="../dat/worldmap.js"></script>
          <script src="worldmap-density-count.js"></script>
          <script src="worldmap-density-pop.js"></script>
          <script src="worldmap-density.js"></script>
          <script src="http://d3js.org/d3.v3.min.js"></script>
          <link href="resources/stylesheets/jquery-jvectormap-1.1.1.css" rel="stylesheet" type="text/css" />
        </head>
        <body>
          <div id="map"></div>
          <script src="resources/javascript/vectormap.js" type="text/javascript"></script>
        </body>
      </html>
    printToFile(new File(new File("html"), name))(writer => writer.println(html.toString))
  }

}
