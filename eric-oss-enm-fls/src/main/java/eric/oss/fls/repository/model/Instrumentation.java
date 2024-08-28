package eric.oss.fls.repository.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "instrumentation")
public class Instrumentation {

	@Id
	@GeneratedValue
	private long id;
	
	/**
	 * The ropTime
	 */
	private String ropTime;
	
	/**
	 * The configuredNumberOfSim
	 */
	private int configuredNumberOfSim;
	
	/**
	 * The cell count
	 */
	private int cellCount;
	
	/**
	 * The number of Nodes
	 */
	private int numberOfNodes;
	
	/**
	 * Node Type
	 */
	private String nodeType;
	
	/**
	 * Number of measurement objecy
	 */
	private int moCounts;
	
	/**
	 * The number of events
	 */
	private int numberOfEvents;
	
	/**
	 * The fileSize
	 */
	private int fileSizeInKb;
	
	/**
	 * The Node release
	 */
	private String nodeRelease;
	
	/**
	 * The file count
	 */
	private int fileCount;
	
	private long epochDate;

	public Instrumentation(String ropTime, int configuredNumberOfSim, int cellCount, int numberOfNodes, String nodeType,
			int moCounts, int numberOfEvents, int fileSizeInKb, String nodeRelease, int fileCount, long epochDate) {
		this.ropTime = ropTime;
		this.configuredNumberOfSim = configuredNumberOfSim;
		this.cellCount = cellCount;
		this.numberOfNodes = numberOfNodes;
		this.nodeType = nodeType;
		this.moCounts = moCounts;
		this.numberOfEvents = numberOfEvents;
		this.fileSizeInKb = fileSizeInKb;
		this.nodeRelease = nodeRelease;
		this.fileCount = fileCount;
		this.epochDate = epochDate;
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the ropTime
	 */
	public String getRopTime() {
		return ropTime;
	}

	/**
	 * @param ropTime the ropTime to set
	 */
	public void setRopTime(String ropTime) {
		this.ropTime = ropTime;
	}

	/**
	 * @return the configuredNumberOfSim
	 */
	public int getConfiguredNumberOfSim() {
		return configuredNumberOfSim;
	}

	/**
	 * @param configuredNumberOfSim the configuredNumberOfSim to set
	 */
	public void setConfiguredNumberOfSim(int configuredNumberOfSim) {
		this.configuredNumberOfSim = configuredNumberOfSim;
	}

	/**
	 * @return the cellCount
	 */
	public int getCellCount() {
		return cellCount;
	}

	/**
	 * @param cellCount the cellCount to set
	 */
	public void setCellCount(int cellCount) {
		this.cellCount = cellCount;
	}

	/**
	 * @return the numberOfNodes
	 */
	public int getNumberOfNodes() {
		return numberOfNodes;
	}

	/**
	 * @param numberOfNodes the numberOfNodes to set
	 */
	public void setNumberOfNodes(int numberOfNodes) {
		this.numberOfNodes = numberOfNodes;
	}

	/**
	 * @return the nodeType
	 */
	public String getNodeType() {
		return nodeType;
	}

	/**
	 * @param nodeType the nodeType to set
	 */
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}


	/**
	 * @return the moCounts
	 */
	public int getMoCounts() {
		return moCounts;
	}

	/**
	 * @param moCounts the moCounts to set
	 */
	public void setMoCounts(int moCounts) {
		this.moCounts = moCounts;
	}

	/**
	 * @return the numberOfEvents
	 */
	public int getNumberOfEvents() {
		return numberOfEvents;
	}

	/**
	 * @param numberOfEvents the numberOfEvents to set
	 */
	public void setNumberOfEvents(int numberOfEvents) {
		this.numberOfEvents = numberOfEvents;
	}

	/**
	 * @return the fileSizeInKb
	 */
	public int getFileSizeInKb() {
		return fileSizeInKb;
	}

	/**
	 * @param fileSizeInKb the fileSizeInKb to set
	 */
	public void setFileSizeInKb(int fileSizeInKb) {
		this.fileSizeInKb = fileSizeInKb;
	}

	/**
	 * @return the nodeRelease
	 */
	public String getNodeRelease() {
		return nodeRelease;
	}

	/**
	 * @param nodeRelease the nodeRelease to set
	 */
	public void setNodeRelease(String nodeRelease) {
		this.nodeRelease = nodeRelease;
	}

	/**
	 * @return the fileCount
	 */
	public int getFileCount() {
		return fileCount;
	}

	/**
	 * @param fileCount the fileCount to set
	 */
	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}

	public long getEpochDate() {
		return epochDate;
	}

	public void setEpochDate(long epochDate) {
		this.epochDate = epochDate;
	}
	
	
}
