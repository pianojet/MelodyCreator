import java.awt.Color;
import javax.swing.JFrame;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import java.awt.Event;
import java.awt.event.MouseAdapter;
import java.awt.Rectangle;


/* 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */

public class MelodyCreator extends JFrame
{
	private static final long serialVersionUID = -2285689380380400353L;
	protected static final String currentVersionUID = "v1.3"; 

	protected MusicKeyboard getKeyFromKeyboard = null;
	protected Melody userMelody = new Melody();
	protected Melody compMelody = new Melody();
	protected Melody savedUserMelody = null;
	protected Melody savedCompMelody = null;
	protected Melody favUserMelody = null;
	protected Melody favCompMelody = null;
	protected Melody totalMelody = new Melody();
	protected JTextArea userMelodyOutput = new JTextArea(1,1);
	protected JTextArea compMelodyOutput = new JTextArea(1,1);
	protected JTextArea playedOutput = new JTextArea(1,1);
	protected JTextArea statsOutput = new JTextArea(1,1);
	protected JTextArea whichGeneratorOutput = new JTextArea(1,1);
	
	protected int generatorMethod = -1;
	protected String generatorMethodText = "Generator Method:   Complete Automation";
	
	protected int maxMelodySize = 9;
	protected int firstKey = Note.C_3;
	protected int keyboardRange = MusicKeyboard.TWO_DIATONIC_SCALES;
	
	protected Rectangle maxRect = new Rectangle();
	
	protected JMenuBar MCMenuBar = new JMenuBar();
	
	//Once the mouse key is pressed, the music keyboard figures out which
	//keyboard key was pressed.
	//When the mouse is released, this adapter processes it and eventually
	//the beveled border is returned to raised
	protected MouseAdapter mainMA = new MouseAdapter()
	{
		public void mouseReleased(MouseEvent e)
		{
			Note tempNote = (Note) getKeyFromKeyboard.keyJustPlayed;
			if (tempNote != null)
			{
				appendAndTest(tempNote);
			} 
			else
			{
				playedOutput.setText("  Make sure you click on the keyboard to play a note.");
			}
		}
	};
	
	//this listener drives actions of all menu items and buttons
	ActionListener printListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent event) 
		{
			String actStr = event.getActionCommand();
			if (actStr == "Restart Melody")
			{
				Object[] options = { "OK", "CANCEL" };
				int selectedValue = JOptionPane.showOptionDialog(null, "Click OK to start over...", "Restart Melody?", 
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
				null, options, options[0]);
				if (selectedValue == JOptionPane.OK_OPTION)
				{
					mainFrameDisposeAndRebuild();
				}
	    		return;
			}
			if (actStr == "Reuse Melody")
			{
				if (((userMelody.notesInMelody == maxMelodySize) || (userMelody.notesInMelody == 0))
					&& (savedUserMelody != null))
				{
					playedOutput.setText("  Reusing previous user melody...");
					resetUMText();
					copySavedToUser();
					for (int counter = 0; counter < userMelody.notesInMelody; counter++)
					{
						if (counter > 0) userMelodyOutput.append(",");
						userMelodyOutput.append("  "+userMelody.getNoteInPlace(counter).notePitch);
						update(getGraphics());
						userMelody.getNoteInPlace(counter).playNote();
					}
					compsTurnAndRestart();
				} 
				else
				{
					playedOutput.setText("  Please finish your melody before reusing it.");
				}
				return;
			}
			if (actStr == "Replay Set")
			{
				if (((userMelody.notesInMelody == maxMelodySize) || (userMelody.notesInMelody == 0))
					&& (savedUserMelody !=null))
				{
					playedOutput.setText("  Replaying previous set...");
					replaySet(savedUserMelody,savedCompMelody);
				} 
				else
				{
					playedOutput.setText("  Please finish this set before replaying it.");
					update(getGraphics());
				}
				return;
			}
			if (actStr == "Play Saved Set")
			{
				if (favUserMelody != null)
				{
					if ((userMelody.notesInMelody == maxMelodySize) || (userMelody.notesInMelody == 0))
					{
						playedOutput.setText("  Playing saved set...");
						replaySet(favUserMelody,favCompMelody);
					} 
					else
					{
						playedOutput.setText("  Please finish this set before playing another.");
						update(getGraphics());
					}
				}
				return;
			}
			if (actStr == "Save Set")
			{
				if (((userMelody.notesInMelody == maxMelodySize) || (userMelody.notesInMelody == 0))
					&& (savedUserMelody != null && savedCompMelody != null))
				{
					informSavedSet();
					favUserMelody = savedUserMelody;
					favCompMelody = savedCompMelody;
				} 
				else
				{
					playedOutput.setText("  Please finish this set before saving it.");
					update(getGraphics());
				}
				return;
			}
			if (actStr == "Change Keyboard's First Note")
			{
				Object[] possibleValues = Note.ALL_NOTES;
				Object selectedValue = JOptionPane.showInputDialog(null, 
				"Select New First Key:", "Input",
				JOptionPane.INFORMATION_MESSAGE, null,
				possibleValues, possibleValues[0]);
				if (selectedValue != null)
				{
					firstKey = Note.getNoteNumberInRange((String)selectedValue);
					remove(getKeyFromKeyboard);
					makeKB();
					add(getKeyFromKeyboard, BorderLayout.NORTH);
					pack();
					resetSMText();
					update(getGraphics());
				}
	    		return;	    
			}
			if (actStr == "Change Keyboard's Range")
			{
				Object[] possibleValues = MusicKeyboard.MUSIC_KEYBOARD_SIZES;
				Object selectedValue = JOptionPane.showInputDialog(null, 
				"Select Number Of Keys:", "Input",
				JOptionPane.INFORMATION_MESSAGE, null,
				possibleValues, possibleValues[0]);
				if (selectedValue != null)
				{
					keyboardRange = (Integer)selectedValue;
					remove(getKeyFromKeyboard);
					makeKB();
					add(getKeyFromKeyboard, BorderLayout.NORTH);
					pack();
					resetSMText();
					update(getGraphics());
				}
	    		return;
			}
			if (actStr == "Change Melody Size")
			{			
				JOptionPane pane = new JOptionPane("Change Melody Size");
				JSlider slid = getSlider(pane);
				slid.setPaintLabels(true);
				slid.createStandardLabels(4, 5);
				slid.setMaximum(13);
				slid.setMinimum(5);
				slid.setValue(9);
				slid.setMajorTickSpacing(4);
				slid.setMinorTickSpacing(4);
				slid.setSnapToTicks(true);
				Object msg[] = {"You will start over after resizing the melody.", "Selected a new melody size:", slid};
				pane.setMessage(msg);
				pane.setMessageType(JOptionPane.QUESTION_MESSAGE);
				pane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
				JDialog dialog = pane.createDialog(pane, "New Melody Size");
				dialog.setVisible(true);
				Object inp = pane.getInputValue();
				Object val = pane.getValue();

				if (val == null) 
				{
					System.out.println("Closed");
				} 
				else 
				{
					if ((Integer)val == JOptionPane.CLOSED_OPTION) 
					{
						System.out.println("Closed");
					} 
					else 
						if ((Integer)val == JOptionPane.OK_OPTION) 
						{
							maxMelodySize = (Integer)inp;
							mainFrameDisposeAndRebuild();
							return;
						} 
						else 
							if ((Integer)val == JOptionPane.CANCEL_OPTION) 
							{
								System.out.println("Cancelled");
							}
				}
				return;
			}
			if (actStr == "Quit")
			{
				confirmExit();
			}
			if (actStr == "Complete Automation")
			{
				generatorMethod = MelodyGenerator.AUTOMATED;
				updateGMText(actStr);
	    		return;	    
			}
			if (actStr == "Copy Melody")
			{
				generatorMethod = MelodyGenerator.COPY_MELODY;
				updateGMText(actStr);
	    		return;	    
			}
			if (actStr == "Horizontal Axis Flip")
			{
				generatorMethod = MelodyGenerator.HORIZONTAL_AXIS_FLIP;
				updateGMText(actStr);
	    		return;	    
			}
			if (actStr == "Vertical Axis Flip")
			{
				generatorMethod = MelodyGenerator.VERTICAL_AXIS_FLIP;
				updateGMText(actStr);
	    		return;	    
			}
			if (actStr == "Simple Harmony Flop")
			{
				generatorMethod = MelodyGenerator.SIMPLE_HARMONY_FLOP;
				updateGMText(actStr);
	    		return;	    
			}
			if (actStr == "Lowered Sequence")
			{
				generatorMethod = MelodyGenerator.LOWERED_SEQUENCE;
				updateGMText(actStr);
	    		return;	    
			}
			if (actStr == "Raised Sequence")
			{
				generatorMethod = MelodyGenerator.RAISED_SEQUENCE;
				updateGMText(actStr);
	    		return;	    
			}
			if (actStr == "Resolve Last Note")
			{
				generatorMethod = MelodyGenerator.RESOLVE_LAST_NOTE;
				updateGMText(actStr);
	    		return;	    
			}
			if (actStr == "Simple Consequent Part-Echo")
			{
				generatorMethod = MelodyGenerator.SIMPLE_CONSEQUENT_PARTECHO;
				updateGMText(actStr);
	    		return;	    
			}
			if (actStr == "Consequent Conjunct Response")
			{
				generatorMethod = MelodyGenerator.CONSEQUENT_CONJUNCT_RESPONSE;
				updateGMText(actStr);
	    		return;	    
			}
			if (actStr == "Consequent Disjunct Response")
			{
				generatorMethod = MelodyGenerator.CONSEQUENT_DISJUNCT_RESPONSE;
				updateGMText(actStr);
	    		return;	    
			}
			if (actStr == "Multi-Sequence Response")
			{
				generatorMethod = MelodyGenerator.MULTI_SEQUENCE_RESPONSE;
				updateGMText(actStr);
	    		return;	    
			}
			if (actStr == "Random Cacophony")
			{
				generatorMethod = MelodyGenerator.RANDOM_CACOPHONY;
				updateGMText(actStr);
	    		return;	    
			}
			if (actStr == "Version Info")
			{
				try
				{
					URL urlAboutText = Note.class.getResource("update.rtf");
					JEditorPane at = new JEditorPane(urlAboutText);
					at.setMaximumSize(new Dimension(880,400));
					at.setMinimumSize(new Dimension(880,400));
					at.setPreferredSize(new Dimension(880,400));
					at.setEditable(false);
					JScrollPane versionScroller = new JScrollPane(at);
					JOptionPane.showMessageDialog(null, versionScroller, "Fixes for "+currentVersionUID, JOptionPane.INFORMATION_MESSAGE);					}
				catch (Exception e)
				{
					e.printStackTrace();
				}
	    		return;
			}
			if (actStr == "About")
			{
				try
				{
					URL urlAboutText = Note.class.getResource("abouttext.rtf");
					JEditorPane at = new JEditorPane(urlAboutText);
					at.setBorder(BorderFactory.createLineBorder(Color.black));
					at.setEditable(false);
					JOptionPane.showMessageDialog(null, at, "About Melody Creator "+currentVersionUID, JOptionPane.INFORMATION_MESSAGE);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
	    		return;
			}
			if (actStr == "Musical Terms")
			{
				try
				{
					URL urlAboutText = Note.class.getResource("musicterms.rtf");
					JEditorPane at = new JEditorPane(urlAboutText);
					at.setMaximumSize(new Dimension(880,400));
					at.setMinimumSize(new Dimension(880,400));
					at.setPreferredSize(new Dimension(880,400));
					at.setEditable(false);
					JScrollPane termsScroller = new JScrollPane(at);
					JOptionPane.showMessageDialog(null, termsScroller, "Musical Terms", JOptionPane.INFORMATION_MESSAGE);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
	    		return;
			}
			if (actStr == "How do I play this thing?")
			{
				try
				{
					URL urlAboutText = Note.class.getResource("directions.rtf");
					JEditorPane at = new JEditorPane(urlAboutText);
					at.setMaximumSize(new Dimension(880,400));
					at.setMinimumSize(new Dimension(880,400));
					at.setPreferredSize(new Dimension(880,400));
					at.setEditable(false);
					JScrollPane directionScroller = new JScrollPane(at);
					JOptionPane.showMessageDialog(null, directionScroller, "Directions...", JOptionPane.INFORMATION_MESSAGE);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
	    		return;
			}
	    }
	};
	
	
	//This is the slider used for the user to select a new melody size.
	public static JSlider getSlider (final JOptionPane p)
	{
		JSlider slider = new JSlider();
		slider.setMajorTickSpacing (33);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
	    ChangeListener changeListener = new ChangeListener() 
	    {
	    	public void stateChanged(ChangeEvent changeEvent) 
	    	{
	    		JSlider theSlider = (JSlider)changeEvent.getSource();
	    		if (!theSlider.getValueIsAdjusting()) 
	    		{
	    			Integer n = new Integer(theSlider.getValue());
	    			p.setInputValue(n);
	    		}
	    	}
	    };
	    slider.addChangeListener(changeListener);
	    return(slider);
	}
	
	//This creates the entire menu bar.
	public void initMCMenuBar()
	{
		String[] optionsItems = new String[] { 
			"Restart Melody", "Reuse Melody", "Replay Set", "Save Set", "Play Saved Set", 
			"Change Keyboard's First Note", "Change Keyboard's Range", "Change Melody Size", "Quit" };
		String[] generatorItems = new String[] {
			"Complete Automation", "Copy Melody", "Horizontal Axis Flip", "Vertical Axis Flip", 
			"Simple Harmony Flop", "Lowered Sequence", "Raised Sequence", "Resolve Last Note", 
			"Simple Consequent Part-Echo", "Consequent Conjunct Response", "Consequent Disjunct Response",
			"Multi-Sequence Response", "Random Cacophony"};
		//String[] helpItems = new String[] { "How do I play this thing?", "What does 'C_3' mean?", "About" };
		char[] optionsShortcuts = {'R','M','P','S','A','F','K','I','Q' };
		char[] generatorShortcuts = {'T', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B','C'};
		//char[] helpShortcuts = { 'H', 'W', 'A' };

		JMenu optionsMenu = new JMenu("Settings");
		JMenu generatorMenu = new JMenu("Generator");
		JMenu helpMenu = new JMenu("Help");
		
		//JMenu howToMenu = new JMenu("How do I play this thing?");
		JMenu whatDoesMenu = new JMenu("What does 'C_3' mean?");
		JMenu aboutMenu = new JMenu("About");
		

		JMenuItem item;
		
		//quick loop to build up the options menu...
		for (int i=0; i < optionsItems.length; i++)
		{
			item = new JMenuItem(optionsItems[i]);
		    item.setAccelerator(KeyStroke.getKeyStroke(optionsShortcuts[i],
		    		Event.CTRL_MASK, false));
		    item.addActionListener(printListener);
		    optionsMenu.add(item);
		}
		
		//optionsMenu.getComponent(4).setEnabled(false);
		optionsMenu.getItem(4).setEnabled(false);
		
		//quick loop to build up the generator menu...
		ButtonGroup generatorGroup = new ButtonGroup();
		for (int i=0; i < generatorItems.length; i++)
		{
			item = new JRadioButtonMenuItem(generatorItems[i]);
		    item.setAccelerator(KeyStroke.getKeyStroke(generatorShortcuts[i],
		    		Event.ALT_MASK, false));
		    item.addActionListener(printListener);
		    if (i==0) item.setSelected(true);
		    generatorGroup.add(item);
		    generatorMenu.add(item);
		}
	    aboutMenu.add(item = new JMenuItem("Version Info"));
	    item.addActionListener(printListener);
		aboutMenu.add(item = new JMenuItem("About"));
	    item.addActionListener(printListener);
	    
	    JMenuItem howToMenu = new JMenuItem("How do I play this thing?");
	    howToMenu.setAccelerator(KeyStroke.getKeyStroke('H',Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));
	    howToMenu.addActionListener(printListener);
	    
	    JMenuItem termsMenu = new JMenuItem("Musical Terms");
	    termsMenu.setAccelerator(KeyStroke.getKeyStroke('T',Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));
	    termsMenu.addActionListener(printListener);
	    
	    URL urlNotePitchHelp = Note.class.getResource("notepitchhelp.JPG");
	    ImageIcon img = new ImageIcon(urlNotePitchHelp);
	    item = new JMenuItem(img);
	    whatDoesMenu.add(item);
	    
		optionsMenu.insertSeparator(2);
	    optionsMenu.insertSeparator(6);
	    optionsMenu.insertSeparator(10);
	    
	    generatorMenu.insertSeparator(1);
	    generatorMenu.insertSeparator(8);
	    generatorMenu.insertSeparator(14);
	    
	    helpMenu.add(howToMenu);
	    helpMenu.add(termsMenu);
	    helpMenu.add(whatDoesMenu);
	    helpMenu.add(aboutMenu);

	    MCMenuBar.add(optionsMenu);
	    MCMenuBar.add(generatorMenu);
	    MCMenuBar.add(helpMenu);

		this.setJMenuBar(MCMenuBar);
	}
	
	//the "resetXText" functions are raw initializers for the individual text lines
	public void resetUMText()
	{
		userMelodyOutput.setForeground(Color.BLACK);
		userMelodyOutput.setBackground(Color.LIGHT_GRAY);
		userMelodyOutput.setFont(new Font(Font.SERIF, Font.BOLD, 16));
		userMelodyOutput.setText("  User melody: ");
	}
	public void resetPMText()
	{
		playedOutput.setForeground(Color.BLUE);
		playedOutput.setBackground(Color.LIGHT_GRAY);
		playedOutput.setFont(new Font(Font.SERIF, Font.BOLD, 16));
		playedOutput.setText("  Play a note on the keyboard: ");
	}
	public void resetCMText()
	{
		compMelodyOutput.setForeground(Color.BLACK);
		compMelodyOutput.setBackground(Color.LIGHT_GRAY);
		compMelodyOutput.setFont(new Font(Font.SERIF, Font.BOLD, 16));
		compMelodyOutput.setText("  Comp melody: ");
	}
	public void resetGMText()
	{
		whichGeneratorOutput.setForeground(Color.BLACK);
		whichGeneratorOutput.setBackground(Color.LIGHT_GRAY);
		whichGeneratorOutput.setFont(new Font(Font.SERIF, Font.ITALIC, 16));
		whichGeneratorOutput.setText("  "+generatorMethodText);
	}
	public void resetSMText()
	{
		statsOutput.setForeground(Color.BLACK);
		statsOutput.setBackground(Color.LIGHT_GRAY);
		statsOutput.setFont(new Font(Font.SERIF, Font.BOLD, 16));
		statsOutput.setText("Melody Note Size:   "+maxMelodySize+",   First Keyboard Note:   "+Note.ALL_NOTES[firstKey]+",   Keyboard Range:   "+keyboardRange+" notes.   Tonality: C MAJOR DIATONIC");
	}

	//called whenever the generator method is changed to update and display text
	public void updateGMText(String str)
	{
		generatorMethodText = "Generator Method:   "+str;
		System.out.println("New Generator Method selected: "+str);
		resetGMText();
		update(getGraphics());
	}
	
	//This initializes common settings shared by all text lines.
	public void initTextComponent(JTextArea t, int w)
	{
		Dimension d = new Dimension();
		d.width = w;
		d.height = 20;
		t.setPreferredSize(d);
		t.setMinimumSize(d);
		t.setMaximumSize(d);
		t.setEditable(false);
		t.setBackground(Color.LIGHT_GRAY);
		t.setVisible(true);
		t.validate();
		t.setWrapStyleWord(true);
	}
	
	//initializes common settings shared by all buttons
	public void initButtonComponent(JButton b, Dimension d)
	{
		b.setPreferredSize(d);
		b.setMinimumSize(d);
		b.setMaximumSize(d);
		b.setVisible(true);
		b.validate();
		b.addActionListener(printListener);
	}

	//Calls initializing methods of texts and adds them.
	public void initTextAreas()
	{

		//text dimensions:
		Dimension td = new Dimension(800, 40);

		//stat text bar (SOUTH panel) dimensions:
		Dimension sd = new Dimension(800, 40);

		TPanel textPanel = new TPanel();
		SPanel statPanel = new SPanel(); 
		
		initTextComponent(userMelodyOutput, td.width);
		initTextComponent(compMelodyOutput, td.width);
		initTextComponent(playedOutput, td.width);
		initTextComponent(whichGeneratorOutput, td.width);
		initTextComponent(statsOutput, sd.width);

		textPanel.initText(td, 4);
		statPanel.initText(sd, 1);
		
		textPanel.add(userMelodyOutput);
		textPanel.add(playedOutput);
		textPanel.add(compMelodyOutput);
		textPanel.add(whichGeneratorOutput);
		statPanel.add(statsOutput);
		this.add(textPanel, BorderLayout.CENTER);
		this.add(statPanel, BorderLayout.SOUTH);

	}
	
	//instantiates and adds buttons
	public void initButtonMenu()
	{
		//button dimensions:
		Dimension bd = new Dimension(200, 30);
		
		BPanel buttPanel = new BPanel();
		
		JButton button1 = new JButton("Restart Melody");
		button1.setEnabled(true);
		button1.setActionCommand("Restart Melody");

		
		JButton button2 = new JButton("Reuse Melody");
		button2.setEnabled(true);
		button2.setActionCommand("Reuse Melody");
		
		JButton button3 = new JButton("Replay Set");
		button3.setEnabled(true);
		button3.setActionCommand("Replay Set");
		
		JButton button4 = new JButton("Save Set");
		button4.setEnabled(true);
		button4.setActionCommand("Save Set");

		JButton button5 = new JButton("Play Saved Set");
		if (favUserMelody == null) button5.setEnabled(false);
		button5.setActionCommand("Play Saved Set");

		initButtonComponent(button1, bd);
		initButtonComponent(button2, bd);
		initButtonComponent(button3, bd);
		initButtonComponent(button4, bd);
		initButtonComponent(button5, bd);
	
		buttPanel.add(button1);
		buttPanel.add(button2);
		buttPanel.add(button3);
		buttPanel.add(button4);
		buttPanel.add(button5);
		buttPanel.initButtons(bd, 5);
		this.add(buttPanel, BorderLayout.WEST);
	}
	
	//used to reset all text areas
	public void resetAllTextAreas()
	{
		resetUMText();
		resetCMText();
		resetPMText();
		resetGMText();
		resetSMText();
	}
	
	public void informSavedSet()
	{
		playedOutput.setText("  Melody set is saved.");
		//location of the originally disabled "play saved set" menu option:
		this.getJMenuBar().getMenu(0).getMenuComponent(5).setEnabled(true);
		
		//location of the originally disabled "play saved set" button:
		JPanel curPanel = (JPanel)this.getContentPane().getComponent(2);
		curPanel.getComponent(4).setEnabled(true);
	}
	
	//this initializes variables that need to be cleared to be processed properly
	public void resetVariables()
	{
		userMelody = new Melody();
		compMelody = new Melody();
		totalMelody = new Melody();
	}
	
	//produces a new keyboard from whatever the current firstKey or keyboardRange is
	//then adds a mouse listener to it, and adds it to the main frame in the NORTH border.
	//
	//the third parameter in the MusicKeyboard constructor is reserved for future implementation of
	//black keys, to make a chromatic keyboard.
	public void makeKB()
	{
		getKeyFromKeyboard = new MusicKeyboard(firstKey, keyboardRange, Scale.DIATONIC);
		this.add(getKeyFromKeyboard, BorderLayout.NORTH);
		getKeyFromKeyboard.initMusicKeyboard();
		//let user know if the keyboard they wanted couldnt be built, but a new one was instead
		if (getKeyFromKeyboard.hadToBeFixed)
		{
			firstKey = getKeyFromKeyboard.getFirstNote();
			playedOutput.setText("  Keyboard range and first key had to be adjusted for size.");
		}
		getKeyFromKeyboard.addMouseListener(mainMA);
	}
	
	//whenever user initiates a program exit, this "are you sure?" is called
	public void confirmExit()
	{
		int selectedValue = JOptionPane.showConfirmDialog(null, 
				"Are you done making music with the computer?", "Quit", JOptionPane.YES_NO_OPTION);
	
		if (selectedValue == JOptionPane.OK_OPTION)
		{
			System.exit(0);
		}
		return;
	}
	
	//this method is necessary to keep user input from flooding into the next turn
	//when the frame is remade, it still appears in the same area on the screen.
	public void mainFrameDisposeAndRebuild()
	{
		this.dispose();
		this.getContentPane().removeAll();
		mainFrameInit();
	}
	
	//initialize and create/reset the frame
	public void mainFrameInit()
	{
		this.resetVariables();
		this.resetAllTextAreas();
		BorderLayout mainLayout = new BorderLayout();
		this.setTitle("Melody Creator "+currentVersionUID+"  by Justin Taylor");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
            	confirmExit();
            }
        });
		this.setBackground(Color.BLACK);
		this.setVisible(true);
		this.getContentPane().setLayout(mainLayout);
		this.setMinimumSize(new Dimension(1000,400));
		this.setMaximumSize(new Dimension(1000,400));
		this.getContentPane().setMaximumSize(new Dimension(1000,400));
		this.setResizable(false);
		this.initTextAreas();
		this.initButtonMenu();
		makeKB();
		this.pack();
	}
	
	//this displays the melody that the computer just performed so the the user can analyze
	//the melody even if the frame is restarted
	public void displaySavedCompMelody()
	{
		for (int counter = 0; counter < savedCompMelody.notesInMelody; counter++)
		{
			if (counter > 0) compMelodyOutput.append(",");
			compMelodyOutput.append("  "+savedCompMelody.getNoteInPlace(counter).notePitch);
		}
		update(getGraphics());
	}
	
	//this functions prompts user to continue with another melody
	public void askForAnother()
	{
		int choice = JOptionPane.showConfirmDialog(null, "Click no to end program.", "Another melody?", JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.NO_OPTION)
		{
			System.exit(0);
		}
		else return;
	}
	
	//this method is called whenever there are two complete melodies that are to be
	//heard again, whether they are saved melodies or just-played melodies
	public void replaySet(Melody u, Melody c)
	{
		resetUMText();
		resetCMText();
		for (int counter = 0; counter < u.notesInMelody; counter++)
		{
			if (counter > 0) userMelodyOutput.append(",");
			userMelodyOutput.append("  "+u.getNoteInPlace(counter).notePitch);
			update(getGraphics());
			u.getNoteInPlace(counter).playNote();
		}
		Note.PAUSE();
		for (int counter = 0; counter < c.notesInMelody; counter++)
		{
			if (counter > 0) compMelodyOutput.append(",");
			compMelodyOutput.append("  "+c.getNoteInPlace(counter).notePitch);
			update(getGraphics());
			c.getNoteInPlace(counter).playNote();
		}
		resetPMText();
	}
	
	//gives the computer a turn to process the user melody and generate its own, then reset the frame
	public void compsTurnAndRestart()
	{
		playedOutput.setText("  The computer will now answer with a  "+maxMelodySize+"  note melody.");
		update(getGraphics());
		Note.PAUSE();
		compMelody = MelodyGenerator.generateMelodyFrom(userMelody, generatorMethod);
		resetCMText();
		for (int counter = 0; counter < compMelody.notesInMelody; counter++)
		{
			if (counter > 0) compMelodyOutput.append(",");
			compMelodyOutput.append("  "+compMelody.getNoteInPlace(counter).notePitch);
			update(getGraphics());
			compMelody.getNoteInPlace(counter).playNote();
		}
		savedCompMelody = compMelody;
		mainFrameDisposeAndRebuild();
		displaySavedCompMelody();
	}
	
	//accurately copy the user melody 
	public void copyUserToSaved()
	{
		for (int count = 0; count < userMelody.notesInMelody; count++)
		{
			savedUserMelody.addNote(userMelody.getNoteInPlace(count));
		}
		
	}
	
	//accurately uses the saved user melody as the new user melody
	public void copySavedToUser()
	{
		for (int count = 0; count < savedUserMelody.notesInMelody; count++)
		{
			userMelody.addNote(savedUserMelody.getNoteInPlace(count));
		}
	}
	
	//adds and tests new note added by user
	public void appendAndTest(Note n)
	{
		
		userMelody.addNote(n);
		if (userMelody.notesInMelody > 1) userMelodyOutput.append(",");
		else if (userMelody.notesInMelody == 1)
		{
			update(getGraphics());
			resetUMText();
		}
		userMelodyOutput.append("  "+n.notePitch);
		playedOutput.setText("  Note just played:  "+n.notePitch+"  (note "+userMelody.getNumberOfNotes()+" of "+maxMelodySize+")");
		update(getGraphics());
		n.playNote();
		n.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		update(getGraphics());
		if (userMelody.notesInMelody >= maxMelodySize)
		{
			savedUserMelody = new Melody();
			copyUserToSaved();
			compsTurnAndRestart();
		}
	}
	
	public static void main(String[] args)
	{
		MelodyCreator mainFrame = new MelodyCreator();	
		mainFrame.initMCMenuBar();
		mainFrame.mainFrameInit();

	}
}
	
