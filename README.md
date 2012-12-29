# JAwesomeChart


## Description

JAwesomeChart is a simple Java library intended to provide developers with an easy to use solution for simple chart rendering.
It's focus is in ease of use and customizability (speed / low memory consumption was not a goal during development but you are welcome to fork it and play with it :) ).
Currently it can be used to create column, pie (optionally exploded), doughnut and line charts easily.

## Usage

First of all you need JAwesomeChart.jar in your classpath (see the section *How to build* below).

Next we need to create a `JAwesomeChart` object:

    JAwesomeChart ac = new JAwesomeChart(600, 400);
    
Then we provide a title (optional) and subtitle (also optional) for our chart:

    ac.setTitle("Wordwide Dekstop Browser Market Share");
    ac.setSubtitle("January 2011");
    
Next, we have to provide the chart's data. This is done using `DataSeries` objects (JAwesomeChart provides the `addSeries` method which creates them for us):

    ac.addSeries("IE", new double[]{42.93}, Color.decode("0x443769"));
    ac.addSeries("Firefox", new double[]{28.2}, Color.decode("0xFF2400"));
    ac.addSeries("Chrome", new double[]{21.08}, Color.decode("0x4A83BD"));
    ac.addSeries("Safari", new double[]{5.33}, Color.decode("0xFF8300"));
    ac.addSeries("Opera", new double[]{1.84}, Color.decode("0xFF0096"));
    ac.addSeries("Other", new double[]{0.62}, Color.decode("0x236A14"));
    
Instead of `Color.decode()` we can use `com.gmigdos.jawesomechart.util.Utilities.generateRandomColor()`.

Then we choose a renderer for our chart - let's create a pie chart:

    PieChartRenderer renderer = new PieChartRenderer();
    ac.setRenderer(renderer);

Finally, we have to render the chart. Normally, we would provide a `Graphics` object as the parameter to JAwesomechart's `draw()` method, but in order to render the chart to a PNG image file the `drawAndSaveChartAsImageFile()` convenience method is provided:

    JAwesomeChart.drawAndSaveChartAsImageFile(ac, "/home/cyberpython/temp/awesomechart/01-pie-chart.png");
    
![Pie chart](http://dl.dropbox.com/u/599926/images/01-pie-chart.png)

What if we want an exploded pie? Piece of cake:

    renderer.setExplosionOffset(10);
    JAwesomeChart.drawAndSaveChartAsImageFile(ac, "/home/cyberpython/temp/awesomechart/02-pie-chart.png");

![Exploded pie chart](http://dl.dropbox.com/u/599926/images/02-pie-chart.png)

Or maybe a doughnut chart :

    renderer.setIsDoughnut(true);
    JAwesomeChart.drawAndSaveChartAsImageFile(ac, "/home/cyberpython/temp/awesomechart/03-pie-chart.png");

![Doughnut chart](http://dl.dropbox.com/u/599926/images/03-pie-chart.png)
  
What about a column chart? Easy as well:

    ac.setRenderer(new ColumnChartRenderer());
    JAwesomeChart.drawAndSaveChartAsImageFile(ac, "/home/cyberpython/temp/awesomechart/04-column-chart.png");

![Column chart](http://dl.dropbox.com/u/599926/images/04-column-chart.png)

For line charts we have to provide arrays with multiple values (otherwise the line will be just a point):

    JAwesomeChart ac2 = new JAwesomeChart(600, 500);
    LineChartRenderer renderer = new LineChartRenderer();
    ac2.setRenderer(renderer);

    ac2.setTitle("Dekstop Browser Market Share 2011");
    renderer.setValueAxisCaption("Market share (%)");
    renderer.setLabelAxisCaption("Month");

    ac2.setLabels(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
    ac2.addSeries("IE", new double[]{46, 45.44, 45.11, 44.52, 43.87, 43.58, 42.45, 41.89, 41.66, 40.18, 40.63, 38.65}, Color.decode("0x443769"));
    ac2.addSeries("Firefox", new double[]{30.68, 30.37, 29.98, 29.67, 29.29, 28.34, 27.95, 27.49, 26.79, 26.39, 25.23, 25.27}, Color.decode("0xFF2400"));
    ac2.addSeries("Chrome", new double[]{15.68, 16.54, 17.37, 18.29, 19.36, 20.65, 22.14, 23.16, 23.61, 25, 25.69, 27.27}, Color.decode("0x4A83BD"));
    ac2.addSeries("Safari", new double[]{5.09, 5.08, 5.02, 5.04, 5.01, 5.07, 5.17, 5.19, 5.6, 5.93, 5.92, 6.08}, Color.decode("0xFF8300"));
    ac2.addSeries("Opera", new double[]{2, 2, 1.97, 1.91, 1.84, 1.74, 1.66, 1.67, 1.72, 1.81, 1.82, 1.98}, Color.decode("0xFF0096"));
    ac2.addSeries("Other", new double[]{0.55, 0.55, 0.54, 0.57, 0.63, 0.61, 0.63, 0.61, 0.62, 0.69, 0.71, 0.75}, Color.decode("0x236A14"));

    JAwesomeChart.drawAndSaveChartAsImageFile(ac2, "/home/cyberpython/temp/awesomechart/05-line-chart.png");
    
    renderer.setDrawPoints(true);
    
    JAwesomeChart.drawAndSaveChartAsImageFile(ac2, "/home/cyberpython/temp/awesomechart/06-line-chart.png");
    
    renderer.setDrawLines(false);
    
    JAwesomeChart.drawAndSaveChartAsImageFile(ac2, "/home/cyberpython/temp/awesomechart/07-line-chart.png");


![Line chart #1](http://dl.dropbox.com/u/599926/images/05-line-chart.png)
![Line chart #2](http://dl.dropbox.com/u/599926/images/06-line-chart.png)
![Line chart #3](http://dl.dropbox.com/u/599926/images/07-line-chart.png)

*Note: the screenshots were not taken using the code above, so the values displayed may not be the ones one would expect.*

## How to build

If you have Netbeans installed you have to open the project named JAwesomeChart and select "Clean and build".
If you do not have Netbeans you will need to install Apache Ant. Then open a terminal, navigate to the project's directory and give:

    ant clean jar
    
This should build a directory named `dist` and inside it you will find the library's JAR file (which is what you need in your classpath).

## License

Copyright © 2012 Georgios Migdos.
The code is available under the terms of the [MIT license](http://www.opensource.org/licenses/mit-license.php).

