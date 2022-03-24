package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		try {
			if ((o1 instanceof SampledFile == false) && (o2 instanceof SampledFile)) {
				return -1;
			}
			if ((o1 instanceof SampledFile) && (o2 instanceof SampledFile == false)) {
				return 1;
			} else

				return ((SampledFile) o1).getFormattedDuration().compareTo(((SampledFile) o2).getFormattedDuration());

		} catch (Exception e) {
			throw new NullPointerException("Values are NULL!");
		}

	}
}
