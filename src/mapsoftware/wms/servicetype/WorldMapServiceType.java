package mapsoftware.wms.servicetype;

public class WorldMapServiceType implements WMServiceType {

    private static final String GET_MAP_STATIC_PART = "http://www2.demis.nl/WMS/wms.asp?WMS=WorldMap&WMTVER=1.0.0&request=getmap&srs=EPSG:4326&&width=700&height=500&format=image/png&styles=&";
    private static final String GET_CAP_STATIC_ADDRESS = "http://www2.demis.nl/WMS/wms.asp?WMS=WorldMap&WMTVER=1.0.0&request=capabilities";

    @Override
    public String getCapabilitiesStaticURL() {
        return GET_CAP_STATIC_ADDRESS;
    }

    @Override
    public String getMapStaticURL() {
        return GET_MAP_STATIC_PART;
    }

}
