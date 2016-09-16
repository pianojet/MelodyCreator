import java.util.ArrayList;
/*
This class is written to provide useful methods for manipulating and testing chords, as well as to provide
definitions for simple triads made up of notes from the Note class.  It can be instantiated to offer more 
functionality with using chords.  Other classes can extend this one to define more complex chords, 
such as seventh chords, ninth chords, etc.
*/

public class Chord
{
	static Note[] CM = { 
		new Note("C_1"), new Note("E_1"), new Note("G_1"), new Note("C_2"), new Note("E_2"), new Note("G_2"),
		new Note("C_3"), new Note("E_3"), new Note("G_3"), new Note("C_4"), new Note("E_4"), new Note("G_4"),
		new Note("C_5"), new Note("E_5"), new Note("G_5"), new Note("C_6")};

	static Note[] GM = {
		new Note("D_1"), new Note("G_1"), new Note("B_1"), new Note("D_2"), new Note("G_2"), new Note("B_2"),
		new Note("D_3"), new Note("G_3"), new Note("B_3"), new Note("D_4"), new Note("G_4"), new Note("B_4"),
		new Note("D_5"), new Note("G_5"), new Note("B_5")};

	static Note[] G7 = {
		new Note("D_1"), new Note("F_1"), new Note("G_1"), new Note("B_1"), new Note("D_2"), new Note("F_2"), new Note("G_2"), new Note("B_2"),
		new Note("D_3"), new Note("F_3"), new Note("G_3"), new Note("B_3"), new Note("D_4"), new Note("F_4"), new Note("G_4"), new Note("B_4"),
		new Note("D_5"), new Note("F_5"), new Note("G_5"), new Note("B_5")};

	static Note[] FM = {
		new Note("C_1"), new Note("F_1"), new Note("A_1"), new Note("C_2"), new Note("F_2"), new Note("A_2"),
		new Note("C_3"), new Note("F_3"), new Note("A_3"), new Note("C_4"), new Note("F_4"), new Note("A_4"),
		new Note("C_5"), new Note("F_5"), new Note("A_5"), new Note("C_6")};
	
	static Note[][] ALL_CHORDS = {
		CM, GM, G7, FM };
	
	
	static char MAJOR_QUALITY = 'M';
	static char MINOR_QUALITY = 'm';
	static char DIMINISHED_QUALITY = 'o';
	static char AUGMENTED_QUALITY = '+';
	
	static int TONIC_DEGREE = 0;
	static int SUPERTONIC_DEGREE = 1;
	static int MEDIANT_DEGREE = 2;
	static int SUBDOMINANT_DEGREE = 3;
	static int DOMINANT_DEGREE = 4;
	static int SUBMEDIANT_DEGREE = 5;
	static int LEADINGTONE_DEGREE = 6;
	
	public String chordName = new String();
	protected char chordRoot = ' ';
	protected char quality = ' ';
	protected int degree = 0;
	protected int totalNotesInChord = 0;
	protected ArrayList<Note> notesInChord = new ArrayList<Note>(0);
	
//builds a whole list of Notes that belong to the triad of the given root 
//not yet written
	public Chord(int rootNote)
	{
//		Note chordRoot = new Note(rootNote);
//		char rootNoteName = chordRoot.getNotePitch().charAt(0);
		quality = MAJOR_QUALITY;
		degree = TONIC_DEGREE;
//		Note noteToAdd = null;
		for (int count = 0; count < Note.ALL_NOTES.length; count++)
		{
/*			noteToAdd = new Note(Note.ALL_NOTES[count]);
			int nnToTest
			if noteToAdd.getNoteNumberInRange()+6/
			if (noteToAdd.getNoteNumberInRange() == chordRoot.getNoteNumberInRange() ||
				noteToAdd.getNoteNumberInRange() == chordRoot.getNoteNumberInRange()+2 ||
				noteToAdd.getNoteNumberInRange() == chordRoot.getNoteNumberInRange()+4)
			{
				
			}*/
		}
	}
	
	public Chord(char n, char q)
	{
		if ((q == MAJOR_QUALITY) || (q == MINOR_QUALITY) ||
		(q == DIMINISHED_QUALITY) || (q == AUGMENTED_QUALITY))
		{
			quality = q;
		}
		else quality = MAJOR_QUALITY;
		chordRoot = n;
	}
	
	//called by any function to test if a given note exists in a given chord
	static boolean isInChord(Note n, Note[] c)
	{
		boolean inChordYet = false; 
		Note testNote = n;
		Note[] testChord = c;
		int chordSize = testChord.length;
		int count = 0;
		while (!inChordYet && count<chordSize)
		{
			if (testChord[count].notePitch.equals(testNote.notePitch)) inChordYet = true;
			else count++;
		}
		return(inChordYet);
	}
	
	//tests to see if a given note exists inside of this instantiated chord
	public boolean isInChord(Note n)
	{
		boolean inChordYet = false; 
		Note testNote = n;
		int chordSize = this.totalNotesInChord;
		int count = 0;
		while (!inChordYet && count<chordSize)
		{
			if (this.notesInChord.get(count).notePitch.equals(testNote.notePitch)) inChordYet = true;
			else count++;
		}
		return(inChordYet);
	}
	
	public char getQuality()
	{
		return(quality);
	}
	
	public String getChordName()
	{
		return(chordName);
	}
}
