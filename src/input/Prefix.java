package input;

import java.io.Serializable;

/**
 * 
 * @author Maximiliano Ariel Lopez (maxilopez@gmail.com)
 *
 */
public class Prefix implements Serializable 
{
	private String name;
	private String reference;
	
	public Prefix(String name, String reference) {
		super();
		this.name = name;
		this.reference = reference;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getReference() {
		return reference;
	}
	
	public void setReference(String reference) {
		this.reference = reference;
	}
}
