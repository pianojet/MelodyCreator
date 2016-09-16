import java.awt.Color;
import javax.swing.*;

import java.awt.event.*;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.border.BevelBorder;

/*
This class represents a keyboard drawn in a JPanel, made up of keyboard keys from
the MusicKeyboardKey class.
*/

public class MusicKeyboard extends JPanel
{
	private static final long serialVersionUID = -1103590389155020385L;
	static int ONE_WHOLESTEP = 2;
	static int ONE_TRICHORD = 3;
	static int ONE_TETRACHORD = 4;
	static int ONE_PENTACHORD = 5;
	static int ONE_SEXACHORD = 6;
	static int ONE_SEPTACHORD = 7;
	static int ONE_DIATONIC_SCALE = 8;
	static int TWO_DIATONIC_SCALES = 15;
	static int THREE_DIATONIC_SCALES = 22;
	static int ONE_CHROMATIC_SCALE = 12;
	static int ONE_PIANO = 88;
	static Integer[] MUSIC_KEYBOARD_SIZES = {ONE_PENTACHORD, ONE_DIATONIC_SCALE, TWO_DIATONIC_SCALES, THREE_DIATONIC_SCALES};
	
	protected int totalNumberOfKeys = 0;
	protected int totalNumberOfWhiteKeys = 0;
	protected int totalNumberOfBlackKeys = 0;
	protected int firstNote = -1;
	protected int tonality = 0;
	protected ArrayList<MusicKeyboardKey> Keys = new ArrayList<MusicKeyboardKey>();	
	protected MusicKeyboardKey keyJustPlayed = null;
	
	//keeps track of whether the constructor parameters had to be changed to make a
	//useable keyboard
	public boolean hadToBeFixed = false;
	
	//tests to see if the given first note and keyboard range can yield a valid,
	//playable keyboard according to the Note class
	static boolean isPlayableKeyboardSize(int firstn, int r)
	{
		if ((firstn < Note.C_1) || ((firstn+r-1) >= Note.ALL_NOTES.length) || (r < 1))
		{
			return(false);
		}
		else return(true);
	}
	
	//usually called in order to make sure all aspects of this keyboard yield a valid,
	//playable keyboard according to the Note class
	public void fixKeyboardFirstNote()
	{
		if (totalNumberOfKeys < 1) totalNumberOfKeys = 1;
		if (totalNumberOfKeys > Note.ALL_NOTES.length) totalNumberOfKeys = Note.ALL_NOTES.length;
		if (firstNote < Note.C_1) firstNote = Note.C_1;
		if (firstNote+totalNumberOfKeys > Note.ALL_NOTES.length) firstNote = Note.ALL_NOTES.length - totalNumberOfKeys;
		this.hadToBeFixed = true;
	}
	
	//constructs a keyboard starting with the given first note, range, and tonality.
	//tonality is not used yet, but allows for future implementation of black
	//keys and chromatic keyboards
	public MusicKeyboard(int n, int numkeys, int t)
	{
		firstNote = n;
		totalNumberOfKeys = numkeys;
		tonality = t;
	}
	
	//returns the list of keys present in this keyboard
	public ArrayList<MusicKeyboardKey> getKeyList()
	{
		return(Keys);
	}
	
	//returns the range, or size of this keyboard
	public int getKeyboardSize()
	{
		return(Keys.size());
	}
	
	//returns the name of the first note on a key in this keyboard
	public String getFirstNotePitch()
	{
		return(Keys.get(0).getNotePitch());
	}
	
	public int getFirstNote()
	{
		return(Keys.get(0).getNoteNumberInRange());
	}
	
	//initializes and builds the keyboard panel
	public void initMusicKeyboard()
	{
		this.hadToBeFixed = false;
		if (!isPlayableKeyboardSize(this.firstNote, totalNumberOfKeys)) fixKeyboardFirstNote();
		
//		Dimension maxKeyboardSize = this.getParent().getSize();

		ArrayList<MusicKeyboardKey> initKeys = new ArrayList<MusicKeyboardKey>(totalNumberOfKeys);
		Dimension kd = new Dimension();
		kd.width = totalNumberOfKeys*MusicKeyboardKey.KEYWIDTH;
		kd.height = MusicKeyboardKey.KEYHEIGHT+20;
		this.setPreferredSize(kd);
		this.setMinimumSize(kd);
		this.setBackground(Color.GRAY);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setVisible(true);
		
		//test to see if all of the keys are going to be natural, white keys
		if (this.tonality == Scale.DIATONIC)
		{	
			this.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
			for (int counter = 0; counter < this.totalNumberOfKeys; counter++)
			{
				initKeys.add(new MusicKeyboardKey(this.firstNote+counter, counter+1));
				initKeys.get(counter).initMusicKeyboardKey();
				initKeys.get(counter).setName(Integer.toString(counter));
				this.add(initKeys.get(counter));
				//if the added key won't show because the keyboard is too big for the 
				//screen, make it invisible to keep it from appearing below the keyboard
//				if ((initKeys.get(counter).noteNumberInKeyboard*MusicKeyboardKey.KEYWIDTH) > maxKeyboardSize.width+3)
//				{
//					initKeys.get(counter).setVisible(false);
//				}
			}			
		}
		else { }
		Keys = initKeys;
		this.addMouseListener(new MouseAdapter()
		{
			//when the mouse is clicked, the variable "keyJustPlayed" is set so that the
			//container can refer to it, and its beveled border is set to lowered to make it
			//appear pressed.  The container's mouse listener reads "keyJustPlayed"
			//when the mouse is released, and the beveled border eventually returns to raised.
			public void mousePressed(MouseEvent e)
			{
				try
				{
					keyJustPlayed = null;
					keyJustPlayed = (MusicKeyboardKey)getComponentAt(e.getX(), e.getY());
					keyJustPlayed.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				}
				catch (ClassCastException cce)
				{
					keyJustPlayed = null;
					System.out.println("Click a key on the keyboard please.");
					return;
				}
			}
		});
		this.validate();
	}
}
