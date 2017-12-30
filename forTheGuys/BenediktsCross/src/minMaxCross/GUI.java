package minMaxCross;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GUI extends JFrame{
	
	
	JPanel center = new JPanel();
	
	JPanel minus = new JPanel();
	JButton[] minusButtons;
	JPanel plus = new JPanel();
	JButton[] plusButtons;
	JPanel main = new JPanel();
	JButton[][] boardButtons;
	
	//Settings
	JPanel settings = new JPanel();
	JButton initializeGame;
	JButton submitSelection;
	JButton cancelSelection;
	
	
	//selection
	int[] selection1 = {-1,-1};
	int[] selection2 = {-1,-1};
		
	
	public GUI(){
		this.basicSettings();
		this.initializeMinus();
		this.initializePlus();
		this.initializeMain();
		center.setLayout(new GridLayout(1, 3));
		center.add(minus);
		center.add(main);
		center.add(plus);
		settings.setLayout(new GridLayout(1, 3));
		this.initializeSettings();
		this.add(center, BorderLayout.CENTER);
		this.add(settings, BorderLayout.SOUTH);
		this.setVisible(true);
	}
	
	

	
	
	
	
	public void movement(int row, int beginning, int end){
		if(beginning>end){
			System.out.println("Beginning and end are not in the right order");
			int local = beginning;
			beginning = end;
			end = local;
		}
		for (int i = beginning; i <= end; i++) {
			System.out.println("Beginning and end are in the right order");
			boardButtons[row][i].setEnabled(false);
			boardButtons[row][i].setBackground(null);
			repaint();
		}
	}
	
	
	public void increase(int i){
		if(i<0||i>5){
			return;
		}
		int enabledButtons=0;
		for (int j = 0; j < boardButtons[i].length; j++) {
			if(boardButtons[i][j].isEnabled()){
				enabledButtons++;
			}
			else{
				break;
			}
		}
		if(enabledButtons>5){
			return;
		}
		boardButtons[i][enabledButtons].setEnabled(true);
	}
	
	
	public void decrease(int i){
		int enabledButtons=-1;
		if(i<0||i>5){
			return;
		}
		for (int j = 0; j < boardButtons[i].length; j++) {
			if(boardButtons[i][j].isEnabled()){
				enabledButtons++;
			}
		}
		if(enabledButtons<0){
			return;
		}
		boardButtons[i][enabledButtons].setEnabled(false);
	}
	
	
	public void enableAllMinusAndPlus(boolean enable){
		for (int i = 0; i < plusButtons.length; i++) {
			plusButtons[i].setEnabled(enable);
		}
		for (int i = 0; i < minusButtons.length; i++) {
			minusButtons[i].setEnabled(enable);
		}
	}
	
	private void initializeSettings() {
		// TODO Auto-generated method stub
		initializeGame = new JButton("Initialize Game");
		initializeGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				submitSelection.setEnabled(true);
				submitSelection.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						//if both are initialized
						if((!(selection1[0]==-1))&&(!(selection2[0]==-1))){
							System.out.println("Both are initialized");
							int row = selection1[0];
							int beginning = selection1[1];
							int end = selection2[1];
							movement(row, beginning, end);
							for (int i = 0; i < selection1.length; i++) {
								selection1[i] = -1;
								selection2[i] = -1;
							}
						}
						//if only one is initialized
						if((!(selection1[0]==-1))&&((selection2[0]==-1))){
							System.out.println("Only the first is initialized");
							int row = selection1[0];
							int beginning = selection1[1];
							int end = selection1[1];
							movement(row, beginning, end);
							for (int i = 0; i < selection1.length; i++) {
								selection1[i] = -1;
								selection2[i] = -1;
							}
						}
						
						//now make an array of this node, send it to Nakato to let the algorithm compute the next node
						//TODO: GO on here
						int[][] board = makeABoardOutOfGUI();
						Nakato n = new Nakato();
						Node iniNode = new Node(board, null, 0);
						n.openList.add((iniNode));
						System.out.println("I will start to compute all the Nodesand I will eventually bring you a succesor");
						n.applyAI(iniNode);
						n.max(iniNode);
						int row = n.suggestedSuccesor.row;
						int beg = n.suggestedSuccesor.beg;
						int end = n.suggestedSuccesor.end;
						System.out.println(row+" "+beg+" "+end);
						waitForMove(row,beg,end);
						//TESTING
					}

				});
				cancelSelection.setEnabled(true);
				initializeGame.setEnabled(false);
				enableAllMinusAndPlus(false);
				for (int i = 0; i < plusButtons.length; i++) {
					plusButtons[i].setBackground(new Color(165,214,167));
					minusButtons[i].setBackground(new Color(248,187,208));
				}
				repaint();
			}
		});
		submitSelection = new JButton("Submit Selection");
		cancelSelection = new JButton("Cancel Selection");
		submitSelection.setEnabled(false);
		cancelSelection.setEnabled(false);
		settings.add(initializeGame);
		settings.add(submitSelection);
		settings.add(cancelSelection);
	}

	
	public void waitForMove(int row, int beg, int end) {
		// TODO Auto-generated method stub
		RedThread rd = new RedThread(row, beg, end, this);
		rd.start();
	}

	public int[][] makeABoardOutOfGUI() {
		// TODO Auto-generated method stub
		int[][] board = new int[6][6];
		for (int i = 0; i < boardButtons.length; i++) {
			for (int j = 0; j < boardButtons[i].length; j++) {
				if(boardButtons[i][j].isEnabled()){
					board[i][j] = 1;
				}
				else{
					board[i][j] = 0;
				}
			}
		}
		return board;
	}
	
	
	private void initializeMain() {
		// TODO Auto-generated method stub
		main.setLayout(new GridLayout(6,6));
		boardButtons = new JButton[6][6];
		for (int i = 0; i < boardButtons.length; i++) {
			for (int j = 0; j < boardButtons[i].length; j++) {
				JButton jB = new JButton();
				if(j>i){
					jB.setEnabled(false);
				}
				boardButtons[i][j] = jB;
				jB.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if(initializeGame.isEnabled()){
							return;
						}
						// TODO Auto-generated method stub
						JButton jB =(JButton) e.getSource();
						int row = -1;
						int column = -1;
						for (int j2 = 0; j2 < boardButtons.length; j2++) {
							for (int j3 = 0; j3 < boardButtons[j2].length; j3++) {
								if(jB==boardButtons[j2][j3]){
									row = j2;
									column = j3;
								}
							}
						}
						//if selection1 is on default
						if(selection1[0] == -1){
							selection1[0] = row;
							selection1[1] = column;
							boardButtons[row][column].setBackground(Color.YELLOW);
							repaint();
							return;
						}
						//if both selections already made
						if((!(selection1[0]==-1))&&(!(selection2[0]==-1))){
							System.out.println("Both selections");
							for (int j2 = 0; j2 < boardButtons.length; j2++) {
								for (int j3 = 0; j3 < boardButtons.length; j3++) {
									boardButtons[j2][j3].setBackground(null);
									selection1[1]=-1;
									selection1[0]=-1;
									selection2[1]=-1;
									selection2[0]=-1;
								}
							}
							return;
						}
						//if first selection already made
						else{
							//if in the same row
							if(row==selection1[0]){
								//if the column is bigger than than the first selection
								if(column>selection1[1]){
									for (int j2 = selection1[1]; j2 <= column; j2++) {
										boardButtons[row][j2].setBackground(Color.yellow);
										selection2[0] = row;
										selection2[1] = column;
									}
								}
								//if the column is smaller than the first selection
								else{
									int local = selection1[1];
									selection1[1] = column;
									column = local;
									for (int j2 = selection1[1]; j2 <= column; j2++) {
										boardButtons[row][j2].setBackground(Color.yellow);
									}
									selection2[0] = row;
									selection2[1] = column;
								}
							}
							//if it is not in the same row
							else{
								for (int j2 = 0; j2 < boardButtons.length; j2++) {
									for (int j3 = 0; j3 < boardButtons.length; j3++) {
										boardButtons[j2][j3].setBackground(null);
										selection1[1]=-1;
										selection1[0]=-1;
										selection2[1]=-1;
										selection2[0]=-1;
									}
								}
							}
						}
					}
				});
			}
		}
		for (int i = 0; i < boardButtons.length; i++) {
			for (int j = 0; j < boardButtons.length; j++) {
				main.add(boardButtons[i][j]);
			}
		}
	}


	private void initializePlus() {
		plus.setLayout(new GridLayout(6,1));
		plusButtons = new JButton[6];
		// TODO Auto-generated method stub
		for (int i = 0; i < 6; i++) {
			JButton plusB = new JButton();
			ImageIcon ico = new ImageIcon("plus.png"); 
			ico.setImage(ico.getImage().getScaledInstance(60,60,Image.SCALE_DEFAULT)); 
	        plusB.setIcon(ico);    
	        plusB.setBackground(Color.GREEN);
	        plusB.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JButton jB = (JButton) e.getSource();
					for (int j = 0; j < plusButtons.length; j++) {
						if(plusButtons[j] == jB){
							increase(j);
							repaint();
						}
					}
				}
			});
	        plusButtons[i] = plusB;
		}
		for (int i = 0; i < plusButtons.length; i++) {
			plus.add(plusButtons[i]);
		}
	}


	private void initializeMinus() {
		minus.setLayout(new GridLayout(6,1));
		minusButtons = new JButton[6];
		// TODO Auto-generated method stub
		for (int i = 0; i < 6; i++) {
			JButton minusB = new JButton();
			ImageIcon ico = new ImageIcon("minus.png"); 
			ico.setImage(ico.getImage().getScaledInstance(60,60,Image.SCALE_DEFAULT)); 
	        minusB.setIcon(ico);
	        minusB.setBackground(Color.RED); 
	        minusB.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JButton jB = (JButton) e.getSource();
					for (int j = 0; j < minusButtons.length; j++) {
						if(minusButtons[j] == jB){
							decrease(j);
							repaint();
						}
					}
				}
			});
	        minusButtons[i] = minusB;
		}
		for (int i = 0; i < minusButtons.length; i++) {
			minus.add(minusButtons[i]);
		}
	}


	private void basicSettings() {
		// TODO Auto-generated method stub
		this.setTitle("Benedikts Cross");
		this.setSize(900, 750);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
	}


	public static void main(String[] args){
		System.out.println("Hello GUI");
		GUI gui = new GUI();
	}







	public JButton[][] getBoardButtons() {
		// TODO Auto-generated method stub
		return boardButtons;
	}
}
