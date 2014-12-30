package input;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Maximiliano Ariel Lopez (maxilopez@gmail.com)
 *
 */
public class Configuration implements Serializable, Cloneable
{
	private List<File> sourceFiles = new ArrayList<File>();
	private Integer selectedFileIndex;
	private String encoding;
	private String fieldDelimiter;
	private String escapeCharacter;
	
	private Integer headersInRow;
	private Integer startRow;
	private Integer endRow;
	
	private String entityType;
	
	private Integer subjectColumn;
	private String subjectPrefix;

	private long maxRows;
	
	private ArrayList<Predicate> predicatesList = new ArrayList<Predicate>();
	private ArrayList<Prefix> prefixesList = new ArrayList<Prefix>();
	
	private transient String errorMessage;

	@Override
	public String toString() {
		return "Configuration [sourceFile=" + sourceFiles
				+ ", sourceFileContent=" + "@sourceFileContent" + ", encoding="
				+ encoding + ", fieldDelimiter=" + fieldDelimiter
				+ ", headersInRow=" + headersInRow + ", startRow=" + startRow
				+ ", endRow=" + endRow + ", entityType=" + entityType
				+ ", subjectColumn=" + subjectColumn + ", subjectPrefix="
				+ subjectPrefix + ", predicatesList=" + predicatesList + ", prefixesList="
				+ prefixesList + "]";
	}

	public List<File> getSourceFiles() {
		return sourceFiles;
	}

	public void setSourceFiles(List<File> sourceFiles) {
		this.sourceFiles = sourceFiles;
	}
	
	public String getSourceFileContent(long maxRows) {
		if (selectedFileIndex==null || selectedFileIndex<0 || getEncoding()==null || getEncoding().length()==0)
			return "";
		
		return Util.getFileContents(getSourceFiles().get(selectedFileIndex), getEncoding(), maxRows, this);
	}
	
	public TextReader getSourceFileReader() {
		if (selectedFileIndex==null || selectedFileIndex<0 || getEncoding()==null || getEncoding().length()==0)
			return null;
		
		return new TextReader(getSourceFiles().get(selectedFileIndex), getEncoding(), getFieldDelimiter(), getEscapeCharacter());
	}
	
	public Integer getSelectedFileIndex() {
		return selectedFileIndex;
	}

	public void setSelectedFileIndex(Integer selectedFileIndex) {
		this.selectedFileIndex = selectedFileIndex;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getFieldDelimiter() {
		return fieldDelimiter;
	}

	public void setFieldDelimiter(String fieldDelimiter) {
		this.fieldDelimiter = fieldDelimiter;
	}

	public Integer getHeadersInRow() {
		return headersInRow;
	}

	public void setHeadersInRow(Integer headersInRow) {
		this.headersInRow = headersInRow;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	public Integer getEndRow() {
		return endRow;
	}

	public void setEndRow(Integer endRow) {
		this.endRow = endRow;
	}

	public String getEntityType() {
		if (entityType!=null)
			return entityType;
		else
			return "Entity_";
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Integer getSubjectColumn() {
		return subjectColumn;
	}

	public void setSubjectColumn(Integer subjectColumn) {
		this.subjectColumn = subjectColumn;
	}

	public String getSubjectPrefix() {
		if (subjectPrefix!=null)
			return subjectPrefix;
		else
			return "";
	}

	public void setSubjectPrefix(String subjectPrefix) {
		this.subjectPrefix = subjectPrefix;
	}

	public ArrayList<Predicate> getPredicatesList() {
		return predicatesList;
	}

	public void setPredicatesList(ArrayList<Predicate> predicatesList) {
		this.predicatesList = predicatesList;
	}

	public ArrayList<Prefix> getPrefixesList() {
		return prefixesList;
	}

	public void setPrefixesList(ArrayList<Prefix> prefixesList) {
		this.prefixesList = prefixesList;
	}
	
	public ArrayList<String> getPredicatesAsStringList(boolean addPrefix)
	{
		ArrayList<String> predicates = new ArrayList<String>();
		
		if (getPredicatesList()!=null)
		{
			if (addPrefix)
				for (Predicate p : getPredicatesList())
					predicates.add(p.getPredicatePrefix() + p.getName());
			else
				for (Predicate p : getPredicatesList())
					predicates.add(p.getName());
		}
		
		return predicates;
	}
	
	public void addPrefix(String name, String reference)
	{
		getPrefixesList().add(new Prefix(name, reference));
	}

	public void addPredicate(String name, String type, String predicatePrefix, String objectPrefix)
	{
		getPredicatesList().add(new Predicate(name, type, predicatePrefix, objectPrefix));
	}

	public void addSourceFile(File file)
	{
		getSourceFiles().add(file);
	}
	
	public String getEscapeCharacter() {
		return escapeCharacter;
	}

	public void setEscapeCharacter(String escapeCharacter) {
		this.escapeCharacter = escapeCharacter;
	}
	
	public long getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(long maxRows) {
		this.maxRows = maxRows;
	}

	public String getErrorMessage() {
		if (errorMessage!=null)
			return errorMessage;
		else
			return "";
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@Override
	public Object clone() 
	{
		Configuration n = new Configuration();
		
		n.setSourceFiles(this.getSourceFiles());
		n.setSelectedFileIndex(this.getSelectedFileIndex());
		n.setEncoding(this.getEncoding());
		n.setFieldDelimiter(this.getFieldDelimiter());
		n.setEscapeCharacter(this.getEscapeCharacter());
		n.setHeadersInRow(this.getHeadersInRow());
		n.setStartRow(this.getStartRow());
		n.setEndRow(this.getEndRow());
		n.setEntityType(this.getEntityType());
		n.setSubjectColumn(this.getSubjectColumn());
		n.setSubjectPrefix(n.getSubjectPrefix());
		n.setPredicatesList(this.getPredicatesList());
		n.setPrefixesList(this.getPrefixesList());
		
		return n;
	}
	
	
}
