package mapsoftware.wms.domain;

import java.util.List;

public class ServiceCapabilitiesInformation {

    public ServiceCapabilitiesInformation(
            List<LayerInformation> layerInformations,
            LocationInformation locationInformation) {
        this.layerInformations = layerInformations;
        this.locationInformation = locationInformation;
    }

    public List<LayerInformation> getLayerInformations() {
        return layerInformations;
    }

    public LocationInformation getLocationInformation() {
        return locationInformation;
    }

    private final List<LayerInformation> layerInformations;
    private final LocationInformation locationInformation;
}
