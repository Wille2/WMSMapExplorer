package mapsoftware.wms.domain;

/**
 * <p>Class holds WMS protocol layer information</p>
 * 
 * @author Ville Ahti
 *
 */
public class LayerInformation {
	private final String name, title;
	
	/**
	 * @param name WMS layer parameter name
	 * @param title WMS layer parameter title
	 */
	public LayerInformation(String name, String title) {
		super();
		this.name = name;
		this.title = title;
	}
	
	/**
	 * <p>Getter for name</p>
	 *
	 * @return name (parameter) of layer
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>Getter for title</p>
	 * 
	 * @return title of layer
	 */
	public String getTitle() {
		return title;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LayerInformation [name=" + name + ", title=" + title + "]";
	}
	
}
