package mapsoftware.wms;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import mapsoftware.wms.domain.LayerInformation;
import mapsoftware.wms.domain.LocationInformation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

/**
 * <p>
 * Parses layer information from xml data
 * </p>
 * 
 * @author Ville Ahti
 * 
 */
class GenericWMSCapabilitiesParser implements WMSCapabilitiesParser {

    public GenericWMSCapabilitiesParser() {
        this.wmsXmlDocument = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * mapsoftware.wms.WMSCapabilitiesParser#parseDocument(java.io.InputStream)
     */
    @Override
    public void parseDocument(InputStream xmlDocument) {
        if (wmsXmlDocument == null) {
            Tidy tidy = new Tidy();
            tidy.setXmlTags(true); // Defines that doc contains XML
            wmsXmlDocument = tidy.parseDOM(xmlDocument, null);
        }
    }

    @Override
    public List<LayerInformation> getLayerInformation() {
        if (wmsXmlDocument == null) {
            System.err.print("parseDocument first");
        }
        if (layerInformation == null) {
            parseLayers();
        }
        return layerInformation;
    }

    @Override
    public LocationInformation getLocationBoundaries() {
        if (wmsXmlDocument == null) {
            System.err.print("parseDocument first");
        }
        if (locationInformation == null) {
            parseLocationInfo();
        }
        return locationInformation;
    }

    private void parseLocationInfo() {
        double minX, minY, maxX, maxY;
        NodeList nl = wmsXmlDocument.getElementsByTagName(LOCATION_TAG);
        Node location = nl.item(0);
        NamedNodeMap locAttr = location.getAttributes();
        minX = Double.parseDouble(locAttr.getNamedItem("minx").getNodeValue());
        minY = Double.parseDouble(locAttr.getNamedItem("miny").getNodeValue());
        maxX = Double.parseDouble(locAttr.getNamedItem("maxx").getNodeValue());
        maxY = Double.parseDouble(locAttr.getNamedItem("maxy").getNodeValue());
        locationInformation = new LocationInformation(minX, minY, maxX, maxY);
        System.out.println(locationInformation);
    }

    private void parseLayers() {
        layerInformation = new ArrayList<LayerInformation>();
        NodeList nl = wmsXmlDocument.getElementsByTagName(LAYER_TAG);
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            Element el = (Element) node;
            NodeList layerNodes = el.getChildNodes();
            Node name = layerNodes.item(0);
            Node title = layerNodes.item(1);
            // TODO: Dummy block
            if (name == null || title == null || name.getFirstChild() == null
                    || title.getFirstChild() == null) {
            } else {
                // space in the parameter fix
                LayerInformation info = new LayerInformation(name
                        .getFirstChild().getNodeValue().replace(" ", "+"),
                        title.getFirstChild().getNodeValue());

                layerInformation.add(info);
            }

        }
    }

    private static final String LAYER_TAG = "Layer";
    private static final String LOCATION_TAG = "LatLonBoundingBox";

    private List<LayerInformation> layerInformation;
    private LocationInformation locationInformation;
    private Document wmsXmlDocument;

}
