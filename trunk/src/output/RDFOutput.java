package output;

/**
 * 
 * @author Maximiliano Ariel Lopez (maxilopez@gmail.com)
 *
 */
public class RDFOutput 
{
	private String format;
	private String graphName;
	private String extension;
	
	public RDFOutput(String format, String graphName, String extension) {
		super();
		this.format = format;
		this.graphName = graphName;
		this.extension = extension;
	}
	
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getGraphName() {
		return graphName;
	}
	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
}
