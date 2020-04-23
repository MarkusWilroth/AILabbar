package pacman.entries.pacman;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacMan extends Controller<MOVE>
{
	private ArrayList<String> yesOrNo;
	private ArrayList<String> discreteDistance;
	private ArrayList<String> directions;
	private ArrayList<DataTuple> trainingData;
	private ArrayList<DataTuple> testData;
	private static HashMap<String, ArrayList<String>> attributes;
	//private static ArrayList<String> attributeList;
	
	public MyPacMan() {
		GenerateTestData();
		
		SetAttributes();
		
		/* ToDo:
		 * Generate testData
		 * Build Tree
		 * Generate Tree
		 */
		
		
	}
	private void SetAttributes() {
		yesOrNo = new ArrayList<String>();
		yesOrNo.add("YES");
		yesOrNo.add("NO");
		
		discreteDistance = new ArrayList<String>();
		discreteDistance.add("VERY_LOW");
		discreteDistance.add("LOW");
		discreteDistance.add("MEDIUM");
		discreteDistance.add("HIGH");
		discreteDistance.add("VERY_HIGH");
		discreteDistance.add("NONE");
		
		directions = new ArrayList<String>();
		directions.add("RIGHT");
		directions.add("LEFT");
		directions.add("UP");
		directions.add("DOWN");
		directions.add("NEUTRAL");
		
		attributes = new HashMap<String, ArrayList<String>>();
		attributes.put("isBlinkyEdible", yesOrNo);
		attributes.put("isInkyEdible", yesOrNo);
		attributes.put("isPinkyEdible", yesOrNo);
		attributes.put("isSueEdible", yesOrNo);
		attributes.put("blinkyDist", discreteDistance);
		attributes.put("inkyDist", discreteDistance);
		attributes.put("pinkyDist", discreteDistance);
		attributes.put("sueDist", discreteDistance);
		attributes.put("blinkyDir", directions);
		attributes.put("inkyDir", directions);
		attributes.put("pinkyDir", directions);
		attributes.put("sueDir", directions);
		
	}
	
	private void GenerateTestData() {
		DataTuple[] data = DataSaverLoader.LoadPacManData();
		trainingData = new ArrayList<DataTuple>();
		testData = new ArrayList<DataTuple>();
		ArrayList<DataTuple> allData = new ArrayList<DataTuple>(Arrays.asList(data));
		
		Random rand = new Random();
		double testDataSize = 0;
		double totalSize = allData.size();
		double proportion = testDataSize/totalSize;
		
		while (proportion < 0.33) { //Varf�r 0.33?
			int index = rand.nextInt(allData.size());
			testData.add(allData.get(index));
			allData.remove(index);
			testDataSize++;
			proportion = testDataSize/totalSize;
		}
		for(int i = 0; i < allData.size(); i++) {
			trainingData.add(allData.get(i));
		}
	}
	
	private void MakeTree() {
		//attributeList
	}
	
	private Node GenerateTree(ArrayList<DataTuple> dataTuples,ArrayList<String> attributeList) {
		Node N = new Node();	//(1) Create a node
		if (IsSameClass(dataTuples)) { //(2) If tuples in "Data Partition" are all of the same class, "Class C", then
			String label = dataTuples.get(0).DirectionChosen.toString(); 
			N.setLabel(label); //Set the label
			return N; //(3) Return N as a leaf node
		}
		if (attributeList.isEmpty()) { //(4) If attribute list is empty then
			String label = MajorityClass(dataTuples).toString(); //Set the label to the Majority class in D - Majority voting
			N.setLabel(label); 
			return N; //(5) return N as a leaf node
		}
		
		return N;
	}
	
	private boolean IsSameClass(ArrayList<DataTuple> dataTuples) {
		MOVE move = dataTuples.get(0).DirectionChosen;
		for (int i = 1; i < dataTuples.size(); i++) {
			if (dataTuples.get(i).DirectionChosen != move) {
				return false;
			}
		}
		return true;
	}
	
	public MOVE MajorityClass(ArrayList<DataTuple> D) {

		MOVE move = null;
		HashMap<MOVE, Integer> moves = new HashMap<MOVE, Integer>();
		moves.put(MOVE.UP, 0);
		moves.put(MOVE.DOWN, 0);
		moves.put(MOVE.RIGHT, 0);
		moves.put(MOVE.LEFT, 0);
		moves.put(MOVE.NEUTRAL, 0);

		for (int i = 0; i < D.size(); i++) {
			MOVE key = D.get(i).DirectionChosen;
			moves.put(key, (moves.get(key) + 1));
		}
		int maxValueInMoves = (Collections.max(moves.values()));
		for (Map.Entry<MOVE, Integer> entry : moves.entrySet()) {
			if (entry.getValue() == maxValueInMoves) {
				move = entry.getKey();
			}
		}
		return move;
	}
	
	private void AttributeSelection(HashMap<String, ArrayList<String>> allAttributes,ArrayList<DataTuple> data, ArrayList<String> attributeList) {
		//InfoAD = SplitInfo
		String splitAttribute = "";
		double bestSplitInfo = Double.MAX_VALUE;
		
		for (int i = 0; i < attributeList.size(); i++) {
			double splitInfo = 0;
			ArrayList<String> currentAttributeValue = allAttributes.get(attributeList.get(i));
			
			
			for (int j = 0; j < currentAttributeValue.size(); j++) { // YES NO
				int eachValueCounter = 0;
				ArrayList<DataTuple> subSet = new ArrayList<DataTuple>();
				// create subset for this value in subset
				for (DataTuple D : data) {
					if (D.getAttributeValue(attributeList.get(i)).equals(
							currentAttributeValue.get(j))) {
						eachValueCounter++;
						subSet.add(D);
					}
				}
				
				double up = 0, down = 0, right = 0, left = 0, neutral = 0;
				for (DataTuple D : subSet) {
					if (D.DirectionChosen == MOVE.UP) {
						up++;
					} else if (D.DirectionChosen == MOVE.DOWN) {
						down++;
					} else if (D.DirectionChosen == MOVE.RIGHT) {
						right++;
					} else if (D.DirectionChosen == MOVE.LEFT) {
						left++;
					} else {
						neutral++;						
					}
				}
				double T = eachValueCounter;
				if (T != 0.0) {
					splitInfo += (T / data.size()) * (
							- ((up / T)      * (log2(up/T)))
							- ((down / T)    * (log2(down/T)))
							- ((right / T)   * (log2(right/T))) 
							- ((left / T)    * (log2(left/T)))
							- ((neutral / T) * (log2(neutral/T)))
							);
				}
			}
		}
		
		
	}
	
	private double log2(double x) {
		double result = 0;
		return result;
	}
	
private MOVE myMove=MOVE.NEUTRAL;
	
	public MOVE getMove(Game game, long timeDue) 
	{
		//Place your game logic here to play the game as Ms Pac-Man
		
		return myMove;
	}
}