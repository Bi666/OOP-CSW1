/**
 * Program to general a KML file from GPS track data stored in a file,
 * for the Advanced task of COMP1721 Coursework 1.
 *
 * @author Wang Biliu
 */
public class ConvertToKML {
  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Usage: ConvertToKML <CSV file> <KML file>");
      System.exit(1);
    }
    String csvFilename = args[0];
    String kmlFilename = args[1];
    try {
      Track track = new Track(args[0]);
      track.writeKML(kmlFilename);
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      System.exit(2);
    }
  }

}
