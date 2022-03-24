package studiplayer.audio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

@SuppressWarnings("serial")
public class PlayList extends LinkedList<AudioFile> {
	protected int index;
	protected boolean random;

	public PlayList() {

	}

	public PlayList(String pathname) {
		index = 0;
		random = true;
		loadFromM3U(pathname);
	}

	public int getCurrent() {
		return index;
	}

	public void setCurrent(int current) {
		index = current;
	}

	public AudioFile getCurrentAudioFile() {
		// using information in Throws-description
		if ((index < 0 || index >= size())) {
			return null;
		}
		return this.get(index);
	}

	public void changeCurrent() {
		if (index < size() - 1) {
			index++;
		} else {
			if (index == this.lastIndexOf(getCurrentAudioFile()) || getCurrentAudioFile() == null) {
				index = 0;
			}
			if (random == true) {
				Collections.shuffle(this);
			}
		}
	}

	public void setRandomOrder(boolean randomOrder) {
		if (random == true) {
			Collections.shuffle(this);
		} else {
			random = randomOrder;
		}
	}

	public void saveAsM3U(String pathname) {
		FileWriter writer = null;
		String fname = pathname;
		String linesep = System.getProperty("line.separator");
		try {
			writer = new FileWriter(fname);
			for (AudioFile oj : this)
				writer.write(oj.path + linesep);
		} catch (IOException e) {
			throw new RuntimeException("Unable to write to file" + fname + ":" + e.getMessage());
		} finally {
			try {
				writer.close();
			} catch (Exception e) {

			}
		}
	}

	public void loadFromM3U(String pathname) {
		this.clear();
		String fname = pathname;
		Scanner scanner = null;
		String line;
		try {
			scanner = new Scanner(new File(fname));
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				if (!line.trim().isEmpty() && !line.trim().startsWith("#")) {
					try {
						this.add(AudioFileFactory.getInstance(line));
					} 
					catch (NotPlayableException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				scanner.close();
			} catch (Exception e) {

			}
		}

	}

	public void sort(SortCriterion order) {
			if(SortCriterion.AUTHOR == order) {
				Collections.sort(this, new AuthorComparator());
			}
			if(SortCriterion.TITLE == order) {
				Collections.sort(this, new TitleComparator());
			}
			if(SortCriterion.ALBUM == order) {
				Collections.sort(this, new AlbumComparator());
			}
			if(SortCriterion.DURATION == order) {
				Collections.sort(this, new DurationComparator());
			}
		
	}

}
