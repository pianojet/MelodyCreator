import java.awt.Color;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Dimension;

public class TPanel extends JPanel
{
	private static final long serialVersionUID = 7407111350575002535L;

	//sets up the panel of texts from the given text line dimension and number of texts
	public void initText(Dimension d, int numTexts)
	{
		Dimension panelD = new Dimension(d.width, d.height*numTexts);
		GridLayout grid = new GridLayout(numTexts,1);
		this.setForeground(Color.LIGHT_GRAY);
		this.setBackground(Color.LIGHT_GRAY);
		this.setPreferredSize(panelD);
		this.setMinimumSize(panelD);
		this.setMaximumSize(panelD);
		this.setLayout(grid);
		this.setVisible(true);
		this.validate();
	}
}
