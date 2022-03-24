package studiplayer.audio;

public class AudioFileFactory {

	public static AudioFile getInstance(String pathname) throws NotPlayableException {
		String endFile;
		try {
			int lastposi = pathname.lastIndexOf(".");
			endFile = pathname.substring(lastposi);
		} catch (Exception e) {
			throw new NotPlayableException(pathname, "Unknown suffix for AudioFile");
		}
		if (endFile.endsWith("3") || endFile.endsWith("g") || endFile.endsWith("G")) {
			return new TaggedFile(pathname);
		} else
			return new WavFile(pathname);
	}
}