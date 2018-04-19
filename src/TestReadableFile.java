import static org.junit.Assert.*;

import java.awt.List;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.junit.Test;

public class TestReadableFile 
{

	/**
	 * Test class for the Change Point Analysis Project
	 * 
	 * @author Jordan Long and Chris Roadcap
	 * @throws FileNotFoundException
	 */
	
	/**
	 * Test that the file is read into an object properly.
	 * @throws FileNotFoundException
	 */
	@Test
	public void testInitializtion() throws FileNotFoundException
	{
		ReadableFile rf = new ReadableFile("data.txt");
		for(int i = 0; i < rf.getListOfValues().size(); i++)
		{
			assertTrue(rf.getListOfValues().get(i) instanceof DataHolderObject);
		}		
	}
	
	/**
	 * Testing computing an average correctly.
	 * @throws FileNotFoundException
	 */
	@Test
	public void testCalculateAverage() throws FileNotFoundException
	{
		ReadableFile rf = new ReadableFile("data.txt");
		assertEquals(11.395, rf.calculateAverage(rf.getListOfValues()), .001);
	}
	
	/**
	 * Testing finding a cusum for a given set of data.
	 * @throws FileNotFoundException
	 */
	@Test
	public void testCusum() throws FileNotFoundException
	{
		ReadableFile rf = new ReadableFile("data.txt");
		ArrayList<DataHolderObject> temp = rf.calculateCusum(rf.getListOfValues());
		//Last value in cusum list should always be zero
		assertEquals(0, temp.get(temp.size()-1).getData(), .001);

	}
	
	/**
	 * Testing finding the smallest value in the cusum list.
	 * @throws FileNotFoundException
	 */
	@Test
	public void testfindSMin() throws FileNotFoundException
	{
		ArrayList<DataHolderObject> temp;
		ReadableFile rf = new ReadableFile("data.txt");
		
		temp = rf.calculateCusum(rf.getListOfValues());
		
		double min = rf.findSMin(temp);
		//Minium value in cusum list
		assertEquals(-.695, min, .001);
	}
	
	/**
	 * Finding the largest value in the CUSUM list.
	 * @throws FileNotFoundException
	 */
	@Test
	public void findSMax() throws FileNotFoundException
	{
		ArrayList<DataHolderObject> temp;
		ReadableFile rf = new ReadableFile("data.txt");
		
		temp = rf.calculateCusum(rf.getListOfValues());
		
		double max = rf.findSMax(temp);
		//maximum value in cusum list 
		assertEquals(17.046, max, .001);
	}
	
	/**
	 * Testing finding the calculated difference between the largest and smallest value.
	 * @throws FileNotFoundException
	 */
	@Test
	public void testCalculateSDiff() throws FileNotFoundException
	{
		ReadableFile rf = new ReadableFile("data.txt");
		assertEquals(17.742, rf.calculateSDiff(rf.OriginalSMax,rf.OriginalSMin),.001);
		
	}
	
	/**
	 * Test if bootstrap method outputs an expected valued with given inputs.
	 * @throws FileNotFoundException
	 */
	@Test
	public void testBootstrap() throws FileNotFoundException
	{
		ReadableFile rf = new ReadableFile("data.txt");
		ArrayList<Double> answer = rf.bootstrap(rf.listOfValues, 1000, .9);
		
		ArrayList<Double> list = new ArrayList<Double>();
		list.add(10.0);
		 assertTrue(answer.equals(list));
		
	}
		

}
