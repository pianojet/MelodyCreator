import java.util.ArrayList;
/*
This class is written to provide useful methods and definitions used to manipulate and test
melodies of any size made up of Notes from the Note class.
*/
public class Melody
{
	protected ArrayList<Note> noteList = new ArrayList<Note>();
	protected int notesInMelody = 0;
	
	//cycles through each note and plays it
	static void playMelody(Melody playMe)
	{
		Note.PAUSE();
		for (int counter = 0; counter < playMe.notesInMelody; counter++)
		{
			playMe.getNoteInPlace(counter).playNote();	
		}
	}
	
	//switches a note in this melody in the given place with the given note
	public void replaceNote(int p, Note n)
	{
		this.noteList.set(p, n);
	}
	
	//returns the range of the given melody. highest note - lowest note
	static int getMelodyRange(Melody m)
	{
		if (m.notesInMelody > 0)
		{
			Note hNote = m.getHighestNote();
			Note lNote = m.getLowestNote();
			int r = hNote.noteNumberInRange-lNote.noteNumberInRange+1;
			return(r);
		}
		else
		{
			System.out.println("No notes in melody!");
			return(0);
		}
	}
	
	//returns an array of distances between notes of this melody.  The array size is 
	//one less than the melody size.  Each element is an int that shows the interval
	//in scale steps between the 2 notes on either "side" (it does not discriminate
	//between whole steps and half steps).  if it is negative, the next note is lower.
	//if positive, the next note is higher.
	public int[] getMelodyShape()
	{
		int[] mShape = new int[this.notesInMelody-1];
		for (int shapecount = 0; shapecount < this.notesInMelody-1; shapecount++)
		{
			mShape[shapecount] =
			(this.getNoteInPlace(shapecount+1).noteNumberInRange - this.getNoteInPlace(shapecount).noteNumberInRange);
		
			System.out.print(mShape[shapecount] + "  ");
		}
		return(mShape);
	}
	
	//constructs a completely empty melody with no size.
	public Melody()
	{
		ArrayList<Note> tempList = new ArrayList<Note>();
		this.noteList = tempList;
		this.notesInMelody = 0;
	}
	
	//constructs an empty melody with the given size
	public Melody(int size)
	{
		ArrayList<Note> tempList = new ArrayList<Note>(size);
		noteList = tempList;
	}
	
	//returns the number of valid notes this melody holds
	public int getNumberOfNotes()
	{
		return(this.notesInMelody);
	}
	
	//adds a given note to this melody.
	public void addNote(Note n)
	{
		this.noteList.add(n);
		this.notesInMelody++;
	}
	
	//returns a Note in the given place from this melody
	public Note getNoteInPlace(int p)
	{
		if (p<this.notesInMelody && p >= 0)
		{
			Note tempNote = null;
			tempNote = this.noteList.get(p);
			return(tempNote);
		}
		else
		{
			System.out.println("p = "+p+" notes in melody: "+this.notesInMelody);
			System.out.println("Invalid access to melody.");
			return(null);
		}
	}
	
	//returns the first highest note in this melody.
	public Note getHighestNote()
	{
		if (this.notesInMelody>0)
		{
			Note highestNote = noteList.get(0); 
			for (int counter = 0; counter<this.notesInMelody; counter++)
			{
				if (noteList.get(counter).getNoteNumberInRange() > highestNote.getNoteNumberInRange())
				{
					highestNote = noteList.get(counter);
				}
			}
			return(highestNote);
		}
		else
		{
			System.out.println("Can't get highest note in an empty melody.");
			return(null);
		}
	}
	
	//returns the first lowest note in this melody.
	public Note getLowestNote()
	{
		if (this.notesInMelody>0)
		{
			Note lowestNote = this.noteList.get(0); 
			for (int counter = 0; counter<this.notesInMelody; counter++)
			{
				if (this.noteList.get(counter).getNoteNumberInRange() < lowestNote.getNoteNumberInRange())
				{
					lowestNote = this.noteList.get(counter);
				}
			}
			return(lowestNote);
		}
		else
		{
			System.out.println("Can't get lowest note in an empty melody.");
			return(null);
		}
	}
}
