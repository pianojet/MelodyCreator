import javax.swing.*;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Color;

public class BPanel extends JPanel
{
	private static final long serialVersionUID = 8325485516253233529L;

	public void initButtons(Dimension d, int numButtons)
	{
		Dimension panelD = new Dimension(d.width, (d.height*numButtons-3));
		GridLayout grid = new GridLayout(numButtons,1);
		grid.setHgap(10);
		
		this.setBackground(Color.LIGHT_GRAY);
		this.setPreferredSize(panelD);
		this.setMinimumSize(panelD);
		this.setMaximumSize(panelD);
		this.setLayout(grid);
		this.setVisible(true);
		this.validate();
	}
}
