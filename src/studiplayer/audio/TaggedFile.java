package studiplayer.audio;

import studiplayer.basic.TagReader;
import java.util.Map;

public class TaggedFile extends SampledFile {
	protected String album;

	public TaggedFile() {
		super();
	}

	public TaggedFile(String s) throws NotPlayableException {
		super(s);
		readAndStoreTags();
	}

	public String getAlbum() {
		return album;
	}

	public void readAndStoreTags() throws NotPlayableException {
		try {
			Map<String, Object> tagMap = TagReader.readTags(getPathname());
			for (String key : tagMap.keySet()) {
				if (tagMap.get(key) != null && tagMap.get(key) != "" && tagMap.get(key) != "0") {
					switch (key) {
					case "author":
						String newauthor = (String) tagMap.get(key);
						author = newauthor.trim();
						break;
					case "title":
						String newtitle = (String) tagMap.get(key);
						title = newtitle.trim();
						break;
					case "album":
						String newalbum = (String) tagMap.get(key);
						album = newalbum.trim();
						break;
					case "duration":
						long newduration = (Long) tagMap.get(key);
						duration = newduration;
					}
				}

			}
		} catch (Exception e) {
			throw new NotPlayableException(getPathname(), e);
		}

	}

	@Override
	public String toString() {
		if (album == null) {
			return super.toString() + " - " + getFormattedDuration();
		}
		return super.toString() + " - " + album + " - " + getFormattedDuration();
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
		if (album.equals(null)) {
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

}
