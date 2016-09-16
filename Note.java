import javax.swing.JComponent;
import java.lang.Integer;
import java.io.*;
import javax.sound.sampled.*;
import java.net.*;
import javax.swing.JOptionPane;
/*
This class provides the basic methods and definitions for musical Notes, and is used by
the included music related classes.  It currently only supports natural notes.  It extends
JComponent, so it can be used easily with other Swing components.
*/
public class Note extends JComponent
{
	private static final long serialVersionUID = -8105939764526671751L;
	
	//these note integers correspond with range numbers
	static int QUARTER_REST = -1;
	static int C_1 = 0;
	static int D_1 = 1;
	static int E_1 = 2;
	static int F_1 = 3;
	static int G_1 = 4;
	static int A_1 = 5;
	static int B_1 = 6;
	static int C_2 = 7;
	static int D_2 = 8;
	static int E_2 = 9;
	static int F_2 = 10;
	static int G_2 = 11;
	static int A_2 = 12;
	static int B_2 = 13;
	static int C_3 = 14;
	static int D_3 = 15;
	static int E_3 = 16;
	static int F_3 = 17;
	static int G_3 = 18;
	static int A_3 = 19;
	static int B_3 = 20;
	static int C_4 = 21;
	static int D_4 = 22;
	static int E_4 = 23;
	static int F_4 = 24;
	static int G_4 = 25;
	static int A_4 = 26;
	static int B_4 = 27;
	static int C_5 = 28;
	static int D_5 = 29;
	static int E_5 = 30;
	static int F_5 = 31;
	static int G_5 = 32;
	static int A_5 = 33;
	static int B_5 = 34;
	static int C_6 = 35;
	
	static String[] ALL_NOTES = {
		"C_1", "D_1", "E_1", "F_1", "G_1", "A_1", "B_1",
		"C_2", "D_2", "E_2", "F_2", "G_2", "A_2", "B_2",
		"C_3", "D_3", "E_3", "F_3", "G_3", "A_3", "B_3",
		"C_4", "D_4", "E_4", "F_4", "G_4", "A_4", "B_4",
		"C_5", "D_5", "E_5", "F_5", "G_5", "A_5", "B_5",
		"C_6"};
	
	
	static char SHARP = '#';
	static char FLAT = 'b';
	static char NATURAL = '_';
	
	static int WHOLE = 0;
	static int HALF = 1;
	static int QUARTER = 2;
	static int EIGHTH = 3;
	
	static final boolean REST = false;
	static final boolean NOTE = true;

	protected boolean isANote = true;
	protected int noteNumberInKeyboard = 0;
	protected int noteNumberInRange = -1;
	protected int octave = 0;
	protected char noteName = ' ';
	protected char accidental = ' ';
	protected int length = 0;
	
	public String notePitch = "   ";
	
	//returns the String equivalent of the given number
	static String getNoteFromNumber(int n)
	{
		String nn = ALL_NOTES[n];
		return(nn);
	}
	
	//tests to see if the given Note has a definition
	static boolean isInAllNoteRange(Note n)
	{
		boolean inItYet = false;
		int count = 0;
		while (count < ALL_NOTES.length && !inItYet)
		{
			if (n.notePitch == ALL_NOTES[count]) inItYet = true;
			else count++;
		}
		return(inItYet);
	}
	
	//enacts a generic pause
	static void PAUSE()
	{
		try
		{
			for (int c=0;c<10;c++)Thread.sleep(100);
		} 
		catch (InterruptedException ie) 
		{
			ie.printStackTrace();
		}
	}
	
	//returns what a basic note number would be from a certain note name string
	static int getNoteNumberInRange(String n)
	{
			int m1 = 0;
			int m2 = 0;
			int nn = 0;	
			switch (n.charAt(0))
			{
				case 'C': m1 = 0; break;
				case 'D': m1 = 1; break;
				case 'E': m1 = 2; break;
				case 'F': m1 = 3; break;
				case 'G': m1 = 4; break;
				case 'A': m1 = 5; break;
				case 'B': m1 = 6; break;
			}
			m2 = Character.getNumericValue(n.charAt(2));
			nn = ((7*m2)-7)+m1;
			return(nn);
	}
	
	//called inside this class to play PCM audio through a line from the provided URL 
	protected static void streamSampledAudio(URL url) throws IOException        
	{
	    AudioInputStream ain = null;
	    SourceDataLine line = null;
	    try 
	    {  	
	    	//open an audio stream 
	    	ain = AudioSystem.getAudioInputStream(url);
	        AudioFormat format = ain.getFormat();
	        DataLine.Info info = new DataLine.Info(SourceDataLine.class,format);
	        if (!AudioSystem.isLineSupported(info)) 
	        {
	            AudioFormat pcm = new AudioFormat(format.getSampleRate(), 16, format.getChannels( ), true, false);
	            ain = AudioSystem.getAudioInputStream(pcm, ain);
	            format = ain.getFormat(); 
	            info = new DataLine.Info(SourceDataLine.class, format);
	        }
	        //open a line
	        line = (SourceDataLine) AudioSystem.getLine(info);
	        line.open(format);  
	        int framesize = format.getFrameSize();
	        byte[] buffer = new byte[4 * 1024 * framesize];
	        int numbytes = 0;

	        boolean started = false;
	
	        for(;;) 
	        {  
	        	int bytesread=ain.read(buffer,numbytes,buffer.length-numbytes);
	            if (bytesread == -1) break;
	            numbytes += bytesread;
	            if (!started) 
	            {
	                line.start( );
	                started = true;
	            }

	            int bytestowrite = (numbytes/framesize)*framesize;

	            line.write(buffer, 0, bytestowrite);

	            int remaining = numbytes - bytestowrite;
	            if (remaining == 0) System.arraycopy(buffer,bytestowrite,buffer,0,remaining);
	            numbytes = remaining;
	        }
	        line.drain( );
	    }
	    catch (IOException ioe)
	    {
	    	System.out.println("IOException in audio.");
	    	ioe.printStackTrace();
	    	return;
	    }
	    catch (UnsupportedAudioFileException uafe)
	    {
	    	System.out.println("Unsupported audio.");
	    	uafe.printStackTrace();
	    	return;
	    }
	    catch (LineUnavailableException lue)
	    {
	    	System.out.println("Line unavailable.");
	    	lue.printStackTrace();
	    	return;
	    }
	    finally 
	    {
	    	//close up the line and stream
	        if (line != null) line.close( );
	        if (ain != null) ain.close( );
	    }
	}
	
	//changes this note to one of a given octave
	public void changeOctaveTo(int o)
	{
		Note tempNote = this;
		tempNote.notePitch.replace(notePitch.charAt(2), Integer.toString(o).charAt(0));
		tempNote = new Note(Note.getNoteNumberInRange(tempNote.notePitch));
		this.notePitch = tempNote.notePitch;
		this.initNote();
	}
	
	//constructs the lowest possible note
	public Note()
	{
		noteNumberInRange = 0;
		octave = 1;
		noteName = 'C';
		accidental = '_';
		this.getNotePitch();
		initNote();
	}
	
	//constructs a note from the given string
	public Note(String n)
	{
		this.notePitch = n;
		this.octave = Character.getNumericValue(notePitch.charAt(2));
		this.noteName = notePitch.charAt(0);
		this.accidental = notePitch.charAt(1);
		this.noteNumberInRange = Note.getNoteNumberInRange(notePitch);
		initNote();
	}
	
	//constructs a note from the given note number
	//if given note is invalid, it automatically creates a valid one
	public Note(int n)
	{
		if (n >= 0 && n < ALL_NOTES.length)
		{
			this.noteNumberInRange = n;
			this.notePitch = ALL_NOTES[n];
			this.octave = Character.getNumericValue(ALL_NOTES[n].charAt(2));
			this.accidental = ALL_NOTES[n].charAt(1);
			this.noteName = ALL_NOTES[n].charAt(0);
		}
		else if (n < 0)
		{
			this.noteNumberInRange = Note.C_1;
			this.notePitch = ALL_NOTES[0];
			this.octave = Character.getNumericValue(ALL_NOTES[0].charAt(2));
			this.accidental = ALL_NOTES[0].charAt(1);
			this.noteName = ALL_NOTES[0].charAt(0);
		}
		else if (n >= ALL_NOTES.length)
		{
			this.noteNumberInRange = ALL_NOTES.length-1;
			this.notePitch = ALL_NOTES[noteNumberInRange];
			this.octave = Character.getNumericValue(ALL_NOTES[noteNumberInRange].charAt(2));
			this.accidental = ALL_NOTES[noteNumberInRange].charAt(1);
			this.noteName = ALL_NOTES[noteNumberInRange].charAt(0);
		}
	}
	
	//makes sure all variables are set correctly
	public void initNote()
	{
		int n = this.noteNumberInRange;
		
		if (n >= 0 && this.notePitch == "   ") this.getNotePitch();
		if (n == -1 && this.notePitch != "   ") this.getNoteNumberInRange();
		
		if (n >= 0 && n < ALL_NOTES.length)
		{
			this.notePitch = ALL_NOTES[n];
			this.octave = Character.getNumericValue(ALL_NOTES[n].charAt(2));
			this.accidental = ALL_NOTES[n].charAt(1);
			this.noteName = ALL_NOTES[n].charAt(0);
		}
		else if (n >= ALL_NOTES.length)
		{
			this.noteNumberInRange = ALL_NOTES.length-1;
			this.notePitch = ALL_NOTES[noteNumberInRange];
			this.octave = Character.getNumericValue(ALL_NOTES[noteNumberInRange].charAt(2));
			this.accidental = ALL_NOTES[noteNumberInRange].charAt(1);
			this.noteName = ALL_NOTES[noteNumberInRange].charAt(0);
		}
		else if (n < 0)
		{
			this.noteNumberInRange = 0;
			this.notePitch = ALL_NOTES[0];
			this.octave = Character.getNumericValue(ALL_NOTES[0].charAt(2));
			this.accidental = ALL_NOTES[0].charAt(1);
			this.noteName = ALL_NOTES[0].charAt(0);
		}
	}

	//constructs a note from the given note name, accidental, and octave
	public Note(char n, char a, int oct)
	{
		this.noteNumberInRange = -1;
		this.octave = oct;
		this.accidental = a;
		this.noteName = n;
		initNote();
	}
	
	//constructs the equivalent of a rest
	public Note(boolean noteOrNot)
	{
		this.isANote = noteOrNot;
	}
	
	//returns the note number of this note
	public int getNoteNumberInRange()
	{
		if (noteNumberInRange == -1)
		{
			int m1 = 0;
			int m2 = 0;
			int nn = 0;	
			switch (notePitch.charAt(0))
			{
				case 'C': m1 = 0; break;
				case 'D': m1 = 1; break;
				case 'E': m1 = 2; break;
				case 'F': m1 = 3; break;
				case 'G': m1 = 4; break;
				case 'A': m1 = 5; break;
				case 'B': m1 = 6; break;
			}
			m2 = Character.getNumericValue(notePitch.charAt(2));
			nn = ((7*m2)-7)+m1;
			return(nn);
		}
		else return noteNumberInRange;
	}
	
	//returns the octave of this note
	public int getOctave()
	{
		if (octave == 0)
		{
			return(Character.getNumericValue(notePitch.charAt(2)));
		}
		else return(octave);
		
	}
	
	//returns the note name of this note
	public char getNoteName()
	{
		if (noteName == ' ')
		{
			return(notePitch.charAt(0));
		}
		else return(noteName);
	}
		
	//returns the note pitch string of this note
	public String getNotePitch()
	{
		if (notePitch == "   " && noteName != ' ' && octave != 0)
		{
			String nPitch = " ";
			StringWriter np = new StringWriter();
			np.flush();
			PrintWriter npPrinter = new PrintWriter(np);
			npPrinter.print(Character.toString(noteName)+Character.toString(accidental)+Integer.toString(octave));
			nPitch = np.toString();
			return nPitch;
		}
		else return(notePitch);
	}
	
	//finds the appropriate note file and calls the actual playing function of this note
	public void playNote()
	{
		if (this.isANote)
		{
			try
			{
				StringWriter waveFileName = new StringWriter();
				PrintWriter waveFileNamePrinter = new PrintWriter(waveFileName);
				waveFileNamePrinter.print(notePitch + ".wav");
				URL urlClip = Note.class.getResource(waveFileName.toString());
				Note.streamSampledAudio(urlClip);
			} catch (Exception e)
			{
				JOptionPane.showMessageDialog(null, "Can not find " + notePitch
						+ ".wav", "Alert", JOptionPane.ERROR_MESSAGE);
			}
		}
		else Note.PAUSE();
	}
}
