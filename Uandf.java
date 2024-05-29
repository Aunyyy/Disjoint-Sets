import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Uandf {

	private int[] parent;
    private int[] rank;
    private boolean finalize = false;
    private HashMap<Integer, Character> lettersHashMap = new HashMap<>();
    private HashMap<Character, Integer> letterSizeHashMap = new HashMap<>();
    private int[] letterSize;
	
	public Uandf(int n) {
		parent = new int[n];
		rank = new int[n];
		
		for (int i = 0; i < n; i++) {
            makeSet(i);
        }
	}
	
	public void makeSet(int i) {
		if(!finalize) {
			parent[i] = i;
			rank[i] = 0;
		}
	}
	
	public void union(int x, int y, Uandf dS) {
		if(!finalize) {
			link(findSet(x, dS), findSet(y, dS));
		}
	}
	
	public void link(int x, int y) {
		if (rank[x] > rank[y]) {
			parent[y] = x;
		}
		else if (rank[x] < rank[y]) {
			parent[x] = y;
		}
		else if (x != y)
		{
			parent[y] = x;
			rank[x] = rank[x] + 1;
		}
	}
	
	public static int findSet(int x, Uandf dS) {
		if (x != dS.parent[x]) {
			dS.parent[x] = findSet(dS.parent[x], dS);
		}
		return dS.parent[x];
	}
	
	public int finalSets(Uandf dS) {
		finalize = true;
		return lettersHashMap.size();
	}
	
	public static void assignLetters(int rows, int cols, Uandf dS)
	{
		//ASCII: a = 97
		int assignLetter = 97;
		//Assign letters to the roots of the disjoint sets.
		for (int i = 0; i < rows*cols; i++) {
			if (dS.parent[i] != -1 && !dS.lettersHashMap.containsKey(findSet(dS.parent[i], dS))) {
				dS.lettersHashMap.put(findSet(dS.parent[i], dS), (char) assignLetter);
				assignLetter++;
			}
		}
	}
	
	public static void printLetters(int rows, int cols, Uandf dS)
	{		
		for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
            	if (dS.parent[i * cols + j] == -1)
            	{
            		System.out.print(" ");
            	}
            	else {
                    System.out.print(dS.lettersHashMap.get(findSet(dS.parent[i * cols + j], dS)));
            	}
            }
            System.out.println();
        }
		
	}
	
	public static void printInputImage(int[][] grid, int rows, int cols) {
		for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
	}
	
	public static void formSets(int[][] grid, Uandf dS, int rows, int cols) {
		int[] dx = {0, 0, 1, -1, 1, -1, 1, -1};
        int[] dy = {1, -1, 0, 0, 1, -1, -1, 1};
        int x = 0, y = 0;
        
		for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
            	if (grid[i][j] == 1) {
            		for (int k = 0; k < dx.length; k++) {
            			x = i + dx[k];
                        y = j + dy[k];
                        if (x >= 0 && x < rows && y >= 0 && y < cols && grid[x][y] == 1) {
                        	dS.union(i * cols + j, x * cols + y, dS);
                        }
            		}
            	}
            	else {
            		dS.parent[i * cols + j] = -1;
            	}
            }
        }
	}
	
	public static void sortedComponentList(Uandf dS) {
		dS.letterSize =  new int[dS.lettersHashMap.size()];
		ArrayList<int[]> arrayList = new ArrayList<>();
		char c = 0;
		int assignLetter = 97;
		
		for (int i = 0; i < dS.parent.length; i++) {
			if (dS.parent[i] != -1) {
				c = dS.lettersHashMap.get(findSet(dS.parent[i], dS));
				dS.letterSize[((int) c) - 97] += 1;
			}
		}
		
		for (int i = 0; i < dS.letterSize.length; i++)
		{
			dS.letterSizeHashMap.put((char) (assignLetter + i), dS.letterSize[i]);
			arrayList.add(new int[]{dS.letterSize[i], i+97});
		}
		
		Collections.sort(arrayList, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return Integer.compare(a[0], b[0]);
            }
        });
		
		Arrays.sort(dS.letterSize);
		
		System.out.println("Components sorted by size: ");
		
		for (int i = arrayList.size()-1; i >= 0; i--)
		{

			System.out.println("Label: " + ((char) arrayList.get(i)[1]) + ", Size: " + arrayList.get(i)[0]);
		}
	}
	
	public static void greater2(int rows, int cols, Uandf dS) {
		char c = 0;
		for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
            	if (dS.parent[i * cols + j] == -1)
            	{
            		System.out.print(" ");
            	}
            	else {
                    c = dS.lettersHashMap.get(findSet(dS.parent[i * cols + j], dS));
                    if (dS.letterSizeHashMap.get(c) > 2)
                    {
                    	System.out.print(c);
                    }
            	}
            }
            System.out.println();
        }
	}
	
	public static void greater11(int rows, int cols, Uandf dS) {
		char c = 0;
		for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
            	if (dS.parent[i * cols + j] == -1)
            	{
            		System.out.print(" ");
            	}
            	else {
                    c = dS.lettersHashMap.get(findSet(dS.parent[i * cols + j], dS));
                    if (dS.letterSizeHashMap.get(c) > 11)
                    {
                    	System.out.print(c);
                    }
            	}
            }
            System.out.println();
        }
	}

	public static void main(String[] args) throws FileNotFoundException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(args[0]));
			String line = reader.readLine();
            String[] parts = line.split("");
            int rows = 1;
            int cols = parts.length;

            // Count the number of rows in the file
            while (reader.readLine() != null) {
                rows++;
            }
            
            reader.close();
            reader =  new BufferedReader(new FileReader(args[0]));
            
            int[][] grid = new int[rows][cols];
            
            for (int i = 0; i < rows; i++) {
                line = reader.readLine();
                String[] binaryChar = line.split("");
                for (int j = 0; j < cols; j++) {
                    grid[i][j] = binaryChar[j].equals("+") ? 1 : 0;
                }
            }
            // Close the file
            reader.close();
            
            //1
            System.out.println("Question 1:\n");
            printInputImage(grid, rows, cols);         
            
            //2
            System.out.println("\nQuestion 2:\n");
            Uandf disjointSet = new Uandf(rows * cols);
            formSets(grid, disjointSet, rows, cols);
            assignLetters(rows, cols, disjointSet);
            printLetters(rows, cols, disjointSet);            
            
            //3
            System.out.println("\nQuestion 3:\n");
            sortedComponentList(disjointSet);
            
            //4
            System.out.println("\nQuestion 4:\n");
            greater2(rows, cols, disjointSet);
            
            //5
            System.out.println("\nQuestion 5:\n");
            greater11(rows, cols, disjointSet);
            
		} 
		catch (IOException e) {
            e.printStackTrace();
        }
	}
}

