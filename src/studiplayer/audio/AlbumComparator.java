package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {

		try {
			if ((o1 instanceof TaggedFile == false) && (o2 instanceof TaggedFile)) {
				return -1;
			}
			if ((o1 instanceof TaggedFile) && (o2 instanceof TaggedFile == false)) {
				return 1;
			}
			if ((o1 instanceof TaggedFile) && (o2 instanceof TaggedFile)) {
				return ((AudioFile) o1).album.compareTo(((AudioFile) o2).album);
			} else {
				if ((o1 instanceof TaggedFile) && (o2 instanceof WavFile)) {

					return ((TaggedFile) o1).getAlbum().compareTo(((WavFile) o2).album);

				}

			}

		} catch (Exception e) {
			throw new NullPointerException("Values are NULL!");
		}
		return 0;
	}
}
