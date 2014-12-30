package input;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import org.apache.commons.lang3.text.StrTokenizer;

import output.RDFOutput;
import output.RowNotValidException;
import output.Triple;
import frontend.Popup;
import frontend.Popup.Type;

/**
 * 
 * @author Maximiliano Ariel Lopez (maxilopez@gmail.com)
 *
 */
public class Util 
{
	private static int TRIPLES_BATCH_SIZE = 50000;
	
	public static ArrayList<String> getCharsets()
	{
		ArrayList<String> r = new ArrayList<String>();

		r.add(StandardCharsets.UTF_8.displayName());
		r.add(StandardCharsets.UTF_16.displayName());
		r.add("UTF-32");
		r.add("ISO-8859-1");
		r.add("ISO-8859-2");
		r.add("ISO-8859-3");
		r.add("ISO-8859-4");
		r.add("ISO-8859-5");
		r.add("ISO-8859-6");
		r.add("ISO-8859-7");
		r.add("ISO-8859-8");
		r.add("ISO-8859-9");
		r.add("ISO-8859-13");
		r.add("ISO-8859-15");
		r.add(StandardCharsets.US_ASCII.displayName());
		r.add("windows-1250");
		r.add("windows-1251");
		r.add("windows-1252");
		r.add("windows-1253");
		r.add("windows-1254");
		r.add("windows-1255");
		r.add("windows-1256");
		r.add("windows-1257");
		r.add("windows-1258");
		r.add("Big5");
		r.add("Big5-HKSCS");
		r.add("EUC-JP");
		r.add("EUC-KR");
		r.add("GB2312");
		r.add("GB18030");
		r.add("GBK");
		r.add("IBM037");
		r.add("IBM273");
		r.add("IBM277");
		r.add("IBM278");
		r.add("IBM280");
		r.add("IBM284");
		r.add("IBM285");
		r.add("IBM290");
		r.add("IBM297");
		r.add("IBM420");
		r.add("IBM424");
		r.add("IBM437");
		r.add("IBM500");
		r.add("IBM775");
		r.add("IBM850");
		r.add("IBM852");
		r.add("IBM855");
		r.add("IBM857");
		r.add("IBM00858");
		r.add("IBM860");
		r.add("IBM861");
		r.add("IBM862");
		r.add("IBM863");
		r.add("IBM864");
		r.add("IBM865");
		r.add("IBM866");
		r.add("IBM868");
		r.add("IBM869");
		r.add("IBM870");
		r.add("IBM871");
		r.add("IBM918");
		r.add("IBM1026");
		r.add("IBM1047");
		r.add("IBM01140");
		r.add("IBM01141");
		r.add("IBM01142");
		r.add("IBM01143");
		r.add("IBM01144");
		r.add("IBM01145");
		r.add("IBM01146");
		r.add("IBM01147");
		r.add("IBM01148");
		r.add("IBM01149");
		r.add("IBM-Thai");
		r.add("ISO-2022-CN");
		r.add("ISO-2022-JP");
		r.add("ISO-2022-JP-2");
		r.add("ISO-2022-KR");
		r.add("JIS_X0201");
		r.add("JIS_X0212-1990");
		r.add("KOI8-R");
		r.add("KOI8-U");
		r.add("Shift_JIS");
		r.add("TIS-620");
		r.add(StandardCharsets.UTF_16BE.displayName());
		r.add(StandardCharsets.UTF_16LE.displayName());
		r.add("UTF-32BE");
		r.add("UTF-32LE");
		r.add("windows-31j");
		
		return r;
	}
	
	public static String chooseFile()
	{
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(null);      
		
		if (file != null)
			return file.getAbsolutePath();
		else
			return "";
	}

	public static String chooseFolder()
	{
		DirectoryChooser dirChooser = new DirectoryChooser();
		File file = dirChooser.showDialog(null);
		
		if (file != null)
			return file.getAbsolutePath();
		else
			return "";
	}
	
	public static List<File> chooseFiles()
	{
        FileChooser fileChooser = new FileChooser();

        return fileChooser.showOpenMultipleDialog(null);        
	}
	
	public static String chooseFileForSaving()
	{
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showSaveDialog(null);
		
		if (file != null)
			return file.getAbsolutePath();
		else
			return "";
	}
	
	public static boolean fileExists(String path)
	{
		if (path==null)
			return false;
		
		path = path.trim();
		
		if (path.length()==0)
			return false;
		
		File f = new File(path);
		return f.exists();
	}
	
	public static String getFileContents(String path, String encoding, Long maxRows, Configuration conf)
	{
		return getFileContents(new File(path), encoding, maxRows, conf);
	}
	
	public static String getFileContents(File file, String encoding, Long maxRows, Configuration conf)
	{
		StringBuffer sb = new StringBuffer();
		
		try
		{
			TextReader tr = new TextReader(file, encoding, conf.getFieldDelimiter(), conf.getEscapeCharacter());
						
			while (tr.getNextLine()!=null) 
			{
				sb.append(tr.getCurrentRowContent() + "\n");
				
				if (maxRows!=null && maxRows<Long.MAX_VALUE && tr.getCurrentRowNumber()>=maxRows)
					break;
			}

			tr.close();
			return sb.toString();
		} 
		catch (Exception e) 
		{
			Popup.popupMessage(Type.ERROR, "Error while reading file", "Read error");
			return "";				
		}			
	}

	public static ArrayList<String> getFieldDelimiterSymbols()
	{
		ArrayList<String> r  = new ArrayList<String>();
		
		r.add(";");
		r.add("|");
		r.add(",");
		r.add(" ");
		r.add("\t");
		r.add("$");
		
		return r;
	}
	
	public static ArrayList<String> getFieldDelimiterTexts(ResourceBundle rb)
	{
		ArrayList<String> r = new ArrayList<String>();
		
		r.add(rb.getString("semicolon"));
		r.add(rb.getString("pipe"));
		r.add(rb.getString("comma"));
		r.add(rb.getString("space"));
		r.add(rb.getString("tab"));
		r.add(rb.getString("dollar_sign"));
		
		return r;
	}
	
	public static ArrayList<String> getDataTypes()
	{
		ArrayList<String> r = new ArrayList<String>();

		r.add("string");
		r.addAll(getDataTypeClasses().keySet());
			
		return r;
	}

	public static Map<String, String> getDataTypeClasses()
	{
		Map<String, String> r = new HashMap<String, String>();

		//r.put("string", "http://www.w3.org/2001/XMLSchema#string");
		r.put("byte", "http://www.w3.org/2001/XMLSchema#byte");
		r.put("short", "http://www.w3.org/2001/XMLSchema#short");
		r.put("int", "http://www.w3.org/2001/XMLSchema#int");
		r.put("integer", "http://www.w3.org/2001/XMLSchema#integer");
		r.put("long", "http://www.w3.org/2001/XMLSchema#long");
		r.put("double", "http://www.w3.org/2001/XMLSchema#double");
		r.put("negativeInteger", "http://www.w3.org/2001/XMLSchema#negativeInteger");
		r.put("nonNegativeInteger", "http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
		r.put("nonPositiveInteger", "http://www.w3.org/2001/XMLSchema#nonPositiveInteger");
		r.put("positiveInteger", "http://www.w3.org/2001/XMLSchema#positiveInteger");
		r.put("unsignedLong", "http://www.w3.org/2001/XMLSchema#unsignedLong");
		r.put("unsignedInt", "http://www.w3.org/2001/XMLSchema#unsignedInt");
		r.put("unsignedShort", "http://www.w3.org/2001/XMLSchema#unsignedShort");
		r.put("unsignedByte", "http://www.w3.org/2001/XMLSchema#unsignedByte");
		r.put("URI", "");
			
		return r;
	}

	public static String getDataTypeClass(String name)
	{
		return getDataTypeClasses().get(name);
	}
	
	public static Integer parseInt(String v)
	{
		if (v==null || v.length()==0)
			return null;
		
		try
		{
			return Integer.parseInt(v);
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}
	
	public static ArrayList<String> getHeadersFromInputFile(Configuration conf, boolean includeSubjectColumn, boolean addPrefixes)
	{
		if (conf.getHeadersInRow()!=null)
			return readHeadersFromFile(conf, includeSubjectColumn);
		else
			return conf.getPredicatesAsStringList(addPrefixes);
	}
	
	public static ArrayList<String> readHeadersFromFile(Configuration conf, boolean includeSubjectColumn)
	{
		if (conf.getHeadersInRow()==null)
			return null;
		
		String contents = conf.getSourceFileContent(conf.getHeadersInRow());

		if (contents==null || contents.length()==0)
			return null;
		
		String[] lines = contents.split("\\n");
		
		String[] hs = Util.tokenize(lines[conf.getHeadersInRow() - 1], conf);
		ArrayList<String> hsWithoutSubjectColumn = new ArrayList<String>();
		
		int c = 1;
		for (String h : hs)
		{
			if (includeSubjectColumn || conf.getSubjectColumn()==null || c!=conf.getSubjectColumn())
				hsWithoutSubjectColumn.add(h);
				
			c++;
		}

		return hsWithoutSubjectColumn;
	}
	
	public static List<ObservableList<String>> getRowsFromInputFile(Configuration conf, Long maxRecords)
	{
		List<ObservableList<String>> rows = new ArrayList<ObservableList<String>>();
		
		TextReader tr = conf.getSourceFileReader();
		//List<String> headers = getHeadersFromInputFile(conf, true, false);
		
		if (tr==null)
			return rows;

		long indexLine = conf.getStartRow()!=null ? conf.getStartRow() : 0;
		long records = 0;
		
		// Skip starting rows in TextReader
		for (int c=0; c<indexLine; c++)
			tr.getNextLine();
		
		while (tr.getNextLine()!=null)
		{
			// The header row is skipped
			if (conf.getHeadersInRow()!=null && indexLine==conf.getHeadersInRow()-1)
			{
				indexLine++;
				continue;
			}

			ObservableList<String> row = FXCollections.observableArrayList();
			
			int fieldCount = 1;
			String[] fields = Util.tokenize(tr.getCurrentRowContent(), conf);
			
			for (String field : fields)
			{
				if (conf.getSubjectColumn()==null || conf.getSubjectColumn()!=fieldCount) 
					row.add(field);
				
				fieldCount++;
			}
			
			rows.add(row);

			records++;
			
			if (conf.getEndRow()!=null && records >= conf.getEndRow() )
				break;
			
			if (maxRecords!=null && records>=maxRecords)
				break;
		}

		tr.close();
		return rows;
	}
	
	public static Field getField(String name, List<Field> list)
	{
		for (Field f : list)
			if (f.getName().equals(name))
				return f;
		
		return null;
	}
	
	public static List<Field> getFieldTypes(Configuration conf)
	{
		List<Field> rows = new ArrayList<Field>();

		TextReader tr = conf.getSourceFileReader();
		ArrayList<String> headers = getHeadersFromInputFile(conf, true, false);
		
		if (tr==null || headers==null || headers.size()==0)
		{
			tr.close();
			return rows;
		}
		
		//long fieldsPerRow = Util.countFieldsInFile(conf);

		long index = conf.getStartRow()!=null ? conf.getStartRow() : 0;
		
		// Skip starting rows in TextReader
		for (int c=0; c<index; c++)
			tr.getNextLine();
		
		long records = 0;
		
		while (tr.getNextLine()!=null)
		{
			// The header row is skipped
			if (conf.getHeadersInRow()!=null && index==conf.getHeadersInRow()-1)
			{
				index++;
				continue;
			}

			int fieldCount = 1;
			for (String field : Util.tokenize(tr.getCurrentRowContent(), conf))
			{
				if (conf.getSubjectColumn()==null || conf.getSubjectColumn()!=fieldCount) 
				{
					Field f = null;
					
					if (rows.size() < fieldCount)
					{
						f = new Field(headers.get(fieldCount-1));
						rows.add(f);
					}
					else
					{
						f = rows.get(fieldCount-1);
					}

					f.evaluate(field);
				}
				
				fieldCount++;
				
				if (fieldCount > headers.size())
					break;
			}
			
			records++;
			
			if (conf.getEndRow()!=null && records >= conf.getEndRow() )
				break;
			
			index++;
		}

		return rows;
	}
	
	public static long getTriplesFromInputFile(Configuration conf, Long pageSize, Long offset, Set<Triple> triples)
	{
		List<String> headers = getHeadersFromInputFile(conf, true, true);

		int ignoringFields = 0;
		TextReader tr = conf.getSourceFileReader();
		
		if (tr==null)
			return 0;
		
		if (headers.size()==0)
		{
			tr.close();
			return 0;
		}
		
		long indexLine = conf.getStartRow()!=null ? conf.getStartRow() : 0;
		long records = offset;
		
		// Skip starting rows in TextReader
		for (int c=0; c<indexLine; c++)
			tr.getNextLine();
		
		while (tr.getNextLine()!=null)
		{
			// The header row is skipped
			if (conf.getHeadersInRow()!=null && indexLine==conf.getHeadersInRow()-1)
			{
				indexLine++;
				continue;
			}

			int headerOffset = conf.getHeadersInRow()!=null ? conf.getHeadersInRow() : 0;
			
			if (offset!=null && (indexLine-headerOffset)<offset)
			{
				indexLine++;
				continue;
			}

			records++;
			
			int indexField = 0;
			String[] fields = Util.tokenize(tr.getCurrentRowContent(), conf);
			
			Triple entityTriple = new Triple();
			
			if (conf.getSubjectColumn()!=null)
			{
				if (fields.length < conf.getSubjectColumn())
					throw new RowNotValidException();
				
				entityTriple.setSubject(conf.getSubjectPrefix() + fields[conf.getSubjectColumn()-1]);
			}
			else
			{
				entityTriple.setSubject(conf.getSubjectPrefix() + records);
			}
			
			entityTriple.setPredicate("a");
			entityTriple.setObject(conf.getEntityType());
			entityTriple.setObjectType("URI");
			triples.add(entityTriple);
			
			for (String field : fields)
			{
				if (conf.getSubjectColumn()==null || (conf.getSubjectColumn()-1)!=indexField) 
				{
					if (field.trim().length()>0)
					{
						Triple triple = new Triple();
						
						int indexConsideringSubject = indexField;
						if (conf.getSubjectColumn()!=null && (indexField+1) >= conf.getSubjectColumn())
							indexConsideringSubject--;
						
						if (indexConsideringSubject > headers.size()-1 || 
								indexConsideringSubject > conf.getPredicatesList().size()-1)
						{
							ignoringFields++;
							continue;
						}
						
						triple.setSubject(entityTriple.getSubject());

						//if (conf.getSubjectColumn()==null)
						//	triple.setPredicate(headers.get(indexField));
						//else
							triple.setPredicate(headers.get(indexConsideringSubject));
							
						triple.setObject(conf.getPredicatesList().get(indexConsideringSubject).getObjectPrefix() + field.replace("\n", "\t"));
						
						String type = conf.getPredicatesList().get(indexConsideringSubject).getType();
						triple.setObjectType(type);
						
						triples.add(triple);
					}
				}
				
				indexField++;
			}
			
			if (conf.getEndRow()!=null && records >= conf.getEndRow() )
				break;
			
			if (pageSize!=null && (records-offset)>=pageSize)
				break;
			
			indexLine++;
		}

		tr.close();
		
		if (ignoringFields>0)
			conf.setErrorMessage("Ignoring: " + ignoringFields + " fields.");
		else
			conf.setErrorMessage("");
		
		return records-offset;
	}

	public static long countRowsInInputFile(Configuration conf)
	{
		long records = 0;
		TextReader tr = conf.getSourceFileReader();
		
		if (tr==null)
			return records;
		
		while (tr.getNextLine()!=null)
			records++;
		
		if (conf.getStartRow()!=null)
			records -= conf.getStartRow();
		
		if (conf.getHeadersInRow()!=null)
			records--;
	
		tr.close();
		return records;
	}

	public static int countFieldsInFile(Configuration conf)
	{
		TextReader tr = conf.getSourceFileReader();
		
		if (tr==null)
			return 0;
		
		int indexLine = conf.getStartRow()!=null ? conf.getStartRow() : 0;
		
		// Skip starting rows in TextReader
		for (int c=0; c<indexLine; c++)
			tr.getNextLine();
		
		tr.getNextLine();
		
		// The header row is skipped
		if (conf.getHeadersInRow()!=null && indexLine==conf.getHeadersInRow()-1)
		{
			String[] fields = Util.tokenize(tr.getCurrentRowContent(), conf);
			tr.close();
			
			return fields.length;
		}
		
		if (tr.getCurrentRowContent()==null)
		{
			tr.close();
			return 1;
		}
		
		String[] fields = Util.tokenize(tr.getCurrentRowContent(), conf);
		tr.close();
		
		return fields.length;
	}
	
	public static int getInteger(Integer i, int defaultValue)
	{
		if (i!=null)
			return i;
		else
			return defaultValue;
	}
	
	public static String[] tokenize(String text, Configuration conf)
	{
		StrTokenizer str = new StrTokenizer(text, conf.getFieldDelimiter());
		str.setIgnoreEmptyTokens(false);
		
		if (conf.getEscapeCharacter()!=null && conf.getEscapeCharacter().length()>0)
			str.setQuoteChar(conf.getEscapeCharacter().charAt(0));
		
		return str.getTokenArray();
	}
	
	public static void saveRFDWithPrefixes(Configuration conf, String format, String graphUri, Long maxRecords, String file)
	{
		OutputStream os;
		
		try 
		{
			os = new BufferedOutputStream(new FileOutputStream(file));
		} 
		catch (FileNotFoundException e) 
		{
			throw new RuntimeException();
		} 
		
		saveRFDWithPrefixes(conf, format, graphUri, maxRecords, os);
	}
	
	public static void saveRFDWithPrefixes(Configuration conf, String format, String graphUri, Long maxRecords, OutputStream os)
	{
		long totalRecords = Util.countRowsInInputFile(conf);
		
		try
		{
			if (!format.equals("FUSEKI"))
			{
				for (Prefix p : conf.getPrefixesList())
				{
					os.write("@prefix ".getBytes());
					os.write(p.getName().getBytes());
					os.write(": <".getBytes());
					os.write(p.getReference().getBytes());
					os.write("> .\n".getBytes());
				}
			}
			else
			{
				if (conf.getSubjectPrefix()!=null && conf.getSubjectPrefix().length()>0)
					os.write(("BASE <" + conf.getSubjectPrefix() + ">").getBytes());
				
				for (Prefix p : conf.getPrefixesList())
				{
					os.write("PREFIX ".getBytes());
					os.write(p.getName().getBytes());
					os.write(": <".getBytes());
					os.write(p.getReference().getBytes());
					os.write(">\n".getBytes());
				}
			}

			if (format.equals("FUSEKI"))
			{
				os.write("INSERT {\n".getBytes());
			}
			
			os.write("\n".getBytes());
			
			long processedSoFar = 0;
			long targetCount = (long) Math.min(TRIPLES_BATCH_SIZE, maxRecords);
			
			//long fieldsPerRow = Util.countFieldsInFile(conf);
			
			while (processedSoFar < Math.min(Math.min(maxRecords, totalRecords), conf.getEndRow()!=null ? conf.getEndRow() : Integer.MAX_VALUE) )
			{
				Set<Triple> pagedTriplesSet = new HashSet<Triple>();
				long addedRecords = Util.getTriplesFromInputFile(conf, targetCount, processedSoFar, pagedTriplesSet);
				
				List<Triple> pagedTriples = new ArrayList<Triple>(pagedTriplesSet);
				Collections.sort(pagedTriples);
				
				if (pagedTriples.size()==0)
					break;
				
				if (format.equals("TURTLE"))
					Util.toTurtle(pagedTriples, graphUri, conf, os);
				else if (format.equals("N-TRIPLE"))
					Util.toNTriples(pagedTriples, graphUri, conf, os);
				else if (format.equals("FUSEKI"))
					Util.toNTriples(pagedTriples, graphUri, conf, os);
					
				
				processedSoFar += addedRecords;
			}
	
			if (format.equals("FUSEKI"))
				os.write(" } WHERE {}".getBytes());
			
			os.flush();
			os.close();
		}
		catch (Exception e)
		{
			try
			{
				os.close();
			}
			catch(Exception e1)
			{
				System.err.println(e1);
			}
			
			throw new RuntimeException(e);
		}
	}

	private static void toTurtle(List<Triple> pagedTriples, String graphUri, Configuration conf, OutputStream os) 
	{
		String lastSubject = null;
		String lastPredicate = null;
		boolean written = false;
		
		try
		{
			for (Triple t : pagedTriples)
			{
				if (lastSubject==null || !lastSubject.equals(t.getSubject()))
				{
					//TODO
					if (t.getSubject().trim().length()==0 || t.getSubject().endsWith("/"))
						continue;
					
					lastSubject = t.getSubject();

					if (written)
						os.write(".\n\n".getBytes());
					
					os.write("<".getBytes());
					os.write(replacePrefixes(t.getSubject(), conf));
					os.write(">\n\t<".getBytes());
					os.write(replacePrefixes(t.getPredicate().trim(), conf));
					os.write("> ".getBytes());
					writeObject(os, t, conf);				
					
					written = true;
				}
				else
				{
					if (lastPredicate==null || !lastPredicate.equals(t.getPredicate()))
					{
						os.write(";\n\t<".getBytes());
						os.write(replacePrefixes(t.getPredicate().trim(), conf));
						os.write("> ".getBytes());
						writeObject(os, t, conf);				
					}
					else
					{
						os.write(", ".getBytes());
						writeObject(os, t, conf);				
					}
						
				}
			}
			
			if (written)
				os.write(".".getBytes());
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private static void toNTriples(List<Triple> pagedTriples, String graphUri, Configuration conf, OutputStream os) 
	{
		try
		{
			for (Triple t : pagedTriples)
			{
				//TODO
				if (t.getSubject().trim().length()==0 || t.getSubject().endsWith("/"))
					continue;
				
				os.write("<".getBytes());
				os.write(replacePrefixes(t.getSubject(), conf));
				os.write("> <".getBytes());
				
				os.write(replacePrefixes(t.getPredicate().trim(), conf));
				os.write("> ".getBytes());
				
				writeObject(os, t, conf);				
				os.write(".\n".getBytes());
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private static void writeObject(OutputStream os, Triple t, Configuration conf) throws Exception
	{
		if (t.getObjectType()!=null && t.getObjectType().equals("URI"))
		{
			os.write("<".getBytes());
			os.write(replacePrefixes(t.getObject().trim(), conf));
			os.write(">".getBytes());			
		}
		else
		{
			os.write("\"".getBytes());
			os.write(replacePrefixes(t.getObject().trim(), conf));
			os.write("\"".getBytes());
		}
		
		if (t.getObjectType()!=null && !t.getObjectType().equals("string") && !t.getObjectType().equals("URI"))
		{			
			os.write("^^<".getBytes());
			os.write(getDataTypeClass(t.getObjectType()).getBytes());
			os.write(">".getBytes());
		}
	}
	
	public static byte[] replacePrefixes(String value, Configuration conf)
	{
		for (Prefix p : conf.getPrefixesList())
			value = value.replaceAll(p.getReference(), p.getName() + ":");
		
		return value.getBytes();
	}

	public static List<RDFOutput> getRDFFormats()
	{
		List<RDFOutput> r = new ArrayList<RDFOutput>();
		
		r.add(new RDFOutput("TURTLE", "http://rdf.org/graph", ".ttl"));
		//r.add(new RDFOutput("TRIX", "http://rdf.org/graph", ".trix"));
		//r.add(new RDFOutput("TRIG", "http://rdf.org/graph", ".trig"));
		//r.add(new RDFOutput("RDF/XML", "http://rdf.org/graph", "xml"));
		r.add(new RDFOutput("N-TRIPLE", "http://rdf.org/graph", ".ntriple"));
		
		return r;
	}
	
	public static boolean saveToFile(String content, String filename)
	{
		try 
		{
			OutputStream os = new BufferedOutputStream(new FileOutputStream(filename)); 
			
			os.write(content.getBytes());
			os.flush();
			os.close();
			return true;
		} 
		catch (Exception e1) 
		{
			return false;
		}
	}
	
	public static List<File> getFiles(String dir)
	{
		List<File> files = new ArrayList<File>();
		
		if (dir!=null)
		{
			File d = new File(dir);
			
			for (File f : d.listFiles())
			{
				if (f.isDirectory())
					files.addAll(getFiles(f.getAbsolutePath()));
				else
					files.add(f);
			}
		}
		
		return files;
	}
	
	public static boolean containsOnlyOne(String text, String character)
	{
		return (text.contains(character) && text.indexOf(character)==text.lastIndexOf(character));
	}
	
	public static boolean equalsZero(String value)
	{		
		Double parsed = parse(value);
		
		if (parsed==null)
			return false;
		
		return parsed==0;
	}
	
	public static boolean isLessOrEqualThan(String a, String b)
	{
		if (a==null || b==null)
			return false;
		
		return a.equals(b) || isGreaterThan(b, a);
	}

	public static boolean isGreaterOrEqualThan(String a, String b)
	{
		if (a==null || b==null)
			return false;
		
		return a.equals(b) || isGreaterThan(a, b);
	}
	
	public static boolean isGreaterThan(String a, String b)
	{
		if (a==null || b==null || a.equals(b))
			return false;
		
		// A is negative and B is positive or zero
		if (a.contains("-") && !b.contains("-"))
			return false;
		
		// A is positive and B is negative or zero
		if (!a.contains("-") && equalsZero(a) && (b.contains("-") || equalsZero(b)))
			return true;
		
		// A is zero and B is positive
		if (equalsZero(a) && !b.contains("-") && equalsZero(b))
			return false;

		// A is zero and B is negative
		if (equalsZero(a) && b.contains("-"))
			return true;
		
		if (a.indexOf(".")>=0)
			a = a.substring(0, a.indexOf("."));
		
		if (b.indexOf(".")>=0)
			b = b.substring(0, b.indexOf("."));
		
		//Both negatives
		if (a.contains("-") && b.contains("-"))
		{
			a = a.substring(a.indexOf("-")+1);
			b = b.substring(b.indexOf("-")+1);
			
			a = addTrailingZeroes(a, Math.max(a.length(), b.length()));
			b = addTrailingZeroes(b, Math.max(a.length(), b.length()));
			
			if (a.compareTo(b)>0)
				return false;
			else
				return true;
		}
		
		a = addTrailingZeroes(a, Math.max(a.length(), b.length()));
		b = addTrailingZeroes(b, Math.max(a.length(), b.length()));
		
		//Both positives
		if (a.compareTo(b)>0)
			return true;
		else
			return false;
	}
	
	public static String addTrailingZeroes(String a, int length)
	{
		StringBuffer sb = new StringBuffer();
		
		for(int i=0; i<(length-a.length()); i++)
			sb.append("0");
		
		sb.append(a);
		return sb.toString();
	}
	
	public static Double parse(String value)
	{
		try
		{
			return Double.parseDouble(value);
		}
		catch(NumberFormatException e)
		{
			return null;
		}
	}
	
	public static boolean isFractional(String value)
	{
		return isNumeric(value) && value.contains(".");
	}
	
	public static boolean isNumeric(String value)
	{
		return Util.parse(value)!=null;
	}
	
	public static int booleanAsInt(boolean v)
	{
		if (v)
			return 1;
		else
			return -1;
	}
	
	public static int compareStrings(String a, String b)
	{
		if (!Util.isNumeric(a) || !Util.isNumeric(b))
			return a.compareTo(b);
		else
			return Util.booleanAsInt(Util.isGreaterThan(a, b));
	}
}
