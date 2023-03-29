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
public class Track {
  // TODO: Create a stub for the constructor
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

  // TODO: Create a stub for highestPoint()

  // TODO: Create a stub for totalDistance()

  // TODO: Create a stub for averageSpeed()
}
