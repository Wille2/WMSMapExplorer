package mapsoftware.wms;

import java.util.HashMap;
import java.util.Map;

import mapsoftware.wms.servicetype.LounaispaikkaServiceType;
import mapsoftware.wms.servicetype.WMServiceType;
import mapsoftware.wms.servicetype.WorldMapServiceType;

public class WMServiceFactory {

    private final static WMSCapabilitiesParser parser = new GenericWMSCapabilitiesParser();
    private final static Map<WMServiceProvider, WMServiceType> serviceTypes = new HashMap<WMServiceProvider, WMServiceType>();

    static {
        serviceTypes.put(WMServiceFactory.WMServiceProvider.LOUNAISPAIKKA,
                new LounaispaikkaServiceType());
        serviceTypes
                .put(WMServiceProvider.WORLD_MAP, new WorldMapServiceType());
    }

    public static enum WMServiceProvider {
        LOUNAISPAIKKA, WORLD_MAP
    }

    public static WMServiceStrategy getWMService(
            WMServiceProvider serviceProvider) {
        return new GenericWMService(parser, serviceTypes.get(serviceProvider));
    }
}
