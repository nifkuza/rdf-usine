package output;

import input.Util;

/**
 * 
 * @author Maximiliano Ariel Lopez (maxilopez@gmail.com)
 *
 */
public class Triple implements Comparable<Triple>
{
	private String subject;
	private String predicate;
	private String object;
	private String objectType;
	
	@Override
	public String toString() 
	{
		return "Triple [" + subject + "," + predicate
				+ "," + object + "]";
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getPredicate() {
		return predicate;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	
	@Override
	public int hashCode() 
	{
		int r = 0;
		
		if (subject!=null)
			r += subject.hashCode();
		
		if (predicate!=null)
			r += predicate.hashCode();
		
		if (object!=null)
			r += object.hashCode();
		
		if (objectType!=null)
			r += objectType.hashCode();
		
		return r;
	}
	
	@Override
	public boolean equals(Object arg0) 
	{
		if (!(arg0 instanceof Triple))
			return false;
		
		return this.compareTo((Triple) arg0)==0;
	}

	@Override
	public int compareTo(Triple arg0) 
	{
		if (!this.getSubject().equals(arg0.getSubject()))
			return Util.compareStrings(this.getSubject(), arg0.getSubject());
		else
		{
			if (!this.getPredicate().equals(arg0.getPredicate()))
				return Util.compareStrings(this.getPredicate(), arg0.getPredicate());
			else
				return Util.compareStrings(this.getObject(), arg0.getObject());
		}
	}
}
