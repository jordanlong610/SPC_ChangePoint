import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class ReadableFile
{
	/**
	 * Class holding all methods related to reading data from a file.
	 * 
	 * @author Jordan Long and Chris Roadcap
	 */
	
	ArrayList<DataHolderObject> listOfValues;
	ArrayList<DataHolderObject> cuSumList;
	double OriginalSMin;
	double OriginalSMax;
	double OriginalSDiff;
	double average;
	double changePoint = 0;
	DataHolderObject changePointObject;
	ArrayList<DataHolderObject> firstHalf = new ArrayList<DataHolderObject>();
	ArrayList<DataHolderObject> secondHalf = new ArrayList<DataHolderObject>();
	ArrayList<Double> solution = new ArrayList<Double>();

	
	/**
	 * Takes a text file, reads it into an ArrayList of DataHolderObjects, and then computes CUSUMS, SDiff and averages on it.
	 * @param fileName The file to read data from
	 * @throws FileNotFoundException
	 * @author Jordan Long and Chris Roadcap
	 */
	public ReadableFile(String fileName) throws FileNotFoundException
	{
		double position = 0;
		ArrayList<DataHolderObject> originalList = new ArrayList<DataHolderObject>();
		Scanner scanner = new Scanner(new File ("data.txt"));
		while(scanner.hasNextLine())
		{
			originalList.add(new DataHolderObject(position, scanner.nextDouble()));
			position++;
		}
		scanner.close();
		this.listOfValues = originalList;
		this.cuSumList = calculateCusum(originalList);
		this.OriginalSMin = findSMin(this.cuSumList);
		this.OriginalSMax = findSMax(this.cuSumList);
		this.OriginalSDiff = calculateSDiff(this.OriginalSMax, this.OriginalSMin);
		this.average = calculateAverage(originalList);
	}
	
	/**
	 * Returns the list of values in the object.
	 * @return List of values.
	 */
	public ArrayList<DataHolderObject> getListOfValues()
	{
		return this.listOfValues;
	}
	
	
	/**
	 * Calculates the average from a given list of values.
	 * @param list List to compute average on.
	 * @return Average value from list
	 * @author Jordan Long
	 */
	public double calculateAverage(ArrayList<DataHolderObject> list)
	{
		double currentAverage = 0;
		for(int i = 0; i < list.size(); i++)
		{
			currentAverage += list.get(i).getData();
		}
		currentAverage = currentAverage/list.size();
		return currentAverage;
	}
	
	/**
	 * Calculates the CUSUM on the given list.
	 * @param listOfData List to calculate CUSUM on.
	 * @return list of CUSUM values.
	 */
	public ArrayList<DataHolderObject> calculateCusum(ArrayList<DataHolderObject> listOfData)
	{
		double cumulativeSum = 0;
		ArrayList<DataHolderObject> tempCusumList = new ArrayList<DataHolderObject>();
		for(int i = 0 ; i < listOfData.size(); i++)
		{
			cumulativeSum += listOfData.get(i).getData() - calculateAverage(listOfData);
			tempCusumList.add(new DataHolderObject(i, cumulativeSum));
		}
		return tempCusumList;
	}
	
	/**
	 * Finds the smallest value in the list
	 * @param list List to check for value.
	 * @return Smallest value in list.
	 */
	public double findSMin(ArrayList<DataHolderObject> list)
	{
		
		double min = list.get(0).getData();
		for(int i = 0; i < list.size(); i++)
		{
			if (list.get(i).getData() < min)
			{
				min = list.get(i).getData();
			}
		}
		return min;
	}
	
	/**
	 * Finds the largest value in the list
	 * @param list List to check for value.
	 * @return Largest value in list.
	 */
	public double findSMax(ArrayList<DataHolderObject> list)
	{
		double max = list.get(0).getData();
		for(int i = 0; i < list.size(); i++)
		{
			if (list.get(i).getData() > max)
			{
				max = list.get(i).getData();
			}
		}
		return max;
	}
	
	/**
	 * Calculates the difference between the largest and smallest values.
	 * @param sMax Largest value
	 * @param sMin Smallest value
	 * @return Difference between 2 values.
	 */
	public double calculateSDiff(double sMax, double sMin)
	{
		double sDiff;
		sDiff = sMax - sMin;
		return sDiff;
	}
	
	/**
	 * Returns the original SDiff.
	 * @return Original SDiff.
	 */
	public double getOriginalSDiff()
	{
		return this.OriginalSDiff;
	}
	

	/**
	 * Calculates the CUSUM on a list, and if the value is greater than zero it is added to the list.
	 * @param objectList List to calculate the CUSUM on
	 * @return List of change point objects.
	 * @author Chris Roadcap
	 */
	public DataHolderObject setChangePointObject(ArrayList<DataHolderObject> objectList)
	{
		double cp = 0;
		DataHolderObject changePointObject = null;
		for(int i = 0; i < objectList.size(); i++)
		{
			ArrayList<DataHolderObject> temp = calculateCusum(objectList);
			if(Math.abs(temp.get(i).getData()) > cp)
			{
				cp = Math.abs(temp.get(i).getData());
				changePointObject = objectList.get(i);
			}
		}
		return changePointObject;
	}

	/**
	 * Bootstrap method for the change point analysis. Takes in a list of values to check, a given confidence
	 * interval, and number of bootstraps to perform, and returns a list of points that the data changes at.
	 * 
	 * @param listToAnalyze List of values to bootstrap.
	 * @param iterations N times to run the bootstrap.
	 * @param desiredConfidence Confidence interval to compare CUSUM to.
	 * @param fileName Name of the text file for the data points.
	 * @return List of values where the underlying distribution changes.
	 * @throws FileNotFoundException
	 * @author Jordan Long and Chris Roadcap
	 */
	public ArrayList<Double> bootstrap(ArrayList<DataHolderObject> list, double iterations, double desiredConfidence)
	{
		ArrayList<DataHolderObject> copy = new ArrayList<DataHolderObject>(list);
		double actualSmin = findSMin(calculateCusum(list));
		double actualSmax = findSMax(calculateCusum(list));
		double actualOriginalSDiff = calculateSDiff(actualSmax, actualSmin);
		double newSMin;
		double newSMax;
		double newSDiff;
		DataHolderObject changePointObject;
		double numSDiffLessThanOriginal = 0;
		double actualConfidence = 0;
		ArrayList<DataHolderObject> tempCopy = new ArrayList<DataHolderObject>();
		tempCopy.addAll(list);
		copy.clear();
		copy.addAll(tempCopy);
		tempCopy.clear();
		
		
		//Perform n bootstraps on data, randomizing it each time.
		for(int i = 0; i < iterations; i++)
		{
			Collections.shuffle(list);
			newSMin = findSMin(calculateCusum(list));
			newSMax = findSMax(calculateCusum(list));
			newSDiff = calculateSDiff(newSMax, newSMin);
			if(newSDiff < actualOriginalSDiff)
			{
				numSDiffLessThanOriginal++;
			}
		}
		actualConfidence = (numSDiffLessThanOriginal/iterations);
		int firstSplit = 0;
		int secondSplit = 0;
		
		//If confidence of CUSUM is greater or equal to given confidence, a change has bee found
		//Add to list, break data set into 2 parts and recursively run bootstraps on those data sets.
		if(actualConfidence >= desiredConfidence)
		{
			changePointObject = setChangePointObject(copy); 
			solution.add(changePointObject.originalIndex);
			if(changePointObject.originalIndex > 0 && changePointObject.originalIndex < copy.size()-1)
			{
				firstSplit = (int) (changePointObject.originalIndex -1);
				secondSplit = (int)(changePointObject.originalIndex + 1);
			
	
				if(copy.size() > 1)
				{
					firstHalf.clear();
					secondHalf.clear();
					for(int i = 0; i <= firstSplit; i++)
					{
						firstHalf.add(listOfValues.get(i));
					}
				
					for(int i = secondSplit ; i < listOfValues.size(); i++)
					{
						secondHalf.add(listOfValues.get(i));
					}
	
					bootstrap(firstHalf, iterations, desiredConfidence);
					bootstrap(secondHalf, iterations, desiredConfidence);
				}
			
			}
		}

		return solution;
	}

}