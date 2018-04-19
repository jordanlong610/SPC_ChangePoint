import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Runner 
{
	/**
	 * Runner for the Change Point project. Performs analysis on a text file of data, and prints points where changes were
	 * detected to the console.
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @author Jordan Long and Chris Roadcap
	 */
	
	public static void main(String[] args) throws FileNotFoundException
	{
		System.out.println("Please enter a the name of the file");
		Scanner scanner = new Scanner(System.in);
		String nameOfFile = scanner.nextLine();
		
		System.out.println("Please enter the number of bootstraps to be performed");
		int numBootstraps = scanner.nextInt();
	
		System.out.println("Please enter the desired confidence as a decimal");
		double confidenceLevel = scanner.nextDouble();
		scanner.close();
		
		ReadableFile rf = new ReadableFile(nameOfFile);
		ArrayList<Double> answer = rf.bootstrap(rf.listOfValues, numBootstraps, confidenceLevel);
		System.out.println("A change in the data set occurs at the following line(s) of the file " + answer );
		
	}		
}