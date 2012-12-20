function simpleBarGraphTiltedLabels(div,ylabel,w,h,maxy,color,margin,yformat) {
    return simpleBarGraph(div,ylabel,w,h,maxy,color,margin,yformat,true);
}

function simpleBarGraph(div,ylabel,w,h,maxy,color,margin,yformat,tilted) {
  // div = typeof div !== 'undefined' ? div : "body";
  // w = typeof w !== 'undefined' ? w : 960;
  // h = typeof h !== 'undefined' ? w : 480;
  // ylabel = typeof ylabel !== 'undefined' ? ylabel : "";
  // //maxy's default is set below
  // color = typeof color !== 'undefined' ? color : "steelblue";
  // margin = typeof margin != 'undefined' ? margin : {top: 20, right: 20, bottom: 80, left: 40};
  yformat = typeof format !== 'undefined' ? format : d3.format(".2s"); // for example, for %ages d3.format(".0%");
  tilted = typeof tilted !== 'undefined' ? tilted : false;

  var width = w - margin.left - margin.right,
      height = h - margin.top - margin.bottom;

  var x = d3.scale.ordinal()
      .rangeRoundBands([0, width], .1);

  var y = d3.scale.linear()
      .range([height, 0]);

  var xAxis = d3.svg.axis()
      .scale(x)
      .orient("bottom");

  var yAxis = d3.svg.axis()
      .scale(y)
      .orient("left")
      .tickFormat(yformat);

  var svg = d3.select(div).append("svg")
      .attr("width", width + margin.left + margin.right)
      .attr("height", height + margin.top + margin.bottom)
    .append("g")
      .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

  var data = d3.csv.parseRows(d3.select("#csv").text(), function(row, index) {
    if (index == 0) {
              return null;
    }
    var obj = new Object();
    obj.el1 = row[0];
    obj.el2 = +row[1];
    return obj;
  });

  x.domain(data.map(function(d) { return d.el1; }));

  if (typeof maxy !== 'undefined') {
    y.domain([0, maxy]);
  } else {
    y.domain([0, d3.max(data, function(d) { return d.el2; })]);
  }

  var xax = svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis);

  if(tilted) {
    xax.selectAll("text")
    .attr("text-anchor", function(d) { return "end" })
    .attr("transform", function(d) { return "translate(-20,20)rotate(-45)"; })
    .style("font-size","11px")
    .style("font-weight","bold");
  }

  svg.append("g")
      .attr("class", "y axis")
      .call(yAxis)
    .append("text")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("dy", ".71em")
      .style("text-anchor", "end")
      .text("Percentage")
      .style("font-weight","bold");

  svg.selectAll(".bar")
      .data(data)
    .enter().append("rect")
      .attr("class", "bar")
      .attr("x", function(d) { return x(d.el1); })
      .attr("width", x.rangeBand())
      .attr("y", function(d) { return y(d.el2); })
      .attr("height", function(d) { return height - y(d.el2); })
      .style("fill", color)
      .on ("mouseover",mover)
      .on ("mouseout",mout);

  if (ylabel.toLowerCase().indexOf("percentage") != -1) { var suffix = "%"; }
  else { var suffix = ""; }

  function mover(d) {
    d3.select(this).transition().attr("height", function(d) { return height - y(d.el2) + 5; })
      .attr("y", function(d) { return y(d.el2) - 5;})
      .attr("width", x.rangeBand() + 2)
      .attr("x", function(d) { return x(d.el1) - 1; })
      .style("fill-opacity", .8);
    $(this).attr("rel","twipsy")
      .attr("data-original-title",d.el2 + suffix)
      .twipsy('show');
  }

  function mout(d) {
    d3.select(this).transition().attr("height", function(d) { return height - y(d.el2); })
      .attr("y", function(d) { return y(d.el2); })
      .attr("width", x.rangeBand())
      .attr("x", function(d) { return x(d.el1); })
      .style("fill-opacity", 1);
    $(this).twipsy('hide');
  }

  $("rect[rel=twipsy]").twipsy({ live: true, offset: 4 });

}