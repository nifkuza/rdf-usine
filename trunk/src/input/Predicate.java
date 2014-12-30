package input;

import java.io.Serializable;

/**
 * 
 * @author Maximiliano Ariel Lopez (maxilopez@gmail.com)
 *
 */
public class Predicate implements Serializable
{
	private String predicatePrefix;
	private String objectPrefix;
	private String name;
	private String type;
	
	public Predicate(String name, String type, String predicatePrefix, String objectPrefix) {
		super();
		this.name = name;
		this.type = type;
		this.predicatePrefix = predicatePrefix;
		this.objectPrefix = objectPrefix;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getPredicatePrefix() {
		return predicatePrefix;
	}

	public void setPredicatePrefix(String predicatePrefix) {
		this.predicatePrefix = predicatePrefix;
	}

	public String getObjectPrefix() {
		return objectPrefix;
	}

	public void setObjectPrefix(String objectPrefix) {
		this.objectPrefix = objectPrefix;
	}
}
