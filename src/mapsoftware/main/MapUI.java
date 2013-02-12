package mapsoftware.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import mapsoftware.wms.WMServiceFactory;
import mapsoftware.wms.WMServiceFactory.WMServiceProvider;
import mapsoftware.wms.WMServiceStrategy;
import mapsoftware.wms.domain.LayerInformation;
import mapsoftware.wms.domain.LocationInformation;
import mapsoftware.wms.domain.LocationInformation.Operation;
import mapsoftware.wms.domain.ServiceCapabilitiesInformation;

/**
 * <p>
 * WMSMapServiceUI made with Java's Swing library
 * </p>
 * 
 * @author Ville Ahti
 * 
 */
public class MapUI extends JFrame {

    private final WMServiceStrategy wmServiceStrategy;
    private LocationInformation area;
    private List<LayerInformation> selectedLayers;

    // TODO: Move installation
    private final JLabel lblMapImage = new JLabel();
    private final JLayeredPane leftPanel = new JLayeredPane();
    private JPanel pnlCheckBox;

    private final JButton btnRefresh = new JButton("Refresh");
    private final JButton btnLeft = new JButton("<");
    private final JButton btnRight = new JButton(">");
    private final JButton btnUp = new JButton("^");
    private final JButton btnDown = new JButton("v");
    private final JButton btnZoomIn = new JButton("+");
    private final JButton btnZoomOut = new JButton("-");

    public static void main(String[] args) {
        new MapUI(WMServiceFactory.getWMService(WMServiceProvider.WORLD_MAP));
    }

    public MapUI(WMServiceStrategy wmServiceStrategy) {
        super();
        this.wmServiceStrategy = wmServiceStrategy;
        init();
    }

    /**
     * <p>
     * Inits UI
     * </p>
     * 
     */
    private void init() {
        // TODO: Edit layout: improve panel organization and whole appearance.
        // Make MenuBar
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        ServiceCapabilitiesInformation serviceInfo = wmServiceStrategy
                .getCapabilities();
        List<LayerInformation> layers = serviceInfo.getLayerInformations();
        area = serviceInfo.getLocationInformation();

        attachButtonListeners();

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        leftPanel.setMaximumSize(new Dimension(100, 600));

        pnlCheckBox = new JPanel();
        pnlCheckBox.setLayout(new BoxLayout(pnlCheckBox, BoxLayout.Y_AXIS));
        leftPanel.add(pnlCheckBox);

        for (LayerInformation layerInfo : layers) {
            pnlCheckBox.add(new LayerCheckBox(layerInfo, true));
        }

        leftPanel.add(btnRefresh);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(btnLeft);
        leftPanel.add(btnRight);
        leftPanel.add(btnUp);
        leftPanel.add(btnDown);
        leftPanel.add(btnZoomIn);
        leftPanel.add(btnZoomOut);
        
        checkSelectedLayers();
        lblMapImage.setIcon(new ImageIcon(wmServiceStrategy.getMap(
                selectedLayers, area)));

        add(lblMapImage, BorderLayout.EAST);
        add(leftPanel, BorderLayout.WEST);

        pack();
        setVisible(true);
        setResizable(false);
    }

    private void attachButtonListeners() {
        ButtonListener bl = new ButtonListener();
        btnRefresh.addActionListener(bl);
        btnLeft.addActionListener(bl);
        btnRight.addActionListener(bl);
        btnUp.addActionListener(bl);
        btnDown.addActionListener(bl);
        btnZoomIn.addActionListener(bl);
        btnZoomOut.addActionListener(bl);
    }

    /**
     * <p>
     * ButtonListener for all UI components
     * </p>
     * 
     */
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnRefresh) {
                updateMapImage();
            }
            if (e.getSource() == btnLeft) {
                area.move(Operation.LEFT);
                updateMapImage();
            }
            if (e.getSource() == btnRight) {
                area.move(Operation.RIGHT);
                updateMapImage();
            }
            if (e.getSource() == btnUp) {
                area.move(Operation.UP);
                updateMapImage();
            }
            if (e.getSource() == btnDown) {
                area.move(Operation.DOWN);
                updateMapImage();
            }
            if (e.getSource() == btnZoomIn) {
                area.move(Operation.ZOOM_IN);
                updateMapImage();
            }
            if (e.getSource() == btnZoomOut) {
                area.move(Operation.ZOOM_OUT);
                updateMapImage();
            }
        }
    }

    /**
     * <p>
     * CheckBox that keeps information about WMS layers
     * </p>
     * 
     * @author Ville Ahti
     * 
     */
    private class LayerCheckBox extends JCheckBox {
        private final LayerInformation layerInformation;

        public LayerCheckBox(LayerInformation layerInfo, boolean selected) {
            super(layerInfo.getTitle(), null, selected);
            this.layerInformation = layerInfo;
        }

        public LayerInformation getLayerInformation() {
            return layerInformation;
        }

    }

    /**
     * <p>
     * Updates map image
     * </p>
     */
    private void updateMapImage() {
        new Thread() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new MapUpdater());
            }
        }.start();
    }

    /**
     * @return request parameter from chosen LayerCheckbox components
     */
    // TODO: Check that at least one is checked to prevent no layers checked
    // errors with WMS (DO EVERYTHING Differently)
    private void checkSelectedLayers() {
    	if(selectedLayers == null) {
    		selectedLayers = new ArrayList<LayerInformation>();
    	}
    	selectedLayers.clear();
      
        for (Component comLayer : pnlCheckBox.getComponents()) {
            if (((LayerCheckBox) comLayer).isSelected()) {
                        selectedLayers.add(((LayerCheckBox) comLayer).getLayerInformation());
            }
        }
    }

    /**
     * <p>
     * Runnable class which updates map
     * </p>
     * 
     * @author Ville Ahti
     */
    private class MapUpdater implements Runnable {
        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            updateMap();
        }

        /**
         * <p>
         * updates map UIs image
         * </p>
         */
        private void updateMap() {
            checkSelectedLayers();
            lblMapImage.setIcon(new ImageIcon(wmServiceStrategy.getMap(selectedLayers,
                    area)));
        }

    }

}
