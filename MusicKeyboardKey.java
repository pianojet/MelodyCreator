import java.awt.Color;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.border.BevelBorder;
/*
This class extends the Note class and represents a Note object as a music keyboard key
*/
public class MusicKeyboardKey extends Note
{
	private static final long serialVersionUID = -8108134061337772196L;
	
	static int KEYWIDTH = 45;
	static int KEYHEIGHT = 150;
	
	protected int noteNumberInKeyboard = 1;
	
	public boolean isWhiteKey = true;
	
	//constructs a music keyboard key from the given note and reference
	//number for which keyboard key this note is meant to be
	public MusicKeyboardKey(int n, int k)
	{
		this.noteNumberInRange = n;
		this.noteNumberInKeyboard = k;
	}
	
	//builds up this note to be displayed on a music keyboard
	public void initMusicKeyboardKey()
	{
//		Rectangle maxKeyboardSize = ((JFrame)this.getParent().getParent()).getMaximizedBounds();
		this.initNote();
		Dimension d = new Dimension();
		d.width = KEYWIDTH;
		d.height = KEYHEIGHT;
		this.setPreferredSize(d);
		this.setMinimumSize(d);
		this.setMaximumSize(d);
		this.setBounds((this.noteNumberInKeyboard-1)*KEYWIDTH,0,KEYWIDTH,KEYHEIGHT);
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
//		if (this.)
		this.setVisible(true);
		this.validate();
	}

	//paints an appropriate box and key name for this note
	public void paint(Graphics boxGraphic)
	{
		int textSize = 17;
		int textXLocation = (MusicKeyboardKey.KEYWIDTH/2)-textSize+3;
		int textYLocation = MusicKeyboardKey.KEYHEIGHT-textSize;
		boxGraphic.setFont(new Font(Font.SERIF,Font.BOLD,textSize));
		if (this.isWhiteKey)
		{
			boxGraphic.setColor(Color.WHITE);
			boxGraphic.drawRect(0,0,this.getWidth(),this.getHeight());
			boxGraphic.fillRect(0,0,this.getWidth(),this.getHeight());
			boxGraphic.setColor(Color.black);
			boxGraphic.drawString(this.notePitch, textXLocation, textYLocation);
		}
		this.paintBorder(boxGraphic);
	}
}
