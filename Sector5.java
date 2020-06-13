// Ved Pradhan
// 5-3-20
// Sector5.java
// This class helps the main class to divide up the map, locate Seismology
// Centers, and locate earthquakes. Provides organization.

public class Sector5
{
  private int sectorPopulation; // How many people in the sector?
  private boolean isEvacuated; // Has the sector been evacuated?
  private boolean isDestroyed; // Has an earthquake destroyed this sector?
  private boolean hasSeismologyCenter; // Does this have a seismology center?
  private int xCoordForCenter; // The xCoordForCenter
  private int yCoordForCenter; // The yCoordForCenter
  private int distanceFromNearestEpicenter; // distance from epicenter

  // Initializes field variables
  public Sector5(int initialPopulation)
  {
    sectorPopulation = initialPopulation;
    isEvacuated = false;
    isDestroyed = false;
    hasSeismologyCenter = false;
    xCoordForCenter = -1;
    yCoordForCenter = -1;
  }

  // getter method
  public int getPopulation()
  {
    return sectorPopulation;
  }

  // setter method
  public void setPopulation(int populationIn)
  {
    sectorPopulation = populationIn;
  }

  // getter method
  public boolean getEvacuated()
  {
    return isEvacuated;
  }

  // getter method
  public boolean getDestroyed()
  {
    return isDestroyed;
  }

  // sets FVs for evacuate mode
  public void evacuate()
  {
    sectorPopulation = 0;
    isEvacuated = true;
  }

  // sets FVs for destroy mode
  public void destroy()
  {
    sectorPopulation = 0;
    isEvacuated = true;
    isDestroyed = true;
    hasSeismologyCenter = false;
    xCoordForCenter = -1;
    yCoordForCenter = -1;
  }

  // resets FVs for restoring
  public void restore()
  {
    isEvacuated = false;
    isDestroyed = false;
  }

  // Returns coordinates in form (X, Y)
  public String getCenterCoordinates()
  {
    if (!hasSeismologyCenter)
      return "()";
    else
    {
      String returnString = new String("");
      returnString = "(" + xCoordForCenter + ", "
        + yCoordForCenter + ")";
      return returnString;
    }
  }

  // Creates a new center with random coordinates
  public void newCenter(int column, int row, int mapPanelWidth,
    int mapPanelHeight)
  {
    if (!isDestroyed)
    {
      int sectorWidth = (int)(mapPanelWidth / 4.0);
      int sectorHeight = (int)(mapPanelHeight / 4.0);

      xCoordForCenter = (int)(Math.random() * sectorWidth);
      xCoordForCenter += column * sectorWidth;

      yCoordForCenter = (int)(Math.random() * sectorHeight);
      yCoordForCenter += row * sectorHeight;
      yCoordForCenter = mapPanelHeight - yCoordForCenter;
        // To convert from java to math

      hasSeismologyCenter = true;
    }
  }

  // gets distance using the distance method.
  public int getDistance(int foreshockXIn, int foreshockYIn)
  {
    if (!hasSeismologyCenter)
      return -1;
    else
    {
      int distanceFromEpicenter = -1;
      distanceFromEpicenter = (int)(Math.sqrt(((xCoordForCenter -
        foreshockXIn) * (xCoordForCenter - foreshockXIn)) + ((yCoordForCenter
        - foreshockYIn) * (yCoordForCenter - foreshockYIn))));
        // root of delta x squared + delta y squared
      return distanceFromEpicenter;
    }
  }
}
