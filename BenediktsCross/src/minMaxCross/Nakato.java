package minMaxCross;

import java.io.*;
import java.util.Base64;
import java.util.Iterator;
import java.util.Vector;



@SuppressWarnings("unused")
public class Nakato {
//	int[][] initialBoard = {
//            {1,1,1},
//          {1,1,1}, 
//         {1,1,1},
//        {1,1,1},
//         {1,1}
//         };
	
	/**If you want to test an initial node, do it here
     1's are stalks that have not been crossed
     0's are stalks that have been crossed
     beg 
   */
	int[][] initialBoard = {
            
			{1},
			{1,1},
			{1,1,1}
			
         };
	
	public Vector<Node> openList = new Vector<Node>();

	
	
	
	/**<p>This is where the real min max comes to power, it is an recrusive method!!
	 * It will compute ALL succesors, no limit implemented at the moment<p>*/
    
    //recursive method that is our main AI
    //computes all posible paths
	public void computeAllSuccesors(Node father){ 
		for(int row = 0; row < father.board.length; row++){
			for(int beg = 0; beg < father.board[row].length; beg++){
				for(int end = 0; end < father.board[row].length; end++){
						//saves time in implementation, no need to calc duplicates
                  if(beg>end){
							continue;
						}

						Node kid = father.cross(row, beg, end);
						if(kid==null){
							continue;
						}
						
						father.kids.add(kid);
                  //how does this work?
                  //this is the base case
                  //starts to print the board after the goal is reached
						if(kid.goalReached()){
							kid.printBoard();
							kid.visited = true;
							continue;
						}
                  //enters the reccursive calls
						else{
							kid.printBoard();
							try {
								InputStream in = System.in;
								InputStreamReader isr = new InputStreamReader(in);
								BufferedReader bfr = new BufferedReader(isr);
//								bfr.readLine();  /**If you want to watch the processin detail,you could activate this*/
								openList.add(kid); //TODO: Provisorisch
								this.computeAllSuccesors(kid);
								
                        //Here we will apply the actual min max methods
                        //This is where our AI makes decisions
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
				}//test
			}
		}
	}
	
	public void max(Node n){  /**Will grab the highest scoring child*/
//		System.out.println("Max");
//
		int max = -200;
      //ternary operator, for each kid in n.kids
		for(Node kid : n.kids){
			if(kid.score>max){
				max=kid.score;
			}
		}
		n.score = max;
	}
	
	
	public void min(Node n){ /**Will grab the poorest scoring child*/
//		System.out.println("min");
//intialize to infinity
		int min = +200;
		for(Node kid : n.kids){
			if(kid.score<min){
				min=kid.score;
			}
		}
		n.score = min;
	}
	
	
	/**<p>Is not fully implemented. Dont use it yet<p>
   */
   //   Not needed

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
	
	
	
	
	public static void main(String[] args){
		Nakato n = new Nakato();
		Node iniNode = new Node(n.initialBoard, null);
		n.openList.add((iniNode));
		iniNode.printBoard();
		n.computeAllSuccesors(iniNode);
		n.max(iniNode);
		System.out.println("--------------------RESULTS--------------------");
		System.out.println("Input for the Min-Max Algorithm:");
		iniNode.printBoard();
		System.out.println();
		System.out.println("The initial Node will produce: "+(n.openList.size()-1)+" nodes!");

		System.out.println("Will Max or Min Win? -100 suggests: Min wins, 100 suggests: Max wins\r\nActual Score:" +iniNode.score);

	}
}



//System.out.println(iniNode.kids.get(0).turnMax);
//System.out.println("Vater: "+iniNode.kids.get(0));
//System.out.println(iniNode.kids.get(0).kids.get(0).turnMax);
//System.out.println("father"+iniNode.kids.get(0).kids.get(0).father);
//System.out.println(iniNode.kids.get(0).kids.get(0).kids.get(0).turnMax);
//System.out.println("These are all the possible Nodes, resulting from the beginning Node. \r\nThey might be not completely different though");
//for(Node ninOL : n.openList){
//	ninOL.printBoard();
//}


//System.out.println("First generation");
//for(Node kid : iniNode.kids){
//	System.out.println("Score: "+kid.score+kid.turnMax);
//	kid.printBoard();
//}
//