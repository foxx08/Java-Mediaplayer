import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import studiplayer.audio.AudioFile;
import studiplayer.audio.AudioFileFactory;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.PlayList;
import studiplayer.audio.SortCriterion;
import studiplayer.audio.TaggedFile;
import studiplayer.audio.WavFile;

public class UTestAudioFileFactory {

	@Test @Ignore
	public void test_getInstance_01() throws Exception {
		try {
			AudioFileFactory.getInstance("unkown.xxx");
			fail("Unknow suffix; excepting exception");	
		} catch (NotPlayableException e) {
			// E
		}
	}
	
	@Test @Ignore
	public void test_getInstance_02() throws Exception {
		try {
			AudioFileFactory.getInstance("nonexistent.mp3");
			fail("File is not readable; excepting exception");
		} catch (NotPlayableException e) {
			//E
		}
	}
	
	@Ignore
	public void test_getInstance_03() throws Exception {
		AudioFile af1 = AudioFileFactory.getInstance("audiofiles/Eisbach Deep Snow.ogg");
		assertTrue("Expecting object of type TaggedFile",
				(af1 instanceof TaggedFile));
		
		AudioFile af2 = AudioFileFactory.getInstance("audiofiles/wellenmeister - tranquility.wav");
		assertTrue("Expecting object of type WavFile",
				(af2 instanceof WavFile));
		
		AudioFile af3 = AudioFileFactory.getInstance("audiofiles/special.oGg");
		assertTrue("Expecting object of type TaggedFile", 
				(af3 instanceof TaggedFile));
	}
	
	@Test @Ignore
	public void test_loadFromM3U_02() throws Exception {
		String m3u_pathname = "playlist.m3u";
		String mp3_pathname = "corrupt.mp3";
		
		FileWriter writer = null;
		try {
			writer = new FileWriter(m3u_pathname);
			writer.write(mp3_pathname + System.getProperty("line.separator"));
		} catch (IOException e) {
			throw new RuntimeException("Unable to store M3U file:" + m3u_pathname, e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				
			}
		}
		PlayList pl = new PlayList();
		pl.loadFromM3U(m3u_pathname);
		new File(m3u_pathname).delete();
	}
	
	@Test
	public void test_sort_byTitle_01() throws Exception {
		PlayList pl1 = new PlayList();
		pl1.add(new TaggedFile("audiofiles/Eisbach Deep Snow.ogg"));
		pl1.add(new WavFile("audiofiles/wellenmeister - tranquility.wav"));
		pl1.add(new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
		pl1.add(new TaggedFile("audiofiles/tanom p2 journey.mp3"));
		pl1.add(new TaggedFile("audiofiles/Rock 812.mp3"));
		pl1.sort(SortCriterion.TITLE);
		String exp[] = new String[] {
				"Eisbach - Deep Snow - The Sea, the Sky - 03:18",
				"Eisbach - Rock 812 - The Sea, the Sky - 05:31",
				"Wellenmeister - TANOM Part I: Awakening - TheAbsoluteNecessityOfMeaning - 05:55",
				"Wellenmeister - TANOM Part II: Journey - TheAbsoluteNecessityOfMeaning - 02:52",
				"wellenmeister - tranquility - 02:21" 
		};
		String sorted[] = new String[5];
		int i = 0;
		for (AudioFile af : pl1) {
			sorted[i] = af.toString();
			i++;
		}
		assertArrayEquals("Wrong sorting by title", exp, sorted);
	}
	
	@Test 
	public void test_sort_byDuration_01() throws Exception {
		PlayList pl1 = new PlayList();
		pl1.add(new TaggedFile("audiofiles/Eisbach Deep Snow.ogg"));
		pl1.add(new WavFile("audiofiles/wellenmeister - tranquility.wav"));
		pl1.add(new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
		pl1.add(new TaggedFile("audiofiles/tanom p2 journey.mp3"));
		pl1.add(new TaggedFile("audiofiles/Rock 812.mp3"));
		pl1.sort(SortCriterion.ALBUM);
		String exp[] = new String[] {
				"wellenmeister - tranquility - 02:21",
				"Wellenmeister - TANOM Part II: Journey - TheAbsoluteNecessityOfMeaning - 02:52",
				"Eisbach - Deep Snow - The Sea, the Sky - 03:18",
				"Eisbach - Rock 812 - The Sea, the Sky - 05:31",
				"Wellenmeister - TANOM Part I: Awakening - TheAbsoluteNecessityOfMeaning - 05:55" 
		};
		String sorted[] = new String[5];
		int i = 0;
		for (AudioFile af : pl1) {
			sorted[i] = af.toString();
			i++;
		}
		assertArrayEquals("Wrong sorting by title", exp, sorted);
	}
	
	
}
