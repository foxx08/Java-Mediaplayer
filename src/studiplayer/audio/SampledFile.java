package studiplayer.audio;

import studiplayer.basic.BasicPlayer;

public abstract class SampledFile extends AudioFile {
	protected String album;

	public SampledFile() {
		super();
	}

	public SampledFile(String s) throws NotPlayableException {
		super(s);
	}

	public void play() throws NotPlayableException {
		try {
			BasicPlayer.play(getPathname());
		} catch (Exception e) {
			throw new NotPlayableException(getPathname(), e);
		}
	}

	public void togglePause() {
		BasicPlayer.togglePause();
	}

	public void stop() {
		BasicPlayer.stop();
	}

	public String getFormattedDuration() {
		return timeFormatter(duration);
	}

	public String getFormattedPosition() {
		Long position = BasicPlayer.getPosition();
		return timeFormatter(position);
	}

	public static String timeFormatter(long microtime) {
		if (microtime < 0) {
			throw new RuntimeException("Negative Time value provided");
		}
		long seconds = microtime / 1000000;
		if (seconds > 5999) {
			throw new RuntimeException("Time value exceeds allowed format");
		}
		long minutes = seconds / 60;
		seconds = seconds - (minutes * 60);
		String newtime = "";
		if (minutes < 10) {
			newtime = "0" + minutes;
		} else {
			newtime = "" + minutes;
		}
		newtime = newtime + ":";
		if (seconds < 10) {
			newtime = newtime + "0" + seconds;
		} else {
			newtime = newtime + "" + seconds;
		}
		return newtime;
	}
}
