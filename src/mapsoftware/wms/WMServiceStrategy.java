package mapsoftware.wms;

import java.net.URL;
import java.util.List;

import mapsoftware.wms.domain.LayerInformation;
import mapsoftware.wms.domain.LocationInformation;
import mapsoftware.wms.domain.ServiceCapabilitiesInformation;


/**
 * <p>WMS protocol interface for doing basic WMS requests</p>
 * 
 * @author Ville Ahti
 *
 */
public interface WMServiceStrategy {
	
	 /**
	  * <p>Protocol method for getting layers and other information</p>
	  * 
	 * @return list of services map layers. 
	 */
	ServiceCapabilitiesInformation getCapabilities();
	 
	
	 /**
	  * <p>Returns a URL to requested map.</p>
	  * 
	 * @param layers gotten from getCapabilities
	 * @param locationInfo location parameters
	 * @return URL which has the requested map 
	 */
	//TODO: improve interface
	URL getMap(List<LayerInformation> layers, LocationInformation locationInfo);
}
