//import java.awt.Color;
//import javax.swing.*;
//import java.awt.Component;
//import java.awt.Container;
//import java.awt.event.*;
//import java.awt.Graphics;
//import javax.swing.JButton;
//import javax.swing.JComponent;
//import javax.swing.BorderFactory;
import javax.swing.JFrame;
//import javax.swing.border.*;
//import javax.swing.BoxLayout;
//import java.util.ArrayList;
//import java.awt.ComponentOrientation;

public class Staff extends JFrame
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -37463748399848515L;
	protected int staffWindowWidth = 0;
	
	public Staff(int keynumber)
	{
		staffWindowWidth = keynumber*MusicKeyboardKey.KEYWIDTH+6;
	}
	
	public void initStaff()
	{
		StaffPanel trebleStaff = new StaffPanel();
		this.add(trebleStaff);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Treble Staff");
		this.setBounds(
				//(this.getTopLevelAncestor().getWidth()-thisNumberOfKeys*KeyboardKey.KEYWIDTH)/2
				0,
				MusicKeyboardKey.KEYHEIGHT+34,
				staffWindowWidth,
				300);
		this.setVisible(true);
		this.validate();
	}
}
