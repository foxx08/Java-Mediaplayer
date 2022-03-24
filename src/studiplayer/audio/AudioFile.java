package studiplayer.audio;

import java.io.File;

public abstract class AudioFile {
	protected String path; // path
	protected String file; // file
	protected String author;
	protected String title;
	protected long duration;
	protected String album = "";

	// default Konstruktor
	public AudioFile() {
	}

	public AudioFile(String pathname) throws NotPlayableException {
		parsePathname(pathname);
		File f = new File(getPathname());
		if (!f.canRead()) {
			throw new NotPlayableException(pathname, "File cant be read");
		}
		getFilename();
		parseFilename(file);
		getAuthor();
		getTitle();
	}

	public void parsePathname(String pathname) {
		char separatorChar = System.getProperty("file.separator").charAt(0);
		String separatorString = Character.toString(separatorChar);
		// Leerer String
		if (pathname.isEmpty()) {
			path = "";
		} else {
			// Betriebssystem
			if (isWindows()) {
				separatorString = separatorString + separatorString;
				path = pathname.replaceAll("[\\/\\\\]+", separatorString);
			} else {
				path = pathname.replaceAll("[\\/\\\\]+", separatorString);
				if (pathname.contains(":")) {
					path = '/' + pathname.replaceAll(":", "").replaceAll("[\\/\\\\]+", separatorString);

				}
			}
		}
		if (path.contains(Character.toString(separatorChar))) {
			int filestart = path.lastIndexOf(Character.toString(separatorChar));
			file = path.substring(filestart + 1);
		} else {
			file = path;
		}
	}

	public void parseFilename(String filename) {

		if (!filename.trim().equals("-")) {
			filename = filename.trim();

			if (filename.contains(" - ") && filename.contains(".")) {
				int strichposi = filename.indexOf(" - ");

				if (filename.contains(".")) {
					int punktposi = filename.lastIndexOf(".");
					author = filename.substring(0, strichposi).trim();
					title = filename.substring(strichposi + 2, punktposi).trim();
				} else {
					author = filename.substring(0, strichposi).trim();
					title = filename.substring(strichposi + 2).trim();
				}
			} else if (filename.contains(".") && !filename.endsWith(" - ")) { //
				int punktposi = filename.lastIndexOf(".");
				author = "";
				title = filename.substring(0, punktposi).trim();
			} else {
				author = "";
				title = filename;
			}

		} else {
			if (filename.equals("-")) {
				author = "";
				title = filename.trim();
			} else {
				author = "";
				title = "";
			}
		}
	}

	public String getPathname() {
		return path;
	}

	public String getFilename() {
		return file;
	}

	public String getAuthor() {
		return author;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		if (getAuthor().equals("")) {
			return title;
		} else {
			return author + " - " + title;
		}
	}

	private static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
	}

	public abstract void play() throws NotPlayableException; // Abspielvorgang starten

	public abstract void togglePause(); // Pausierung an- oder ausschalten

	public abstract void stop(); // Abspielvorgang beenden

	public abstract String getFormattedDuration(); // Anzeige: Gesamtspieldauer eines Liedes

	public abstract String getFormattedPosition(); // Anzeige: aktuelle Position im gerade gespielten Lied

	public abstract String[] fields();
}
