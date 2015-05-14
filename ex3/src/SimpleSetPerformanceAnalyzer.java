import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.LinkedList;

/**
 * An analyzer class to test for the performance section
 * @author guybrush
 */
public class SimpleSetPerformanceAnalyzer {
	public static void main (String[] args){
		long startTime;
		long endTime;
		
		String a = "hash Hash guitar piano violin DAST CLIDS Infi Hello Bye Test Algebra Boo DAST CLIDS Shalom Kushkush hello hi naal DAST CLIDS oink";
		String[] b = a.split(" ");
		OpenHashSet c = new OpenHashSet(b);
		System.out.println(c.delete("Boo"));
		System.out.println(c.delete("Infi"));
		System.out.println(c.delete("Hello"));
		System.out.println(c.size());
		System.out.println(c.capacity());
		System.out.println(c.delete("hello"));
		System.out.println(c.size());
		System.out.println(Arrays.toString(c.table));
		System.out.println(c.contains("hello"));
		
		
		/*
		String[] data1 = Ex3Utils.file2array("data1.txt");
		String[] data2 = Ex3Utils.file2array("data2.txt");
		
		// an array of the different Sets
		SimpleSet[] setsToCheck1 = { new OpenHashSet(), 
									new ChainedHashSet(),
									new CollectionFacadeSet(new TreeSet<String>()),
									new CollectionFacadeSet(new HashSet<String>()),
									new CollectionFacadeSet(new LinkedList<String>())};
		
		SimpleSet[] setsToCheck2 = { new OpenHashSet(), 
									new ChainedHashSet(),
									new CollectionFacadeSet(new TreeSet<String>()),
									new CollectionFacadeSet(new HashSet<String>()),
									new CollectionFacadeSet(new LinkedList<String>())};
		
		// test 1, add the words of data1 to the different Sets
		for(SimpleSet set: setsToCheck1){
			startTime = System.nanoTime();
			for(String word: data1)
				set.add(word);
			endTime = System.nanoTime();
			System.out.println(set.getClass()+" data1 task1 in Micro "+(endTime-startTime)/(float)1000000);
		}
		
		// test 3, check for a 'hi' in data 1
		for(SimpleSet set: setsToCheck1){
			startTime = System.nanoTime();
			set.contains("hi");
			endTime = System.nanoTime();
			System.out.println(set.getClass()+" data1 task3 in Nano "+(endTime-startTime));
		}
		
		// test 4, check for a '-13170890158' in data 1
		for(SimpleSet set: setsToCheck1){
			startTime = System.nanoTime();
			set.contains("-13170890158");
			endTime = System.nanoTime();
			System.out.println(set.getClass()+" data1 task4 in Nano "+(endTime-startTime));
		}
		
		
		// test 2, add the words of data2 to the different Sets
		for(SimpleSet set: setsToCheck2){
			startTime = System.nanoTime();
			for(String word: data2)
				set.add(word);
			endTime = System.nanoTime();
			System.out.println(set.getClass()+" data2 task2 in Micro "+(endTime-startTime)/(float)1000000);
		}
		
		// test 5, check for a '23' in data 1
		for(SimpleSet set: setsToCheck1){
			startTime = System.nanoTime();
			set.contains("23");
			endTime = System.nanoTime();
			System.out.println(set.getClass()+" data2 task5 in Nano "+(endTime-startTime));
		}
		
		// test 6, check for a 'hi' in data 1
		for(SimpleSet set: setsToCheck1){
			startTime = System.nanoTime();
			set.contains("hi");
			endTime = System.nanoTime();
			System.out.println(set.getClass()+" data2 task6 in Nano "+(endTime-startTime));
		}
*/
	}

}
