package studiplayer.audio;

import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile {

	public WavFile() {
		super();
	}

	public WavFile(String Pfad) throws NotPlayableException {
		super(Pfad);
		readAndSetDurationFromFile(getPathname());
	}

	public void readAndSetDurationFromFile(String pathname) throws NotPlayableException {
		try {
			WavParamReader.readParams(getPathname());
		} catch (Exception e) {
			throw new NotPlayableException(pathname, e);
		}
		long numbers = WavParamReader.getNumberOfFrames();
		float framerate = WavParamReader.getFrameRate();
		duration = computeDuration(numbers, framerate);

	}

	public String toString() {
		String s = super.toString();
		return s + " - " + getFormattedDuration();
	}

	public String[] fields() {
		if (author.equals(null)) {
			String fields[] = { "", title, album, getFormattedDuration() };
			return fields;
		}
		if (title.equals(null)) {
			String fields[] = { author, "", album, getFormattedDuration() };
			return fields;
		}
		if (this.album == null) {
			String fields[] = { author, title, "", getFormattedDuration() };
			return fields;
		}
		if (getFormattedDuration().equals(null)) {
			String fields[] = { author, title, album, "" };
			return fields;
		}
		String fields[] = { author, title, album, getFormattedDuration() };
		return fields;
	}

	public static long computeDuration(long numberOfFrames, float frameRate) {
		return (long) (numberOfFrames / frameRate * 1000000);
	}
}
