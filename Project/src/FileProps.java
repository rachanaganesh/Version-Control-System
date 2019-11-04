
public class FileProps {

	private String artifactID = "";
	private String oldFileName = "";
	private String newfilePath = "";
	private String oldfilePath = "";
	public FileProps(String artifactID, String oldFileName, String filePath,String oldfilePath) {
		this.artifactID = artifactID;
		this.oldFileName = oldFileName;
		this.newfilePath = filePath;
		this.oldfilePath = oldfilePath;
		
	}

	public String getArtifactID() {
		return artifactID;
	}

	public void setArtifactID(String artifactID) {
		this.artifactID = artifactID;
	}

	public String getOldFileName() {
		return oldFileName;
	}

	public String getNewfilePath() {
		return newfilePath;
	}

	public void setNewfilePath(String newfilePath) {
		this.newfilePath = newfilePath;
	}

	public void setOldFileName(String oldFileName) {
		this.oldFileName = oldFileName;
	}
	public String getOldfilePath() {
		return oldfilePath;
	}

	public void setOldfilePath(String oldfilePath) {
		this.oldfilePath = oldfilePath;
	}
}
