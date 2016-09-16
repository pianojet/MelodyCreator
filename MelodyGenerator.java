import java.util.Random;
/*
This class provides many useful methods that are part of making computer created, musical
melodies.  This class works hand in hand with the Melody class, and uses the contents found
in the Note class.
*/
public class MelodyGenerator
{
	static final int AUTOMATED = -1;
	static final int COPY_MELODY = 0;
	static final int HORIZONTAL_AXIS_FLIP = 1;
	static final int VERTICAL_AXIS_FLIP = 2;
	static final int SIMPLE_HARMONY_FLOP = 3;
	static final int LOWERED_SEQUENCE = 4;
	static final int RAISED_SEQUENCE = 5;
	static final int RESOLVE_LAST_NOTE = 6;
	static final int SIMPLE_CONSEQUENT_PARTECHO = 7;
	static final int CONSEQUENT_CONJUNCT_RESPONSE = 8;
	static final int CONSEQUENT_DISJUNCT_RESPONSE = 9;
	static final int MULTI_SEQUENCE_RESPONSE = 10;
	static final int RANDOM_CACOPHONY = 11;
	static final int totalNumOfMethods = 12;
	static Random rnd = new Random();

	//returns either -1 or +1 randomly
	static int plusOrMinusOne()
	{
		int r = 0;
		int p = rnd.nextInt(2);
		if (p == 0) r = -1;
		else r = 1;
		return(r);
	}
	
	//returns a melody of completely random notes
	static Melody randomCacophony(Melody m)
	{
		Melody mel = m;
		for (int count = 0; count < mel.notesInMelody; count ++)
			mel.replaceNote(count, new Note(rnd.nextInt(Note.ALL_NOTES.length)));
		return(mel);
	}
	
	//simply returns the melody given to it.
	static Melody copyMelody(Melody m)
	{
		Melody mel = m;
		return(mel);
	}
	
	//flips the given melody along a horizontal axis.
	static Melody horizontalAxisFlip(Melody m)
	{
		Melody mel = new Melody();
		int h = m.getHighestNote().noteNumberInRange;
		int l = m.getLowestNote().noteNumberInRange;
		for (int counter = 0; counter < m.getNumberOfNotes(); counter++)
		{
			int newNoteNum = l + ( h-m.getNoteInPlace(counter).noteNumberInRange);
			Note newNote = new Note(newNoteNum);
			mel.addNote(newNote);
		}
		return(mel);
	}
	
	//flips the given melody along a vertical axis
	static Melody verticalAxisFlip(Melody m)
	{
		Melody mel = new Melody();

		for (int counter = 0; counter < m.getNumberOfNotes(); counter++)
		{
			mel.addNote(m.getNoteInPlace((m.getNumberOfNotes()-1)-counter)); 
		}
		return(mel);
	}
	
	//switches C chord notes with G chord notes, and vice versa
	static Melody simpleHarmonyFlop(Melody m)
	{
		Melody mel = new Melody();
		for (int counter = 0; counter < m.getNumberOfNotes(); counter++)
		{
			switch (((m.getNoteInPlace(counter).getNoteNumberInRange())%7))
			{
			case 1:
			case 3:
			case 5:
				{
					if (m.getNoteInPlace(counter).getNoteNumberInRange() <= 1)
					{
						Note newNote = new Note(m.getNoteInPlace(counter).getNoteNumberInRange()+4);
						mel.addNote(newNote);
					}
					else
					{
						Note newNote = new Note(m.getNoteInPlace(counter).getNoteNumberInRange()-1);
						mel.addNote(newNote);
					}
					break;
				}
			case 2:
			case 4:
			case 6:
			case 0:
				{
					Note newNote = new Note(m.getNoteInPlace(counter).getNoteNumberInRange()+1);
					mel.addNote(newNote);
					break;
				}
			}
		}
		return(mel);
	}
	
	//lowers the entire given melody a random amount 3 notes or less
	static Melody loweredSequence(Melody m)
	{
		Melody mel = m;
		//test to see if the melody can be lowered at all
		if (mel.getLowestNote().noteNumberInRange > 0)
		{
			int maxDisplacementRange = 2; 
			int size = mel.notesInMelody;
			//find a suitable displacement range so that we dont make a melody of invalid notes
			while ((mel.getLowestNote().noteNumberInRange-maxDisplacementRange < 0) && (maxDisplacementRange > 0))
			{
				maxDisplacementRange--;	
			}
			int displacement = rnd.nextInt(maxDisplacementRange) + 1;
			//switch the notes
			for (int count = 0; count < size; count++)
			{
				mel.replaceNote(count, new Note(mel.getNoteInPlace(count).noteNumberInRange-displacement));
			}
		}
		return(mel);
	}
	
	//raises the entire given melody a random amount 3 notes or less
	static Melody raisedSequence(Melody m)
	{
		Melody mel = m;
		//test to see if the melody can be raised at all
		if (mel.getHighestNote().noteNumberInRange < Note.ALL_NOTES.length-1)
		{
			int maxDisplacementRange = 2; 
			int size = mel.notesInMelody;
			//find a suitable displacement range that wont push the melody too high
			while ((mel.getHighestNote().noteNumberInRange+maxDisplacementRange >= Note.ALL_NOTES.length) && (maxDisplacementRange > 0))
			{
				maxDisplacementRange--;	
			}
			int displacement = rnd.nextInt(maxDisplacementRange) + 1;
			//switch the notes
			for (int count = 0; count < size; count++)
			{
				mel.replaceNote(count, new Note(mel.getNoteInPlace(count).noteNumberInRange+displacement));
			}
		}
		return(mel);
	}
	
	//changes the given melody's last note into a C chord tone
	static Melody resolveLastNote(Melody m)
	{
		Melody mel = m;
		int size = m.notesInMelody;
		Note lastNote = mel.getNoteInPlace(size-1);
		Note nextToLastNote = new Note(mel.getNoteInPlace(size-2).notePitch);
		Note newLast = lastNote;
		//test to see if the last note is already in a C chord
		if (!Chord.isInChord(newLast,Chord.CM))
		{
			int upOrDown = MelodyGenerator.plusOrMinusOne();
			int count = 1;
			//ensure the end of the melody moves stepwise to the last note, which is a
			//random C chord tone
			while (!Chord.isInChord(newLast, Chord.CM))
			{
				newLast = new Note(nextToLastNote.noteNumberInRange+count*(upOrDown*(-1)));
				if (!Chord.isInChord(newLast, Chord.CM))
				{
					count++;
					newLast = new Note(nextToLastNote.noteNumberInRange+count*(upOrDown));
				}
			}
		}
		//if the note is already a chord tone, make it a root (C) note
		else if (newLast.noteName == 'E')
		{
			newLast = new Note(newLast.noteNumberInRange-2);
		}
		else if (newLast.noteName == 'G')
		{
			switch (MelodyGenerator.plusOrMinusOne())
			{
			case -1: newLast = new Note(newLast.noteNumberInRange+3);break;
			case 1:  newLast = new Note(newLast.noteNumberInRange-4);break;
			}
		}
		mel.replaceNote(size-1, newLast);
		return(mel);
	}
	
	//starts from the 2nd note of the given melody, adds a new note to the end in a C chord
	static Melody simpleConsequentPartecho(Melody m)
	{
		Melody mel = m;
		int size = m.notesInMelody;
		Melody tempMel = new Melody();
		//copy the given melody from the 2nd note
		for (int count = 1;count < size; count++)
		{
			tempMel.addNote(m.getNoteInPlace(count));
		}

		Note newLast = mel.getNoteInPlace(size-1);
		//test to see if the last note is already chord note
		if (!Chord.isInChord(newLast,Chord.CM))
		{
			Note nextToLastNote = newLast;
			int upOrDown = MelodyGenerator.plusOrMinusOne();
			newLast = new Note(nextToLastNote.noteNumberInRange+upOrDown);
			int count = 1;
			//ensures the last part of the resolved melody moves stepwise
			while (!Chord.isInChord(newLast, Chord.CM))
			{
				newLast = new Note(nextToLastNote.noteNumberInRange+count*(upOrDown*(-1)));
				if (!Chord.isInChord(newLast, Chord.CM))
				{
					count++;
					newLast = new Note(nextToLastNote.noteNumberInRange+count*(upOrDown));
				}
			}
		}
		//if its already in a C chord, make it a C or a G
		else if (newLast.noteName == 'E')
		{
			newLast = new Note(newLast.noteNumberInRange-(MelodyGenerator.plusOrMinusOne()+1));
		}
		else if (newLast.noteName == 'G')
		{
			switch (MelodyGenerator.plusOrMinusOne())
			{
			case -1: newLast = new Note(newLast.noteNumberInRange+3);break;
			case 1:  newLast = new Note(newLast.noteNumberInRange-4);break;
			}
		}
		tempMel.addNote(newLast);
		mel = tempMel;
		return(mel);
	}
	
	//resolves the last note of the given melody, then works backwards to create new
	//notes that move stepwise, randomly up or down 
	static Melody consequentConjunctResponse(Melody m)
	{
		//begin by having a solid, resolved ending in the melody 
		Melody mel = resolveLastNote(m);
		Note noteToAdd = null;
		int size = m.notesInMelody;
		int count = size-2;
		while (count >= 0)
		{
			//adds a note randomly up or down
			noteToAdd = new Note(mel.getNoteInPlace(count+1).noteNumberInRange+MelodyGenerator.plusOrMinusOne());
			mel.replaceNote(count, noteToAdd);
			count--;
		}
		return(mel);
	}
	
	static Melody consequentDisjunctResponse(Melody m)
	{
		//begin by having a solid, resolved ending in the melody 
		Melody mel = resolveLastNote(m);
		Note noteToAdd = null; 
		int size = m.notesInMelody;
		//make sure last note is a C...
		if (mel.getNoteInPlace(size-1).noteName == 'C')
		{
			//test to see if its the highest C or lowest C
			if (mel.getNoteInPlace(size-1).noteNumberInRange+1 == Note.ALL_NOTES.length)
			{
				//... make 2nd from last note a G so that it goes G-C
				mel.replaceNote(size-2, new Note(Note.ALL_NOTES.length-5));
			}
			else if (mel.getNoteInPlace(size-1).noteNumberInRange == Note.C_1)
			{
				//... make 2nd from last note a higher G so it goes G-C
				mel.replaceNote(size-2, new Note(Note.G_1));
			}
			else
			{
				//if its a valid C, make 2nd from last note a high or low G at random...
				if (plusOrMinusOne() == -1)
					mel.replaceNote(size-2, new Note(mel.getNoteInPlace(size-1).noteNumberInRange-3));
				else mel.replaceNote(size-2, new Note(mel.getNoteInPlace(size-1).noteNumberInRange+4));
			}
		}
		else if (mel.getNoteInPlace(size-1).noteName == 'E')
		{
			//switch the last note E with a C, then make the last 2 notes go E-C...
			noteToAdd = mel.getNoteInPlace(size-1);
			mel.replaceNote(size-1, new Note(mel.getNoteInPlace(size-1).noteNumberInRange-2));
			mel.replaceNote(size-2, noteToAdd);
		}
		else if (mel.getNoteInPlace(size-1).noteName == 'G')
		{
			//switch the last note G with a randomly higher or lower C, then make it resolve G-C...
			noteToAdd = mel.getNoteInPlace(size-1);
			if (plusOrMinusOne() == -1)
				mel.replaceNote(size-1, new Note(mel.getNoteInPlace(size-1).noteNumberInRange-3));
			else mel.replaceNote(size-1, new Note(mel.getNoteInPlace(size-1).noteNumberInRange+4));
			mel.replaceNote(size-2, noteToAdd);
		}
		//last two notes (size-2) of the melody are done.
		
		int count = size-3;
		while (count >= 0)
		{
			//adds a note randomly up or down by skip
			noteToAdd = new Note(mel.getNoteInPlace(count+1).noteNumberInRange+2*MelodyGenerator.plusOrMinusOne());
			mel.replaceNote(count, noteToAdd);
			count--;
		}
		return(mel);
	}
	
	
	static Melody multiSequenceResponse(Melody m)
	{
		//begin by having a solid, resolved ending in the melody 
		Melody mel = resolveLastNote(m); 
		int size = m.notesInMelody;
		if (size > 8)
		{
			Melody cutMel = new Melody();
			//get a quick copy of the 4 notes before the last note in the melody in correct order
			for (int count = 0; count < 4; count++) cutMel.addNote(mel.getNoteInPlace(size-(5-count)));

			//figure out how many 4 note sequences will fit, and make an array of sequences
			int icount = (mel.getNumberOfNotes()-5)/4;
			Melody[] seqMel = new Melody[icount];
			while (icount > 0)
			{
				icount--;
				//obtain a sequence higher or lower at random of the copy
				if (plusOrMinusOne() == -1) seqMel[icount] = loweredSequence(cutMel); 
				else seqMel[icount] = raisedSequence(cutMel);

			}
			icount = (mel.getNumberOfNotes()-5)/4;
			while (icount > 0)
			{
				icount --;
				//inserts the sequences into the melody, each group of 4 notes at a time
				for (int count2 = 0; count2 < 4; count2 ++)
				{
					mel.replaceNote((icount*4)+count2, seqMel[icount].getNoteInPlace(count2));
				}
			}
		}
		else if (size > 4)
		{
			Melody cutMel = new Melody();
			Melody seqMel = new Melody();
			for (int count = 0; count < 3; count++) cutMel.addNote(mel.getNoteInPlace(size-(3-count)));
			if (plusOrMinusOne() == -1) seqMel = loweredSequence(cutMel); 
			else seqMel = raisedSequence(cutMel);
			for (int count = 0; count < 2; count++) mel.replaceNote(count, seqMel.getNoteInPlace(count));		
		}
		return(mel);
	}
	

/*

//	The following planned methods are not yet coded

	
	static Melody simpleConsequentDisjunctResponse(Melody m)
	{
		Melody mel = new Melody();
		int size = m.notesInMelody;
		Note[] tempMel = new Note[size];
		return(mel);
	}
	static Melody simpleAntecedentConjunctResponse(Melody m)
	{
		Melody mel = new Melody();
		int size = m.notesInMelody;
		Note[] tempMel = new Note[size];
		return(mel);
	}
	static Melody simpleAntecedentDisjunctResponse(Melody m)
	{
		Melody mel = new Melody();
		int size = m.notesInMelody;
		Note[] tempMel = new Note[size];
		return(mel);
	}*/

	//runs a given melody through the given generator method
	static Melody composeUsingMethod(final Melody m, int method)
	{
		Melody finishedMelody = new Melody();
		int meth = method;
		switch (meth)
		{
		case COPY_MELODY:
			finishedMelody = copyMelody(m); // a copy
			System.out.println("Method 1, Copy");
			break;
		case HORIZONTAL_AXIS_FLIP:
			finishedMelody = horizontalAxisFlip(m); //flip along horizontal axis
			System.out.println("Method 2, Horizontal Axis Flip");
			break;
		case VERTICAL_AXIS_FLIP:
			finishedMelody = verticalAxisFlip(m); //flip along vertical axis
			System.out.println("Method 3, Vertical Axis Flip");
			break;
		case SIMPLE_HARMONY_FLOP:
			finishedMelody = simpleHarmonyFlop(m); //primitive harmony flop
			System.out.println("Method 4, Simple Harmony Flop");
			break;
		case LOWERED_SEQUENCE:
			finishedMelody = loweredSequence(m); //primitive harmony flop
			System.out.println("Method 5, lowered sequence");
			break;	
		case RAISED_SEQUENCE:
			finishedMelody = raisedSequence(m); //primitive harmony flop
			System.out.println("Method 6, raised sequence");
			break;	
		case RESOLVE_LAST_NOTE:
			finishedMelody = resolveLastNote(m); //resolves last note
			System.out.println("Method 7, Resolve Last Note");
			break;
		case SIMPLE_CONSEQUENT_PARTECHO:
			finishedMelody = simpleConsequentPartecho(m); //melody shifted, resolved
			System.out.println("Method 8, Simple Consequent Part-echo");
			break;
		case CONSEQUENT_CONJUNCT_RESPONSE:
			finishedMelody = consequentConjunctResponse(m); //different stepwise melody, resolved
			System.out.println("Method 9, Consequent Conjunct Response");
			break;
		case CONSEQUENT_DISJUNCT_RESPONSE:
			finishedMelody = consequentDisjunctResponse(m); //different skipwise melody, resolved
			System.out.println("Method 9, Consequent Disjunct Response");
			break;
		case MULTI_SEQUENCE_RESPONSE:
			finishedMelody = multiSequenceResponse(m); //makes a melody of short sequences
			System.out.println("Method 10, Multi-Sequence Response");
			break;
		case RANDOM_CACOPHONY:
			finishedMelody = randomCacophony(m); // a copy
			System.out.println("Method 11, Random Cacophony");
			break;
		}
		return(finishedMelody);
	}
	
	//runs a given melody through a random generator method
	static Melody generateMelodyFrom(Melody givenMelody)
	{
		Melody tempMel = givenMelody;
		int whichMethod = rnd.nextInt(totalNumOfMethods); 		
		Melody generatedMelody = new Melody(tempMel.notesInMelody);
		generatedMelody = composeUsingMethod(tempMel, whichMethod);	
		return(generatedMelody);		
	}
	
	//runs a given melody through a given generator method.  if its -1, 
	//runs a random method
	public static Melody generateMelodyFrom(Melody givenMelody, int meth)
	{
		int whichMethod = meth; 
		Melody tempMel = givenMelody;
		Melody generatedMelody = new Melody(tempMel.notesInMelody);
		if (whichMethod == -1)
		{
			int m = rnd.nextInt(totalNumOfMethods); 
			generatedMelody = composeUsingMethod(tempMel, m);
		}
		else
		{
			generatedMelody = composeUsingMethod(tempMel, whichMethod);
		}
		return(generatedMelody);		
	}
}
