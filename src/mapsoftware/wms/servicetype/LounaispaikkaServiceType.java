package mapsoftware.wms.servicetype;

public class LounaispaikkaServiceType implements WMServiceType {

    private static final String GET_MAP_STATIC_PART = "http://kartat.lounaispaikka.fi/wms/maakuntakaava?version=1.1.1&service=WMS&request=getmap&srs=EPSG:4326&&width=700&height=500&format=image/png&styles=&";
    private static final String GET_CAP_REQ_ADDRESS = "http://kartat.lounaispaikka.fi/wms/maakuntakaava?version=1.1.1&service=WMS&request=GetCapabilities";

    @Override
    public String getCapabilitiesStaticURL() {
        // TODO Auto-generated method stub
        return GET_CAP_REQ_ADDRESS;
    }

    @Override
    public String getMapStaticURL() {
        // TODO Auto-generated method stub
        return GET_MAP_STATIC_PART;
    }

}
