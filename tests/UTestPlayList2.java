import static org.junit.Assert.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import studiplayer.audio.AudioFile;
import studiplayer.audio.PlayList;
import studiplayer.audio.SortCriterion;
import studiplayer.audio.TaggedFile;
import studiplayer.audio.WavFile;


public class UTestPlayList2 {

	@Test @Ignore
	public void test_getCurrentAudioFile_01() throws Exception {
		PlayList pl = new PlayList();
		assertEquals("Wrong current AudioFile", null, pl.getCurrentAudioFile());
	}
	
	@Test @Ignore
	public void test_getCurrentAudioFile_02() throws Exception {
		PlayList pl = new PlayList();
		TaggedFile tf0 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");
		pl.add(tf0);
		pl.setCurrent(10);
		assertEquals("Wrong current AudioFile", null, pl.getCurrentAudioFile());
	}
	
	@Test @Ignore
	public void test_getCurrentAudioFile_04() throws Exception {
		PlayList pl = new PlayList();
		TaggedFile tf0 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");
		TaggedFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
		pl.add(tf0);
		pl.add(tf1);
		pl.setCurrent(1);
		assertEquals("Wrong current AudioFile", tf1, pl.getCurrentAudioFile());
		pl.remove(0); 
		assertEquals("Wrong current AudioFile", null, pl.getCurrentAudioFile());
	}
	
	@Test @Ignore
	public void test_changeCurrent_01() throws Exception {
		PlayList pl = new PlayList();
		TaggedFile tf0 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");
		TaggedFile tf1 = new TaggedFile("audiofiles/tanom p2 journey.mp3");
		TaggedFile tf2 = new TaggedFile("audiofiles/Rock 812.mp3");
		pl.add(tf0);
		pl.add(tf1);
		pl.add(tf2);
		pl.setCurrent(0);
		assertEquals("Wrong current index", 0, pl.getCurrent());
		pl.changeCurrent();
		assertEquals("Wrong change in current index", 1, pl.getCurrent());
		pl.changeCurrent();
		assertEquals("Wrong change in current index", 2, pl.getCurrent());
		pl.changeCurrent();
		assertEquals("Wrong change in current index", 0, pl.getCurrent());
	}
	
	@Test @Ignore
	public void test_changeCurrent_02() throws Exception {
		PlayList pl = new PlayList();
		TaggedFile f0 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");
		TaggedFile f1 = new TaggedFile("audiofiles/tanom p2 journey.mp3");
		TaggedFile f2 = new TaggedFile("audiofiles/Rock 812.mp3");
		WavFile f4 = new WavFile("audiofiles/wellenmeister - tranquility.wav");
		pl.add(f0); pl.add(f1); pl.add(f2); pl.add(f4);
		pl.setRandomOrder(true);
		for(int i = 0; i < 5 * pl.size(); i++) {
			System.out.printf("Pos=%d Filename =%s\n", pl.getCurrent(),
			pl.getCurrentAudioFile().getFilename());
			assertEquals("Wrong current index", i % pl.size(), pl.getCurrent());
			pl.changeCurrent();
			if(pl.getCurrent() == 0) System.out.println("");
		}
	}
	
	@Test @Ignore
	public void test_WriteLinesToFiles_01() throws Exception {
		FileWriter writer = null;
		String fname = "file.txt";
		//String linesep = System.getProperty("line.separator");
		String linesep = "\n";
		try {
			writer = new FileWriter(fname);
			writer.write("Line1" + linesep);
			writer.write("Line2" + linesep);
			writer.write("Line3" + linesep);
		} catch (IOException e) {
			throw new RuntimeException(
					"Unable to write to file" + fname + ":" + e.getMessage());
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
				
			}
		}
	}
	
	@Ignore
	public void test_ReadLinesFromFile_01() throws Exception {
		String fname = "file.txt";
		Scanner scanner = null;
		String line;
		try {
			scanner = new Scanner(new File(fname));
			int i = 1;
			while(scanner.hasNextLine()) {
				line = scanner.nextLine();
				System.out.println("Got line " + i + "|" + line + "|");
				i++;
			}
		}catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				scanner.close();
			} catch (Exception e) {
				
			}
		}
		
	}
	
	@Test
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
	
	//@SuppressWarnings("deprecation")
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
}


