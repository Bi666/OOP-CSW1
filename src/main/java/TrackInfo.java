/**
 * Program to provide information on a GPS track stored in a file.
 *
 * @author Wang Biliu
 */
public class TrackInfo {
  public static void main(String[] args) {
    // TODO: Implementation TrackInfo application here
	  if (args.length != 1) {}
	  try {
		  Track track = new Track(args[0]);
		  System.out.printf("%d points in track%n", track.size());
		  
		  Point lowest = track.lowestPoint();
		  Point highest = track.highestPoint();
		  System.out.printf("lowest point is %s%n", lowest);
		  System.out.printf("Highest point is %s%n", highest);
		  
		  double distKm = track.totalDistance() / 1000.0;
		  System.out.printf("Total distance = %.3f km%n", distKm);
		  
		  double avgSpeed = track.averageSpeed();
		  System.out.printf("Average speed = %3f m/s%n", avgSpeed);
	  }
	  catch (Exception error) {
		  System.err.println("Error: " + error.getMessage());
		  System.exit(2);
	  }
  }
}
