package minMaxCross;

import java.awt.Adjustable;
import java.io.*;
import java.util.Base64;
import java.util.Iterator;
import java.util.Vector;



@SuppressWarnings("unused")
public class Nakato {
	
	
	
	
	public int searchTreeLevel = 20;
	
//	int[][] initialBoard = {
//            {1,1,1},
//          {1,1,1}, 
//         {1,1,1},
//        {1,1,1},
//         {1,1}
//         };
	
	/**If you want to test an initial node, do it here*/
	int[][] initialBoard = {
            
			{1},
			{1,1},
			{1,1,1},
			{1,1,1,1},
			{1,1,1,1}
			
			
         };
	
	public Vector<Node> openList = new Vector<Node>();

	
	
	
	public Nakato(int i) {
		// TODO Auto-generated constructor stub
		searchTreeLevel = i;
	}


	/**<p>This is where the real min max comes to power, it is an recrusive method!!
	 * It will compute ALL succesors, no limit implemented at the moment<p>*/
	public void applyAI(Node father){ 
		father.adjacent = father.makeAdjacentArray();
		father.printBoard2();
		for(int row = 0; row < father.board.length; row++){
			for(int beg = 0; beg < father.board[row].length; beg++){
				for(int end = 0; end < father.board[row].length; end++){
						if((beg>end)||father.board[row][beg]==0){
							continue;
						}
						boolean notPossible = false;
						int local = beg;
						for (int i = beg; i <= end; i++) {
							if(father.board[row][i]==0){
								notPossible = true;
								break;
							}
						}
						beg = local;
						if(notPossible){
							continue;
						}
						Node kid = father.cross(row, beg, end);
						if(kid==null){
							continue;
						}
						//adjacent test phase
						kid.adjacent = kid.makeAdjacentArray();
						for (int i = 0; i < kid.adjacent.length; i++) {
							System.out.print(kid.adjacent[i]+"   ");
						}
						System.out.println();
						if(kid.alreadyExisiting()){
							continue;
						}
						//adjacent test phase
						
						
						if(kid.generation==1){
							kid.row = row;
							kid.beg = beg;
							kid.end = end;
						}
						
						father.kids.add(kid);
						////System.out.println("This is the new Node");
						kid.printBoard();
						if(!kid.goalReached(searchTreeLevel)){
//							//System.out.println("The goal is not reached yet!");
						}
						if(kid.goalReached(searchTreeLevel)){
//							//System.out.println("Goal is reached!");
							kid.printBoard();
							kid.visited = true;
							continue;
						}
						else{
							
							try {
								InputStream in = System.in;
								InputStreamReader isr = new InputStreamReader(in);
								BufferedReader bfr = new BufferedReader(isr);
//								bfr.readLine();  /**If you want to watch the processin detail,you could activate this*/
								openList.add(kid); //TODO: Provisorisch
//								//System.out.println("I will call the recrusive function now!");
								this.applyAI(kid);
								//Here we will apply the actual min max methods
								if(kid.turnMax){
									max(kid);
								}
								else{
									min(kid);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
				}
			}
		}
	}
	
	
	public Node suggestedSuccesor = null;
	
	
	public void max(Node n){  /**Will grab the highest scoring child*/
//		//System.out.println("Max");
		int max = -200;
		for(Node kid : n.kids){
			if(kid.score>max){
				max=kid.score;
				suggestedSuccesor = kid;
			}
		}
		n.score = max;
	}
	
	
	public void min(Node n){ /**Will grab the poorest scoring child*/
//		//System.out.println("min");
		int min = +200;
		for(Node kid : n.kids){
			if(kid.score<min){
				min=kid.score;
				suggestedSuccesor = kid;
			}
		}
		n.score = min;
	}
	
	
	/**<p>Is not fully implemented. Dont use it yet<p>*/
	public void addToOL(Node n){
		for(Node n2 : openList){
			if(n.equals(n2)){
				return;
			}
			else{
				continue;
			}
		}
		openList.add(n);
	}
	
	
	public Nakato(){
		
	}
	
	public static void main(String[] args){
		Nakato n = new Nakato();
		Node iniNode = new Node(n.initialBoard, null, 0);
		n.openList.add((iniNode));
		iniNode.printBoard();
		System.out.println("I will start to compute all the methods");
		n.applyAI(iniNode);
		System.out.println("inein");
		n.max(iniNode);
		System.out.println("--------------------RESULTS--------------------");
		System.out.println("Input for the Min-Max Algorithm:");
		iniNode.printBoard();
		System.out.println();
		System.out.println("The initial Node will produce: "+(n.openList.size()-1)+" nodes!");

		System.out.println("Will Max or Min Win? -100 suggests: Min wins, 100 suggests: Max wins\r\nActual Score:" +iniNode.score);
		System.out.println("The suggested succesor is ");
		n.suggestedSuccesor.printBoard2();
		System.out.println("Size first generation:  "+iniNode.kids.size());
		for (Node kid : iniNode.kids) {
			for (int i = 0; i < kid.adjacent.length; i++) {
				System.out.print(kid.adjacent[i]+"   ");
			}
			System.out.println();
		}
//		for (int i = 0; i < 15; i++) {
//			Nakato nak = new Nakato(i);
//			Node iniNode2 = new Node(nak.initialBoard, null, 0);
//			nak.openList.add((iniNode2));
//			nak.applyAI(iniNode2);
//			nak.max(iniNode2);
//			//System.out.print("Searchtreelevel: "+i);
//			//System.out.println("The initial Node will produce: "+(nak.openList.size()-1)+" nodes!");
//		}
	}
}



