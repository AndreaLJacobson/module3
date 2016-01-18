package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.MarkerFactory;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);

	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
	    	PointFeature f = earthquakes.get(0);
	    	System.out.println(f.getProperties());
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	// PointFeatures also have a getLocation method
	    }
	    
	    int yellow = color(255, 255, 0);
	    int blue = color(0,0,205);
	    int red = color(220,20,60);
	    
	    //TODO: Add code here as appropriate
	    
	    //***purpose: (1) Create markers for each earthquake location. 
	    //(2) make the radius of the marker 10 by modifying helper methods

	    	for(int i = 0; i < earthquakes.size(); i++)
	    	{
	    		PointFeature pf = earthquakes.get(i);
	    		SimplePointMarker smp = new SimplePointMarker(pf.getLocation());
	    		Object magObj = pf.getProperty("magnitude");
		    	float mag = Float.parseFloat(magObj.toString());
		    	
		    	//Color and size of markers are defined as such:
		    	//  Minor earthquakes (less than magnitude 4.0) will have blue markers and be small.
		        //	Light earthquakes (between 4.0-4.9) will have yellow markers and be medium.
		    	//  Moderate and higher earthquakes (5.0 and over) will have red markers and be largest.
		    	
		    	if(mag < 4.0)
		    	{
		    		smp.setColor(blue);
		    		smp.setRadius(6);
		    	}
		    	else if(mag < 5.0)
		    	{
		    		smp.setColor(yellow);
		    		smp.setRadius(8);
		    	}
		    	else
		    	{
		    		smp.setColor(red);
		    		smp.setRadius(10);
		    	}
		    
	    		markers.add(smp);
	    		System.out.println("Marker " + i+ " location: " + markers.get(i).getLocation().toString());
	    	}//end for loop creating markers
		    
	    	for(Marker marker: markers)
		    {
		    	map.addMarkers(marker);
		    }
		    	
	}//end setup
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
		// finish implementing and use this method, if it helps.
		
		return new SimplePointMarker(feature.getLocation());
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		fill(255,255,255);
		rect(30, 40, 150, 200, 7);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(14);
		text("Earthquake Key", 40, 70 );
		textSize(12);
		text("By Magnitude", 40, 85 );
		
		fill(0,0,205);
		ellipse(50, 100, 6, 6);
		fill(255, 255, 0);
		ellipse(50, 130, 8, 8);
		fill(220,20,60);
		ellipse(50, 160, 10, 10);
		
		textSize(12);
		String a = "Below 4.0";
		text(a, 75, 100);
		String b = "4.0+";
		text(b, 75, 130);
		String c = "5.0+";
		text(c, 75, 160);
		
		
	
	}
}
