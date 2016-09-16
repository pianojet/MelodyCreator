import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class SPanel extends JPanel
{
	private static final long serialVersionUID = -4799980736370669704L;

	public void initText(Dimension d, int numTexts)
	{
		Dimension panelD = new Dimension(d.width, d.height*numTexts);
		FlowLayout flow = new FlowLayout();
		this.setPreferredSize(panelD);
		this.setMinimumSize(panelD);
		this.setMaximumSize(panelD);
		this.setLayout(flow);
		this.setBackground(Color.LIGHT_GRAY);
		this.setForeground(Color.GRAY);
		this.setVisible(true);
		this.validate();
	}
}
