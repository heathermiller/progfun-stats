package progfun

import java.io._

abstract class PieChartFactory extends GraphFactory with Utilities {

  protected def divName = name.take(name.length-5)
  protected val width: Int = 500
  protected val height: Int = 240

  def data: List[(String, Int)]

  protected def writePieChartJS(): Unit = {
    val writer =
      new PrintWriter(new FileWriter(new File(new File("html"), divName + ".js")))

    writer.println("var r1 = Raphael(\"" + divName + "\", " + width + ", " + height + ");")

    val valuesString =
      data.map(_._2).mkString("[", ",", "]")

    val legendString =
      data.map(_._1).map(cat => "\"%%.%% " + cat + "\"").mkString("[", ",", "]")

    writer.println("var pc1 = r1.piechart(110, 120, 100, " + valuesString + ", { legend: " + legendString + ", legendpos: \"east\" });")

    val contents = """pc1.hover(function () {
    this.sector.stop();
    this.sector.scale(1.1, 1.1, this.cx, this.cy);

    if (this.label) {
        this.label[0].stop();
        this.label[0].attr({ r: 7.5 });
        this.label[1].attr({ "font-weight": 800 });
    }
}, function () {
    this.sector.animate({ transform: 's1 1 ' + this.cx + ' ' + this.cy }, 500, "bounce");

    if (this.label) {
        this.label[0].animate({ r: 5 }, 500, "bounce");
        this.label[1].attr({ "font-weight": 400 });
    }
});"""

    writer.println(contents)
    writer.close()
  }

  def writeHtml(): Unit = {
    writePieChartJS()
    val html =
      <html>
	      <head>
          <title>{ divName + " pie chart" }</title>
          <script src="resources/javascript/jquery.js"></script>
          <!-- <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script> -->
	        <script src="resources/javascript/raphael.js"></script>
          <script src="resources/javascript/g.raphael.js"></script>
          <script src="resources/javascript/g.pie.js"></script>
	      </head>
        <body>
          <div id={ divName }></div>
          <script src={ divName + ".js" }></script>
        </body>
      </html>
    printToFile(new File(new File("html"), name))(writer => writer.println(html.toString))
  }

}
