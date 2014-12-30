package input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * 
 * @author Maximiliano Ariel Lopez (maxilopez@gmail.com)
 *
 */
public class TextReader 
{
	private BufferedReader br;
	private String encoding;
	private String fieldDelimiter;
	private String escapeCharacter;
	
	private long currentRowNumber = 0;
	private String currentRowContent = null;

	public String getCurrentRowContent() {
		return currentRowContent;
	}

	public long getCurrentRowNumber() {
		return currentRowNumber;
	}
	
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public TextReader(File file, String encoding, String fieldDelimiter, String escapeCharacter)
	{
		setEncoding(encoding);
		open(file);
		this.fieldDelimiter = fieldDelimiter;
		this.escapeCharacter = escapeCharacter;
	}
	
	public TextReader(String file, String encoding, String fieldDelimiter, String escapeCharacter)
	{
		setEncoding(encoding);
		open(file);
		this.fieldDelimiter = fieldDelimiter;
		this.escapeCharacter = escapeCharacter;
	}

	public void open(String file)
	{
		open(new File(file));
	}
	
	public void open(File file)
	{
		currentRowNumber = 0;
		
		try
		{
			br = new BufferedReader(new FileReader(file));
		} 
		catch (Exception e) 
		{
			throw new RuntimeException(e);				
		}				
	}

	public String getNextLine()
	{
		try
		{
			String line = br.readLine();
			
			if (line==null)
				return null;

			currentRowContent = new String(line.getBytes(), getEncoding());
			currentRowNumber++;
			
			while (escapeCharacter!=null && count(currentRowContent, escapeCharacter)%2!=0)
			{
				line = br.readLine();
				
				if (line==null)
					break;
				
				currentRowContent += new String(("\n" + line).getBytes(), getEncoding());
			}
			
			if (currentRowContent!=null)
				return currentRowContent;
			else
			{
				close();
				return null;
			}
		} 
		catch (Exception e) 
		{
			throw new RuntimeException(e);				
		}
	}
	
	public void close()
	{
		try
		{
			br.close();
		} 
		catch (Exception e) 
		{
		}	
	}
	
	public int count(String text, String value)
	{
		int r = 0;
		int c = 0;
		
		while (text!=null)
		{
			c = text.indexOf(value, c);
			
			if (c<0)
				break;
			
			r++;
			c++;
		}
		
		return r;
	}
}
