package mapsoftware.wms;

import java.io.InputStream;
import java.util.List;

import mapsoftware.wms.domain.LayerInformation;
import mapsoftware.wms.domain.LocationInformation;

public interface WMSCapabilitiesParser {
    // TODO: parseDocument parses only document and return nothing, getLayers
    // gives LayerInformation and getLocationBoundaries gives
    // LocationInformation
    /**
     * <p>
     * Returns a List containing LayerInformation fetched from the InputStream.
     * </p>
     * 
     * @param wmsXMLDocument
     *            InputStream containing getCapabilities request xml
     * @return List containing LayerInformation.
     * 
     */
    void parseDocument(InputStream wmsXMLDocument);

    List<LayerInformation> getLayerInformation();

    LocationInformation getLocationBoundaries();
}
