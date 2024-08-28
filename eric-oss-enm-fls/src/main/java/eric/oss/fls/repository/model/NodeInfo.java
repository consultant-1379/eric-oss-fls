package eric.oss.fls.repository.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * @author xjaimah
 *
 */
@Entity
public class NodeInfo {

	/**
	 * The id
	 */
	@Id 
    @SequenceGenerator(name = "mySeqGen", sequenceName = "mySeqGen", initialValue = 1)
    @GeneratedValue(generator = "mySeqGen")
	private long id;
	
	/**
	 * The nodeName
	 */
	private String nodeName;
	/**
	 * The nodeType
	 */
	private String nodeType;
	/**
	 * The fileLocation
	 */
	
	@Column(length = 1012)
	private String fileLocation;
	/**
	 * The fileCreationTimeInOss
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date  fileCreationTimeInOss;
	/**
	 * The dataType
	 */
	private String dataType;
	/**
	 * The fileType
	 */
	private String fileType;
	/**
	 * The startRopTimeInOss
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date startRopTimeInOss;
	/**
	 * The endRopTimeInOss
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date endRopTimeInOss;
	/**
	 * The fileSize
	 */
	private long fileSize;
	
	
	public NodeInfo() {

	}

	/**
	 * @param nodeName
	 * @param nodeType
	 * @param fileLocation
	 * @param fileCreationTimeInOss
	 * @param dataType
	 * @param fileType
	 * @param startRopTimeInOss
	 * @param endRopTimeInOss
	 * @param fileSize
	 */
	public NodeInfo(String nodeName, String nodeType, String fileLocation, Date fileCreationTimeInOss,
			String dataType, String fileType, Date startRopTimeInOss, Date endRopTimeInOss, long fileSize) {
		this.nodeName = nodeName;
		this.nodeType = nodeType;
		this.fileLocation = fileLocation;
		this.fileCreationTimeInOss = fileCreationTimeInOss;
		this.dataType = dataType;
		this.fileType = fileType;
		this.startRopTimeInOss = startRopTimeInOss;
		this.endRopTimeInOss = endRopTimeInOss;
		this.fileSize = fileSize;
	}

	/**
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @param nodeName
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * @return
	 */
	public String getNodeType() {
		return nodeType;
	}

	/**
	 * @param nodeType
	 */
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	/**
	 * The hashCode
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.nodeName, this.nodeType, this.fileLocation, this.startRopTimeInOss,
				this.endRopTimeInOss);
	}

	/**
	 * The equals
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeInfo other = (NodeInfo) obj;
		if (id != other.id)
			return false;
		if (fileLocation == null) {
			if (other.fileLocation != null)
				return false;
		} else if (!fileLocation.equals(other.fileLocation))
			return false;
		if (nodeName == null) {
			if (other.nodeName != null)
				return false;
		} else if (!nodeName.equals(other.nodeName))
			return false;
		if (nodeType == null) {
			if (other.nodeType != null)
				return false;
		} else if (!nodeType.equals(other.nodeType))
			return false;
		if (startRopTimeInOss == null) {
			if (other.startRopTimeInOss != null)
				return false;
		} else if (!startRopTimeInOss.equals(other.startRopTimeInOss))
			return false;
		if (endRopTimeInOss == null) {
			if (other.endRopTimeInOss != null)
				return false;
		} else if (!endRopTimeInOss.equals(other.endRopTimeInOss))
			return false;
		return true;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public Date getFileCreationTimeInOss() {
		return fileCreationTimeInOss;
	}

	public void setFileCreationTimeInOss(Date fileCreationTimeInOss) {
		this.fileCreationTimeInOss = fileCreationTimeInOss;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Date getStartRopTimeInOss() {
		return startRopTimeInOss;
	}

	public void setStartRopTimeInOss(Date startRopTimeInOss) {
		this.startRopTimeInOss = startRopTimeInOss;
	}

	public Date getEndRopTimeInOss() {
		return endRopTimeInOss;
	}

	public void setEndRopTimeInOss(Date endRopTimeInOss) {
		this.endRopTimeInOss = endRopTimeInOss;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	
	@Override
	public String toString() {
		return "NodeInfo{" + "id:" + this.id + ", nodeName:'" + this.nodeName + '\'' + ", nodeType:'" + this.nodeType
				+ '\'' + ", fileLocation:'" + this.fileLocation + '\'' + ", dataType:'" + this.dataType + '\'' + '}';
	}

}
