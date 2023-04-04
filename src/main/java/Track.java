/**
 * Represents a point in space and time, recorded by a GPS sensor.
 *
 * @author Wang Biliu
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

public class Track {
  // TODO: Create a stub for the constructor
	public Track(){
		points = new ArrayList<>();
	}
	
	private List<Point> points;

    public Track(String filename) throws IOException, GPSException {
        this.points = new ArrayList<>();
        this.readFile(filename);
    }
  // TODO: Create a stub for readFile()
    public void readFile(String filename) throws IOException, GPSException {
    	points.clear();
        Scanner scanner = new Scanner(new File(filename));
        scanner.nextLine(); // skip header line
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            if (parts.length != 4) {
                throw new GPSException("Invalid number of values in line: " + line);
            }
            // Add null/empty check for each field
            if (parts[0] == null || parts[0].isEmpty()) {
                throw new GPSException("Timestamp is null: " + line);
            }
            if (parts[1] == null || parts[1].isEmpty()) {
                throw new GPSException("Latitude is null: " + line);
            }
            if (parts[2] == null || parts[2].isEmpty()) {
                throw new GPSException("Longitude is null: " + line);
            }
            if (parts[3] == null || parts[3].isEmpty()) {
                throw new GPSException("Elevation is null: " + line);
            }
            ZonedDateTime timestamp = ZonedDateTime.parse(parts[0]);
            double longitude = Double.parseDouble(parts[1]);
            double latitude = Double.parseDouble(parts[2]);
            double elevation = Double.parseDouble(parts[3]);
            Point point = new Point(timestamp, longitude, latitude, elevation);
            this.add(point);
        }

        scanner.close();
    }
  // TODO: Create a stub for add()
    public void add(Point point) {
        this.points.add(point);
    }
  // TODO: Create a stub for get()
    public Point get(int index) throws GPSException {
        if (index < 0 || index >= this.size()) {
            throw new GPSException("Index out of bounds");
        }
        return this.points.get(index);
    }
  // TODO: Create a stub for size()
    public int size() {
        return this.points.size();
    }
  // TODO: Create a stub for lowestPoint()
    public Point lowestPoint() throws GPSException {
        if (points.size() < 1) {
            throw new GPSException("Not enough points in the track.");
        }
        double lowest = 10000.0;
        Point lowele = null;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getElevation() < lowest) {
                lowele = points.get(i);
                lowest = points.get(i).getElevation();
            };
        }
        return lowele;
    }

  // TODO: Create a stub for highestPoint()
    public Point highestPoint() throws GPSException {
        if (points.size() < 1) {
            throw new GPSException("Not enough points in the track.");
        }
        double highest = -10000.0;
        Point highele = null;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getElevation() > highest) {
            	highele = points.get(i);
                highest = points.get(i).getElevation();
            };
        }
        return highele;
    }
    
  // TODO: Create a stub for totalDistance()
    public double totalDistance() throws GPSException {
        if (points.size() < 2) {
            throw new GPSException("Not enough points in the track.");
        }
        double distance = 0.0;
        for (int i = 1; i < points.size(); i++) {
        	distance += Point.greatCircleDistance(points.get(i-1), points.get(i));
        }
        return distance;
    }
    
  // TODO: Create a stub for averageSpeed()
    public double averageSpeed() throws GPSException {
        if (points.size() < 2) {
            throw new GPSException("Not enough points in the track.");
        }
        double distance = totalDistance();
        ZonedDateTime startTime = points.get(0).getTime();
        ZonedDateTime endTime = points.get(points.size()-1).getTime();
        long timeInterval = ChronoUnit.SECONDS.between(startTime, endTime);
        if (timeInterval == 0) {
            throw new GPSException("Time interval cannot be zero.");
        }
        return distance / (double)timeInterval;
    }

    public void writeKML(String filename) throws IOException {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
            writer.write("<Document>\n");
            writer.write("<name>" + filename + "</name>\n");

            for (int i = 0; i < this.points.size(); i++) {
                Point point = this.points.get(i);
                ZonedDateTime time = point.getTime();
                double lat = point.getLatitude();
                double lon = point.getLongitude();
                double ele = point.getElevation();

                writer.write("<Placemark>\n");
                writer.write("<name>" + time.format(dateFormatter) + " " + time.format(timeFormatter) + "</name>\n");
                writer.write("<description>Elevation: " + ele + "</description>\n");
                writer.write("<Point>\n");
                writer.write("<coordinates>" + lon + "," + lat + "," + ele + "</coordinates>\n");
                writer.write("</Point>\n");
                writer.write("</Placemark>\n");
            }

            writer.write("</Document>\n");
            writer.write("</kml>\n");
        }
    }
}