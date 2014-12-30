package input;

public class Field 
{
	String name;
	boolean numeric;
	boolean fractional;
	String min;
	String max;
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append(name);
		sb.append(". ");
		
		if (numeric)
			sb.append("numeric. ");
		
		if (fractional)
			sb.append("fractional. ");
		
		if (min!=null)
			sb.append(min);
		else
			sb.append("no min");

		sb.append(". ");
		
		if (max!=null)
			sb.append(max);
		else
			sb.append("no max");
		
		sb.append(". ");
		
		return sb.toString();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isNumeric() {
		return numeric;
	}
	public void setNumeric(boolean numeric) {
		this.numeric = numeric;
	}
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
	public boolean isFractional() {
		return fractional;
	}
	public void setFractional(boolean fractional) {
		this.fractional = fractional;
	}
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	
	public Field(String name)
	{
		setFractional(false);
		setNumeric(true);
		setName(name);
		setMin(null);
		setMax(null);
	}
	
	public void evaluate(String value)
	{
		if (isNumeric())
		{
			if (!isFractional())
			{
				if (Util.isFractional(value))
					setFractional(true);
			}
			
			if (!Util.isNumeric(value))
			{
				setFractional(false);
				setNumeric(false);
			}
		}
		
		if (isNumeric())
		{
			if (min==null || Util.isGreaterThan(min, value))
				min = value;
			
			if (max==null || Util.isGreaterThan(value, max))
				max = value;
		}
	}
	

	public String getType()
	{
		if (!isNumeric())
			return "string";
		else
		{
			if (isFractional())
			{
				return "double";
			}
			else
			{
				if (Util.isGreaterOrEqualThan(min, "0"))
				{
					if (Util.isLessOrEqualThan(max, "255"))
						return "unsignedByte";
					else if (Util.isLessOrEqualThan(max, "65535"))
						return "unsignedShort";
					else if (Util.isLessOrEqualThan(max, "4294967295"))
						return "unsignedInt";
					else if (Util.isLessOrEqualThan(max, "18446744073709551615"))
						return "unsignedLong";
					else if (Util.equalsZero(min))
						return "nonNegativeInteger";
					else
						return "positiveInteger";
				}
				else
				{
					if (Util.isGreaterOrEqualThan(min, "-128") && Util.isLessOrEqualThan(max, "127"))
						return "byte";
					else if (Util.isGreaterOrEqualThan(min, "-32768") && Util.isLessOrEqualThan(max, "32767"))
						return "short";
					else if (Util.isGreaterOrEqualThan(min, "-2147483648") && Util.isLessOrEqualThan(max, "2147483647"))
						return "int";
					else if (Util.isGreaterOrEqualThan(min, "-9223372036854775808") && Util.isLessOrEqualThan(max, "9223372036854775807"))
						return "long";
					else if (!Util.isGreaterOrEqualThan(max, "0"))
						return "negativeInteger";
					else if (Util.equalsZero(max))
						return "nonPositiveInteger";
				}
			}
			
			return "integer";
		}
	}
}
