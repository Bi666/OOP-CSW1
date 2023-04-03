/**
 * Represents a point in space and time, recorded by a GPS sensor.
 *
 * @author Wang Biliu
 */
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.temporal.ChronoUnit;

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
	private void readFile(String filename) throws IOException, GPSException {
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            if (parts.length != 4) {
                throw new GPSException("Invalid number of values in line: " + line);
            }
            double latitude = Double.parseDouble(parts[0]);
            double longitude = Double.parseDouble(parts[1]);
            double elevation = Double.parseDouble(parts[2]);
            ZonedDateTime timestamp = ZonedDateTime.parse(parts[3]);
            Point point = new Point(timestamp, longitude, elevation, latitude);
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
        double highest = 10000.0;
        Point highele = null;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getElevation() < highest) {
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
}
