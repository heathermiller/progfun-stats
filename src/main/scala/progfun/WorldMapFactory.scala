package progfun

import java.io.File
import scala.io.Source

abstract class WorldMapFactory extends GraphFactory with Utilities {
  import CourseraData._

  val name: String

  /** `data` is a `List[(String, AnyVal)] where the
   *  String is the ISO2 code representing the country, and AnyVal is the
   *  numerical data that you'd like to have plotted.
   */
  val data: List[(String, AnyVal)]

  def label: String = "Number of Students"

  val lightcolor: String = "#ffffff"
  val darkcolor: String = "#330066"

  // probably don't override these
  def varname: String = "data"
  def popoverText: String = "<br>" + label + ": <b>'+"+varname+"[iso]+'</b>'"

  val lines = Source.fromFile("dat/countries.tsv").getLines.toList

  /** List[List[String]] representing each country's ISO2, ISO3, and country name (as entered in the survey).
   *  That is, each element in the outer list contains a List(iso2, iso3, country)
   */
  val iso2Iso3Country = lines.map (line => line.split("\t").toList)

  // Any/All Maps that could possibly be necessary. All are Map[String, String]
  // e.g. iso2Country is a Map iso2 -> country
  val iso2Country = (getColumn(0, lines) zip getColumn(2, lines)).toMap
  val iso3Country = (getColumn(1, lines) zip getColumn(2, lines)).toMap
  val countryIso2 = iso2Country.map(_.swap)
  val countryIso3 = iso3Country.map(_.swap)
  val iso2Iso3    = (getColumn(0, lines) zip getColumn(1, lines)).toMap
  val iso3Iso2    = iso2Iso3.map(_.swap)
  val iso2population = {
    val lines = Source.fromFile("dat/populationByIso3.tsv").getLines.toList
    val popCode = getColumn(1, lines).map(code => iso3Iso2 getOrElse (code, "N/A")) // iso2, List[String]
    val pop2011 = getColumn(2, lines).map(_ toLong) // populations, List[Long]
    Map((popCode zip pop2011): _*)
  }

  def data2jsArray(data: List[(String, AnyVal)]): String = {
      val objArray = "var data = {" :: data.map { case (iso, value) => "'"+iso+"': "+value+"," }
      val noComma = objArray.last.replace(",", "")
      val total = countries.length
      (objArray.dropRight(1) ++ List(noComma) ++ List("};\nvar tot ="+total+";")).mkString("")
  }

  def jvectormapParams: String =
    "\n" +
    "$('#map').vectorMap({\n" +
    "  map: 'world_mill_en',\n" +
    "  regionStyle: {\n" +
    "    hover: {\n" +
    "      'fill-opacity': 0.6\n" +
    "    }\n" +
    "  },\n" +
    "  series: {\n" +
    "    regions: [{\n" +
    "     values: "+varname+",\n" +
    "     scale: ['"+lightcolor+"', '"+darkcolor+"'],\n" +
    "     normalizeFunction: 'polynomial',\n" +
    "   }]\n" +
    " },\n" +
    "  onRegionLabelShow: function(e, el, iso){\n" +
    "   el.html('<b>'+el.html()+'</b>"+popoverText+");\n" +
    " }\n" +
    "});"

  // output to directory "html"all
  /* Required files: jquery-jvectormap, ../dat/worldmap.js,
   *                 resources/javascript/vectormap.js
   */
  def writeHtml(): Unit = {
    // worldMapToJs(densCode zip dens, total, "density", "html/worldmap-density.js")
    // worldMapToJs(densCode zip densCount, total, "count", "html/worldmap-density-count.js")
    // worldMapToJs(densCode zip densPop, total, "population", "html/worldmap-density-pop.js")

    // <script src="worldmap-density-count.js"></script>
    // <script src="worldmap-density-pop.js"></script>
    // <script src="worldmap-density.js"></script>

    val html =
      <html>
        <head>
          <title>World Map</title>
          <script src="resources/javascript/jquery.js"></script>
          <script src="resources/javascript/jquery-jvectormap-1.1.1.min.js"></script>
          <script src="../dat/worldmap.js"></script>
          <script src="resources/javascript/d3.js"></script>
          <link href="resources/stylesheets/jquery-jvectormap-1.1.1.css" rel="stylesheet" type="text/css" />
        </head>
        <body>
          <div id="map"></div>
          <script type="text/javascript">
            { data2jsArray(data) }
            { new scala.xml.Unparsed(jvectormapParams) }
          </script>
        </body>
      </html>
    printToFile(new File(new File("html"), name))(writer => writer.println(html.toString))
  }

}
