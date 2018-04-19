
public class DataHolderObject 
{
	/**
	 * DataHolderObject Class. Holds the value and index position for each value in a data set.
	 * @author Chris Roadcap
	 */
	double data;
	double originalIndex;
	
	/**
	 * Constructor for the DataHolderObject
	 * @param originalIndex The index that the value is located at in the list.
	 * @param value The value at that index.
	 */
	public DataHolderObject (double originalIndex, double value)
	{
		this.originalIndex = originalIndex;
		this.data = value;
	}
	
	/**
	 * Returns the data in the DataHolderObject
	 * @return data
	 */
	public double getData()
	{
		return this.data;
	}
	
	/**
	 * Returns the index in the DataHolderObject.
	 * @return
	 */
	public double getOriginalIndex()
	{
		return this.originalIndex;
	}

}
