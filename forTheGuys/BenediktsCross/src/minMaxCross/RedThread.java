package minMaxCross;

import java.awt.Color;

import javax.swing.JButton;

public class RedThread extends Thread{
	
	GUI gui;
	int row, beg, end;
	
	public RedThread(int row, int beg, int end, GUI gui){
		this.gui = gui;
		this.row = row;
		this.beg = beg;
		this.end = end;
		for (int i = beg; i <= end ; i++) {
			gui.getBoardButtons()[row][i].setBackground(Color.RED);
		}
	}
	
	
	public void run(){
		try {
			System.out.println("I am in the THREAAAAAAD");
			Thread.sleep(1000l);
			System.out.println("I am done with thread");
			gui.movement(row, beg, end);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
