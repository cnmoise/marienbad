package minMaxCross;

import java.io.*;
import java.util.Base64;
import java.util.Iterator;
import java.util.Vector;


public class claudetester {

   public static void main(String[] args){
         int[][] initialBoard = {
            
            {0,0,1,1},
            {0,0,0,1},
            {0,0,1,0},
            {0,1,0,1}
   			
            };
         //System.out.println(singleScount(initialBoard));
         //System.out.println(doubleScount(initialBoard));
         columnCount(initialBoard);
   	}

	/**
   `        {1},
   			{1,1},
   			{1,1,1}
   
   {1,1,0}
   If you want to test an initial node, do it here
     1's are stalks that have not been crossed
     0's are stalks that have been crossed
     beg 
   */

   //how many single strokes (ones which have not been crossed out) are on board
   /*
   001
   011
   101
   010
   
   returns 4
   */
   
      public static int singleScount(int[][] board)
      {
         int c = 0;
         boolean noLeft = true;
         boolean noRight = true;
         //assuming we don't change our board structure
         for(int i= 0; i < board.length; i++){
         
            System.out.print(" i" + i);
            for(int j= 0; j < board[i].length; j++){
            
               //System.out.print(" j" + j);
               //System.out.print(" c" + c);
               
               if(board[i][j] == 1){
                  
                  //to avoid out of bounds check
                  if(j>0){
                     //is the left neighbor a 0
                      //System.out.println(" Check left neigh ");
                     if(board[i][j-1] == 1)
                     {
                        noLeft = false;
                     }
                  }
                  //to avoid out of bounds check
                  if(j < board[i].length-1 && noLeft){
                            //is the right neighbor a 0
                            //System.out.println(" Check right neigh ");
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
            System.out.println();
            noLeft = true;
            noRight = true;
         }
         
       return c;
      }
      
      /*
    public static int doubleScount(int[][] board)
      {
         int c = 0;
         boolean onepartner = false;
      
         for(int i= 0; i < board.length; i++){
         
            System.out.print(" i" + i);
            for(int j= 0; j < board[i].length; j++){
            
               System.out.print(" j" + j);
               System.out.print(" c" + c);
               
               if(board[i][j] == 1){
                  
                  //to avoid out of bounds check
                  if(j < board[i].length-1){
                        System.out.println("enter loop: ");
                           
                            //is the right neighbor a 0
                           if(board[i][j+1] == 1 && !onepartner)
                           {
                           onepartner = true;
                           }
                           else if(board[i][j+1] == 1 && onepartner)
                           {
                           onepartner = false;
                           }
                           
                        }
                     
                     //stuff
                    
                     
                        System.out.println("onepartner: "+ onepartner);
            }
            System.out.println(); 
         }
         //wait til all the row's been checked to declare a double
         //^ is the exclusive or operator, 
         
            if(noLeft ^ noRight){
                     System.out.println("XOR noLeft: "+ noLeft);
                     System.out.println("XOR noRight: "+ noRight);
                     c++;
                     }
                  }
         
            noLeft = true;
            noRight = true;
      
      return c;

            }*/ 
           
           /*
           example:
           
           101
           001
           000
           111
           -
           returns
           
           213
           
           */
            
            
            
           public static void columnCount(int[][] board){
           
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
                  System.out.print(resultarr[l]);

                 }

          /* 
           //gives us the length of the last array
           int v = board.length-1;
           int b = board[v].length;
           //create an array to store # of 1's in every col
           int[] colc = new int[b];
           
           //System.out.println(" Board length" + b);
           //System.out.println(" Board length" + colc.length);
           
           for(int i= 0; i < board.length; i++){
         
            System.out.print(" i" + i);
            for(int j= 0; j < board[i].length; j++){
                  if(board[i][j] == 1){
                  System.out.print(" j" + j);
                     colc[j]+= 1;
                     System.out.print(" colval" + colc[j]);
                  }
                  else{
                     colc[j] = 0;
                  }
               }
            
            }
               //to test it
               System.out.print(" the array : ");
               for(int l= 0; l < colc.length; l++)
                 {
                  System.out.print(colc[l]);

                 }
                 
                 */
                 
                 
                 
                 
           }
        }//end of claudetester
  