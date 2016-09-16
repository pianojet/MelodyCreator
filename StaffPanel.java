import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
//import java.awt.Color;
import javax.swing.*;
//import java.awt.Component;
//import java.awt.Container;
//import java.awt.event.*;
//import java.awt.Graphics;
//import javax.swing.JButton;
//import javax.swing.JComponent;
//import javax.swing.BorderFactory;
//import javax.swing.JFrame;
//import javax.swing.border.*;
//import javax.swing.BoxLayout;
//import java.util.ArrayList;
//import java.awt.ComponentOrientation;

//import javax.swing.JFrame;

public class StaffPanel extends JPanel
{
	private static final long serialVersionUID = 4885223702024147026L;
	protected Dimension staffPanelDimension = new Dimension(); 

	public void initStaff()
	{
		staffPanelDimension.width = 200;
		staffPanelDimension.height = 20;
		
		this.setPreferredSize(staffPanelDimension);
		this.setMinimumSize(staffPanelDimension);
		this.setMaximumSize(staffPanelDimension);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setBackground(Color.WHITE);
		this.addNotify();
		this.setVisible(true);
		this.validate();
	}
	public void paint(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.drawLine(10, 10, 30, 10);
	}
}
