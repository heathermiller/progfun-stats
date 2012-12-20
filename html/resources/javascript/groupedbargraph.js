function groupedBarGraph(div,ylabel,w,h,colorArr,margin,yformat) {
  // div = typeof div !== 'undefined' ? div : "body";
  // ylabel = typeof ylabel !== 'undefined' ? ylabel : "";
  // w = typeof w !== 'undefined' ? w : 960;
  // h = typeof h !== 'undefined' ? h : 480;
  // colorArr = typeof colorArr !== 'undefined' ? colorArr : ["#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#ff8c00"];
  // margin = typeof margin != 'undefined' ? margin : {top: 20, right: 20, bottom: 30, left: 40};
  yformat = typeof format !== 'undefined' ? format : d3.format(".2s"); // for example, for %ages d3.format(".0%");

  var width = w - margin.left - margin.right,
      height = h - margin.top - margin.bottom;

  var x0 = d3.scale.ordinal()
      .rangeRoundBands([0, width], .1);

  var x1 = d3.scale.ordinal();

  var y = d3.scale.linear()
      .range([height, 0]);

  var color = d3.scale.ordinal()
      .range(colorArr);

  var xAxis = d3.svg.axis()
      .scale(x0)
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

  var categories = new Array();

  var data = d3.csv.parseRows(d3.select("#csv").text(), function(row, index) {
    /*if (index == 0) {
      return null;
    } else*/ if (index == 0) {
      for (i = 1; i < row.length; i++) {
        categories[i] = row[i];
      }
      return null;
    }
    var obj = new Object();
    obj.Key = row[0];
    for (i = 1; i < row.length; i++) {
    	obj[categories[i]] = +row[i];
    }
    return obj;
  });

  var expLevels = d3.keys(data[0]).filter(function(key) { return key !== "Key"; });

  data.forEach(function(d) {
    d.exp = expLevels.map(function(name) { return {name: name, value: +d[name]}; });
  });

  x0.domain(data.map(function(d) { return d.Key; }));
  x1.domain(expLevels).rangeRoundBands([0, x0.rangeBand()]);
  y.domain([0, d3.max(data, function(d) { return d3.max(d.exp, function(d) { return d.value; }); })]);

  svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis);

  svg.append("g")
      .attr("class", "y axis")
      .call(yAxis)
    .append("text")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("dy", ".71em")
      .style("text-anchor", "end")
      .text(ylabel)
      .style("font-weight", "bold");

  var language = svg.selectAll(".language")
      .data(data)
    .enter().append("g")
      .attr("class", "g")
      .attr("transform", function(d) { return "translate(" + x0(d.Key) + ",0)"; })
      .style("font-size", "10px");

  language.selectAll("rect")
      .data(function(d) { return d.exp; })
    .enter().append("rect")
      .attr("width", x1.rangeBand())
      .attr("x", function(d) { return x1(d.name); })
      .attr("y", function(d) { return y(d.value); })
      .attr("height", function(d) { return height - y(d.value); })
      .style("fill", function(d) { return color(d.name); })
      .on ("mouseover",mover)
      .on ("mouseout",mout);

  if (ylabel.toLowerCase().indexOf("percentage") != -1) { var suffix = "%"; }
  else { var suffix = ""; }

  function mover(d) {
    d3.select(this).transition().attr("height", function(d) { return height - y(d.value) + 5; })
      .attr("y", function(d) { return y(d.value) - 5;})
      .attr("width", x1.rangeBand() + 2)
      .attr("x", function(d) { return x1(d.name) - 1; })
      .style("fill-opacity", .9);
    $(this).attr("rel","twipsy")
      .attr("data-original-title",d.value + suffix)
      .twipsy('show');
  }

  function mout(d) {
    d3.select(this).transition().attr("height", function(d) { return height - y(d.value); })
      .attr("y", function(d) { return y(d.value); })
      .attr("width", x1.rangeBand())
      .attr("x", function(d) { return x1(d.name); })
      .style("fill-opacity", 1);
    $(this).twipsy('hide');
  }

  $("rect[rel=twipsy]").twipsy({ live: true, offset: 4 });

  var legend = svg.selectAll(".legend")
      .data(expLevels.slice())
    .enter().append("g")
      .attr("class", "legend")
      .attr("transform", function(d, i) { return "translate(0," + i * 20 + ")"; });

  legend.append("rect")
      .attr("x", width - 18)
      .attr("width", 18)
      .attr("height", 18)
      .style("fill", color);

  legend.append("text")
      .attr("x", width - 24)
      .attr("y", 9)
      .attr("dy", ".35em")
      .style("text-anchor", "end")
      .text(function(d) { return d; });
}

