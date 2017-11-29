package minMaxCross;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.Vector;

public class Node {
	int[][] board; //board will save the status of the new board
	public Vector<Node> kids = new Vector<Node>(); //saves the kids
	Node father;  //father Node 
	boolean turnMax;  //Is it the turn of max or of min when processing the succesors of THIS node
	boolean visited = false;  //important flag
	public int score = 0;  //-100 if min wins, 100 if max Wins
	
	
//	public void 
	
	
	public Node(int[][] board, Node father) {
		super();//? needed
		this.board = board;
		this.father = father;
		if (father==null){
			turnMax = true;
		}
		else{  //I had some bugs here, thats why it looks that complicated
			if(father.turnMax==true){
				turnMax = false;
			}
			if(father.turnMax==false){
				turnMax = true;
			}
		}
	}
	
	
	
	/**<p>The goal is reached, whenever there is nothing left to cross<p>*/
	public boolean goalReached(){  //Too complicated, a lot left too improve
		System.out.println();
		int count = 0; //How many 1s are still left
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if(board[i][j]==1){
					count++;
				}
			}
			System.out.println();
		}
      //describes win condition 
		if(count==0&&!turnMax){
//			System.out.println("Im in here");
			score = -100;
			visited = true;
			return true;
		}
		if(count==0&&turnMax){
//			System.out.println("I am in here2");
			score = 100;
			visited = true;
			return true;
		}
		return false;
	}
	
	/**Just simulates a crossing, in order to legitamize the action of crossing*/
	public boolean crossSimulation(int row, int beginning, int end) throws ArrayIndexOutOfBoundsException{  
		int[][] initBoard2 = hardCopyArray(board);
		for(int i = beginning; i <= end; i++){
			if(initBoard2[row][i]==0){
				return false;
			}
		}
		return true;
	}
	
	
	/**Here is the real crossing
   cross from beginning to the end
   */
	public Node cross(int row, int beginning, int end){
		try{
		if(this.crossSimulation(row, beginning, end)){
			@SuppressWarnings("static-access")
			int[][] newBoardNode = this.hardCopyArray(board);
			for(int i = beginning; i <= end; i++){
				newBoardNode[row][i] = 0;
			}
//			System.out.println();
			return new Node(newBoardNode, this);
		}
		else{
//			System.err.println("Out of bounds");
			return null;
		}
		}
		catch(ArrayIndexOutOfBoundsException e){
//			System.err.println("Out of bounds");
			return null;
		}
	}
	
	/**prints the board of THIS node*/
	public void printBoard(){ 
		System.out.println();
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**<p>THIS IS NOT MY METHOD<p>*/
	public static int[][] hardCopyArray(int[][] aSource) {
		String s = serialize(aSource);
		int[][] erg = deserialize(s);
		return erg;
	}
	
	
	/**<p>THIS IS NOT MY METHOD<p>*/
	public static String serialize(int[][] g) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(g);
			oos.close();
			String encoded = Base64.getEncoder().encodeToString(baos.toByteArray());
			baos.close();
			return encoded;
		} catch (IOException e) {
			System.out.println("o.GB.serialize(): Error");
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**<p>THIS IS NOT MY METHOD<p>*/
	public static int[][] deserialize(String s) {
		int[][] g = null;
		byte[] data = Base64.getDecoder().decode(s);
		try {
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();
			if (o instanceof int[][]) {
				g = (int[][]) o;
			}
		} catch (IOException e) {
			System.err.println("o.GB.deserialize(): IOException ");
			g = null;
		} catch (ClassNotFoundException e) {
			System.err.println("o.GB.deserialize(): IOException ");
			e.printStackTrace();
			g = null;
		}
		return g;
	}
	
	
	
	/**Fairly the same as printBoard()*/
	public String toString(){
		String erg="";
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				erg+=board[i][j];
			}
			erg+="\r\n";
		}
		return erg;
	}
	
	
}
