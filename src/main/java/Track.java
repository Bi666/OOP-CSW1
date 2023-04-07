/*
  Represents a point in space and time, recorded by a GPS sensor.

  @author Wang Biliu
 */
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.temporal.ChronoUnit;

public class Track {
	public Track(){
		points = new ArrayList<>();
	}
	
	private List<Point> points;

    public Track(String filename) throws IOException, GPSException {
        this.points = new ArrayList<>();
        this.readFile(filename);
    }

    public void readFile(String filename) throws IOException, GPSException {
    	points.clear();
    	// Opening the file
        Scanner scanner = new Scanner(new File(filename));
        // skip header line
        scanner.nextLine();
        // Reading file data
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            // Determine the number of data is complete
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

    public void add(Point point) {
        this.points.add(point);
    }

    public Point get(int index) throws GPSException {
        if (index < 0 || index >= this.size()) {
            throw new GPSException("Index out of bounds");
        }
        return this.points.get(index);
    }

    public int size() {
        return this.points.size();
    }

    public Point lowestPoint() throws GPSException {
        if (points.size() < 1) {
            throw new GPSException("Not enough points in the track.");
        }
        double lowest = 10000.0;
        Point lowele = null;
        for (Point point : points) {
            if (point.getElevation() < lowest) {
                lowele = point;
                lowest = point.getElevation();
            }
        }
        return lowele;
    }

    public Point highestPoint() throws GPSException {
        if (points.size() < 1) {
            throw new GPSException("Not enough points in the track.");
        }
        double highest = -10000.0;
        Point highele = null;
        for (Point point : points) {
            if (point.getElevation() > highest) {
                highele = point;
                highest = point.getElevation();
            }
        }
        return highele;
    }

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

    public double averageSpeed() throws GPSException {
        if (points.size() < 2) {
            throw new GPSException("Not enough points in the track.");
        }
        double distance = totalDistance();
        // Get the start and end time
        ZonedDateTime startTime = points.get(0).getTime();
        ZonedDateTime endTime = points.get(points.size()-1).getTime();
        long timeInterval = ChronoUnit.SECONDS.between(startTime, endTime);
        if (timeInterval == 0) {
            throw new GPSException("Time interval cannot be zero.");
        }
        return distance / (double)timeInterval;
    }
}