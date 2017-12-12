package minMaxCross;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class Node {
	int alpha;
	int beta;
	int value;
	
	
	//adjacent runtime optimization´
	int[] adjacent;
	
	
	int[][] board; //board will save the status of the new board
	public Vector<Node> kids = new Vector<Node>(); //safes the kids
	Node father;  //father Node 
	boolean turnMax;  //Is it the turn of max or of min when processing the succesors of THIS node
	boolean visited = false;  //important flag
	public int score = 0;  //-100 if min wins, 100 if max Wins
	public int generation;
	
	
	//Movement variables, The variables that made the movement to create this node
	public int row = -1;
	public int beg = -1;
	public int end = -1;
	

	public int heuristics(){
		if(this.getAmountOfAllStrokes()==this.singleScount()){
			//terminal node
			if(this.getAmountOfAllStrokes()%2==0){
				if(this.turnMax){
					return 100;
				}
				else{
					return -100;
				}
			}
			else{
				if(this.turnMax){
					return -100;
				}
				else{
					return 100;
				}
			}
		}
		if(this.doubles()==2&&this.getAmountOfAllStrokes()==4){
			if(this.turnMax){
				return -100;
			}
			else{
				return +100;
			}
			//Terminal Node
		}
		if(this.oneMultiServeralSingle()){
			//Terminal Node
			if(this.turnMax){
				return +100;
			}
			else{
				return -100;
			}
		}	
		int[] evenUneven = analyzeColumnCount();
		if(evenUneven[0]>0&&evenUneven[1]==0){
			return 100;
		}
		if(evenUneven[1]>0&&evenUneven[0]==0){
			return -100;
		}
		return 0;
	}
	
	
	public int[] analyzeColumnCount(){
		int evenCount = 0;
		int unevenCount = 0;
		int[] evenUneven = columnCountOfBinaryBoard();
		int[] resultArray = new int[2];
		for (int i = 0; i < evenUneven.length; i++) {
			if(evenUneven[i]==0){   //bug fix
				continue;
			}
			if(evenUneven[i]%2==0){
				evenCount++;
			}
			else{
				unevenCount++;
			}
		}
		resultArray[0] = evenCount;
		resultArray[1] = unevenCount;
		return resultArray;
	}
	
	
	public int[] columnCountOfBinaryBoard(){
		int[][] binaryTranslation = this.binaryTranslation();
		this.modifyArray(binaryTranslation);
		int[] columnCount = this.columnCount(binaryTranslation);
		for (int i = 0; i < columnCount.length; i++) {
			//System.out.print(columnCount[i]);
		}
		return columnCount;  // dummy
	}
	
	
	public int[][] binaryTranslation(){
		Vector<Integer> independent = this.countIndependentStrokes();
		Vector<String> translatedStrings = new Vector<String>();
		for (Integer integer : independent) {
			String binString = "";
			while(integer!=0){
				binString = (integer%2) + binString;
				integer = integer / 2;
			}
			translatedStrings.add(binString);
		}
		int[][] resultBoard = new int[translatedStrings.size()][3];
		for (int i = 0; i < independent.size(); i++) {
			//System.out.println(independent.get(i));
			String s = translatedStrings.get(i);
			while(s.length()!=3){
				s = "0"+s;
			}
			for (int j = 0; j < 3; j++) {
				resultBoard[i][j] = Integer.parseInt(s.substring(j,j+1));
			}
		}
		
		for (int i = 0; i < resultBoard.length; i++) {
			for (int j = 0; j < resultBoard[i].length; j++) {
				//System.out.print(resultBoard[i][j]);
			}
			//System.out.println();
		}
		
		return resultBoard;
	}
	
	
	
	
	
	public static void main(String[] args){
		/**Only for tets purposes*/
		int[][] board = {
	            
				{1},{1,1},{1,1,1},{1,1,1,1}
				
	         };
		Node n = new Node(board, null, 0);
		//System.out.println("Heuristic: "+n.heuristics());
	}
	
	
	
	
	
	public Vector<Integer> countIndependentStrokes() {
		// TODO count every independent adjacent stoke
		Vector<Integer> independent = new Vector<Integer>();
		for (int i = 0; i < board.length; i++) {
			int ongoing = 0;
			for (int j = 0; j < board[i].length; j++) {
				if(board[i][j]==0){
					if(ongoing>0){
						independent.add(ongoing);
						ongoing = 0;
					}
				}
				else{
					ongoing++;
				}
			}	
			if(ongoing>0){
				independent.add(ongoing);
				ongoing = 0;
			}
		}
		
		return independent;
	}


	public Node(int[][] board, Node father, int generation) {
		super();
		this.board = board;
//		this.alpha = alpha;
//		this.beta = beta;
		this.father = father;
		this.generation = generation;
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
	
	
	public int getAmountOfAllStrokes(){
		int strokes = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if(board[i][j]==1){
					strokes++;
				}
			}
		}
		return strokes;
	}
	
	
	
	public int[] columnCount(int[][] board){

        int maxLength = 0;
              for (int i = 0; i < board.length; i++) {
                  if(board[i].length>maxLength){
                      maxLength = board[i].length;
                  }
              }

        int[] resultarr= new int[maxLength];

        for(int i= 0; i < board.length; i++){
               for(int j= 0; j < board[i].length; j++){

                  if(board[i][j]==1){
                     resultarr[j]+=1;
                  }

               }

            }

        for(int l= 0; l < resultarr.length; l++)
              {
               ////System.out.print(resultarr[l]);

              }
        return resultarr;
	}
	
	public int[][] modifyArray(int[][] board){
		int maxLength = 0;
		for (int i = 0; i < board.length; i++) {
			if(board[i].length>maxLength){
				maxLength = board[i].length;
			}
		}
		
		int[][] resultBoard = new int[board.length][maxLength];
		
		for (int i = 0; i < board.length; i++) {
			for (int bY = board[i].length-1, rY = resultBoard[i].length-1 ; bY >= 0 && rY >= 0; bY--, rY--) {
				resultBoard[i][rY] = board[i][bY];
			}
		}
		////System.out.println();
		for(int i = 0; i < resultBoard.length; i++){
			for(int j = 0; j < resultBoard[i].length; j++){
				////System.out.print(resultBoard[i][j]);
			}
			////System.out.println();
		}
		return resultBoard;
	}
	
	
	
	
	
	
	/**return all double stocks that exists*/
	public int doubles(){
		int multisButNotDoubles=0;
		int doubles = 0;
		int single = 0;
		int ongoing = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if(board[i][j]==1){
					if(ongoing==0){
						single++;
					}
					if(ongoing==1){
						////System.out.println("Found a double");
						doubles++;
						single--;
					}
					if(ongoing==2){
						////System.out.println("minus double at "+i+j+"");
						doubles--;
						multisButNotDoubles++;
					}
					ongoing++;
				}
				else{
					ongoing = 0;
				}
			}
			ongoing = 0;
		}
//		//System.out.println("Single"+single+" Multi"+multi);
		return doubles;
		
	}
	
	

	
	
	
	/**returns true, if multi is one and there are n singles. With 0>=n>=infinity
	 * for everything else it returns false*/
	public boolean oneMultiServeralSingle(){
		int multi = 0;
		int single = 0;
		int ongoing = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if(board[i][j]==1){
					if(ongoing==1){
						multi++;
						single--;
						ongoing++;
					}
					if(ongoing==0){
						single++;
						ongoing++;
					}
				}
				else{
					ongoing = 0;
				}
			}
			ongoing = 0;
		}
		////System.out.println("Single"+single+" Multi"+multi);
		if(multi==1){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	/**return the number of single stocks on the board*/
	public int singleScount()
    {
       int c = 0;
       boolean noLeft = true;
       boolean noRight = true;
       //assuming we don't change our board structure
       for(int i= 0; i < board.length; i++){

          ////System.out.print(" i" + i);
          for(int j= 0; j < board[i].length; j++){

             //////System.out.print(" j" + j);
             //////System.out.print(" c" + c);

             if(board[i][j] == 1){

                //to avoid out of bounds check
                if(j>0){
                   //is the left neighbor a 0
                    ////System.out.println(" Check left neigh ");
                   if(board[i][j-1] == 1)
                   {
                      noLeft = false;
                   }
                }
                //to avoid out of bounds check
                if(j < board[i].length-1 && noLeft){
                          //is the right neighbor a 0
                          ////System.out.println(" Check right neigh ");
                         if(board[i][j+1] == 1)
                         {
                         noRight = false;
                         }

                      }
                if(noLeft && noRight){
                   c++;
                   }
                }
          }
          ////System.out.println();
          noLeft = true;
          noRight = true;
       }

     return c;
    }
	

	

	
	
	/**<p>The goal is reached, whenever there is nothing left to cross<p>*/
	public boolean goalReached(int searchTreeLevel){  
		////System.out.println();
		int count = 0; //How many 1s are still left
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if(board[i][j]==1){
					count++;
				}
			}
			////System.out.println();
		}
		
		boolean goalReached = false;
		
		if(count==0&&!turnMax){
//			//System.out.println("Im in here");
			score = -100;
			goalReached = true;
		}
		if(count==0&&turnMax){
//			//System.out.println("I am in here2");
			score = 100;
			goalReached = true;
		}
		if(generation>searchTreeLevel){
			score = this.heuristics();
			goalReached = true;
		}
		
		
		
		if (goalReached) {
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
	
	
	/**Here is the real crossing*/
	public Node cross(int row, int beginning, int end){
		try{
		if(this.crossSimulation(row, beginning, end)){
			@SuppressWarnings("static-access")
			int[][] newBoardNode = this.hardCopyArray(board);
			for(int i = beginning; i <= end; i++){
				newBoardNode[row][i] = 0;
			}
//			//System.out.println();
			return new Node(newBoardNode, this, (this.generation+1));  //alpha beta änderung
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
//		//System.out.println();
//		for(int i = 0; i < board.length; i++){
//			for(int j = 0; j < board[i].length; j++){
//				//System.out.print(board[i][j]);
//			}
//			//System.out.println();
//		}
//		//System.out.println();
	}
	public void printBoard2(){ 
		//System.out.println();
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				//System.out.print(board[i][j]);
			}
			//System.out.println();
		}
//		//System.out.println();
//		String s = this.turnMax ? "MAXNODE":"MINNODE";
//		//System.out.println(s);
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
			//System.out.println("o.GB.serialize(): Error");
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**<p>THIS IS NOT MY METHOD
	 * TODO: adding reference & @serializable()
	 * <p>*/
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


	public boolean alreadyExisiting() {
		if(this.father==null){
			return false;
		}
		List<Node> siblings = father.kids;
		for (Iterator iterator = siblings.iterator(); iterator.hasNext();) {
			Node node = (Node) iterator.next();
			int[] sibadja = node.adjacent;
			boolean isTheSame = true;
			for (int i = 0; i < sibadja.length; i++) {
				if(sibadja[i]!=adjacent[i]){
					isTheSame = false;
				}
			}
			if(isTheSame){
				System.out.println("Yeah, it is already exisintg");
				return true;
			}
		}
		return false; //dummy
	}


	public int[] makeAdjacentArray() {  //works,tested
		// TODO Auto-generated method stub
		int[] resultArray = new int[6];
		Vector<Integer> independent = this.countIndependentStrokes();
		for (Iterator iterator = independent.iterator(); iterator.hasNext();) {
			Integer integer = (Integer) iterator.next();
			resultArray[integer-1] += 1;
		}
		return resultArray;
	}
	
	
}
