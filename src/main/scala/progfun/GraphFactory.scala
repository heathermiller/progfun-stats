package progfun

import java.io.File
import scala.io.Source

trait GraphFactory {
  /* Name of HTML file containing generated graph */
  val name: String

  def writeHtml(): Unit
}

abstract class SimpleBarGraphFactory extends GraphFactory with Utilities {
  /** Label for y-axis */
  def label: String

  // Defaults
  val div = "#graph"
  val width = 640
  val height = 480
  val maxy: Any = "undefined"
  val color = "steelblue"
  val margin = "{top: 20, right: 20, bottom: 80, left: 40}"

  /** Data to be displayed in the bar graph */
  def data: List[(String, AnyVal)]

  protected def csvData: String = {
    val lines = data map { case (name, value) =>
      name + "," + value
    }
    "el1,el2\n" + lines.mkString("\n")
  }

  // output to directory "html"
  def writeHtml(): Unit = {
    val html =
      <html>
        <head>
          <link rel="stylesheet" href="resources/stylesheets/bootstrap.css" type="text/css" />
          <link rel="stylesheet" href="resources/stylesheets/base.css" type="text/css" />
        </head>
        <body>
          <div id="graph">&nbsp;</div>
          <script id="csv" type="text/csv">{ csvData }</script>
          <script src="resources/javascript/jquery.js"></script>
          <script src="resources/javascript/bootstrap-twipsy.js"></script>
          <script src="resources/javascript/d3.js"></script>
          <script src="resources/javascript/simplebargraph.js"></script>
          <script type="text/javascript">
            simpleBarGraphTiltedLabels('{ div }', '{ label }', '{ width }', '{ height }', { maxy }, '{ color }', { margin })
          </script>
        </body>
      </html>
    printToFile(new File(new File("html"), name))(writer => writer.println(html.toString))
  }
}

abstract class GroupedBarGraphFactory extends GraphFactory with Utilities {
  /** label for the y-axis */
  def label: String

  // Defaults
  val div = "#graph"
  val width = 960
  val height = 480
  val colorArray = "['#98abc5', '#8a89a6', '#7b6888', '#6b486b', '#a05d56', '#d0743c', '#ff8c00']"
  val margin = "{top: 20, right: 20, bottom: 30, left: 40}"

  /** Graph Legend */
  val captions: List[String]

  /** Data to be displayed in the grouped bar graph */
  def data: List[(String, List[AnyVal])]

  protected def csvData: String = {
    val lines = data map { case (group, values) =>
      group + "," + values.mkString(",")
    }
    captions.mkString(",") + "\n" + lines.mkString("\n")
  }

  // output to directory "html"
  def writeHtml(): Unit = {
    val html =
      <html>
    	<head>
    	  <link rel="stylesheet" href="resources/stylesheets/bootstrap.css" type="text/css" />
    	  <link rel="stylesheet" href="resources/stylesheets/base.css" type="text/css" />
    	</head>
      <body>
        <div id="graph">&nbsp;</div>
        <script id="csv" type="text/csv">{ csvData }</script>
        <script src="resources/javascript/jquery.js"></script>
        <script src="resources/javascript/bootstrap-twipsy.js"></script>
        <script src="resources/javascript/d3.js"></script>
        <script src="resources/javascript/groupedbargraph.js"></script>
        <script type="text/javascript">
          groupedBarGraph('{ div }', '{ label }', '{ width }', '{ height }', { colorArray }, { margin })
        </script>
        </body>
      </html>
    printToFile(new File(new File("html"), name))(writer => writer.println(html.toString))
  }
}

