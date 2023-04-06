/*
  Launcher for application to plot elevations of a GPS track, needed
  for the Advanced task of COMP1721 Coursework 1.

  @author Wang Biliu
 */
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PlotApplication extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage stage) throws IOException, GPSException {
    Parameters params = getParameters();
    List<String> args = params.getRaw();
    if (args.size() < 1) {
      System.out.println("Usage: java PlotApplication <filename>.");
      System.exit(0);
    }
    String filename = args.get(0);
    try {
      Track track = null;
      try {
        track = new Track(filename);
      } catch (IOException e) {
        System.err.println("Bad filename");
        e.printStackTrace();
        System.exit(1);
      }

      // Create x and y axes
      NumberAxis xAxis = new NumberAxis();
      NumberAxis yAxis = new NumberAxis();
      xAxis.setLabel("Distance (m)");
      yAxis.setLabel("Elevation (m)");

      // Set tick units
      xAxis.setTickUnit(100);
      yAxis.setTickUnit(5);

      // Create line chart
      LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
      lineChart.setTitle("Elevation Plot");
      lineChart.setCreateSymbols(false);

      // Add data to chart
      XYChart.Series<Number, Number> series = new XYChart.Series<>();
      series.setName(filename.substring(filename.lastIndexOf("/") + 1));
      double distance = 0.0;
      for (int i = 0; i < track.size(); i++) {
        Point point = track.get(i);
        double elevation = point.getElevation();
        series.getData().add(new XYChart.Data<>(distance, elevation));

        if (i < track.size() - 1) {
          Point nextPoint = track.get(i + 1);
          distance += Point.greatCircleDistance(point, nextPoint);
        }
      }
      ObservableList<XYChart.Series<Number, Number>> data = lineChart.getData();
      data.add(series);

      // Display chart
      Scene scene = new Scene(lineChart, 2000, 75);
      stage.setScene(scene);
      stage.show();
    }
    catch (GPSException e) {
      System.err.println("Error: " + e.getMessage());
      System.exit(2);
    }
  }
}
