package studiplayer.ui;

import java.net.URL;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import studiplayer.audio.AudioFile;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.PlayList;

public class Player extends Application {

	private static Stage primaryStage;
	private Label songDescription = null;
	private Label playTime;
	private Button playButton;
	private Button pauseButton;
	private Button stopButton;
	private Button nextButton;
	private Button editorButton;
	private PlayList playList = new PlayList();
	public static final String DEFAULT_PLAYLIST = "playlists/DefaultPlayList.m3u";
	List<String> parameters;
	private String playListPathname;
	private String startTime = "00:00"; // final
	private String currentTitle = "Current song: ";
	private volatile boolean stopped;
	private PlayListEditor playListEditor;
	private boolean editorVisible;
	private String defaultDescription = "no current Song";

	public Player() {
	}

	// @Override
	public void start(Stage primaryStage) throws Exception {

		// create components for window
		Player.primaryStage = primaryStage;
		BorderPane mainPane = new BorderPane();
		playButton = createButton("play.png");
		playButton.setOnAction(e -> {
			playCurrentSong();
		});
		pauseButton = createButton("pause.png");
		pauseButton.setOnAction(e -> {
			pauseCurrentSong();
		});
		stopButton = createButton("stop.png");
		stopButton.setOnAction(e -> {
			stopCurrentSong();
		});
		nextButton = createButton("next.png");
		nextButton.setOnAction(e -> {
			nextFollowingSong();
		});
		editorButton = createButton("pl_editor.png");
		editorButton.setOnAction(e -> {
			if (this.editorVisible) {
				this.editorVisible = false;
				this.playListEditor.hide();
			} else {
				this.editorVisible = true;
				this.playListEditor.show();
			}
		});
		setButtonStates(false, false, false, false, false); 

		parameters = getParameters().getRaw();
		playListEditor = new PlayListEditor(this, this.playList);
		editorVisible = false;

		// check for arguments
		if (parameters.size() >= 1) {
			playList = new PlayList(parameters.get(0));
			playListPathname = parameters.get(0);
		} else {
			playList = new PlayList(Player.DEFAULT_PLAYLIST);
			playListPathname = Player.DEFAULT_PLAYLIST;
		}
		if (!playList.isEmpty()) {
			currentTitle = playList.getCurrentAudioFile().toString();
			defaultDescription = "";
			startTime = "00:00";
		}
		// ---------------------------------------------------------------------

		songDescription = new Label(currentTitle + defaultDescription);
		playTime = new Label(startTime);
		refreshUI();

		// position components in window
		HBox hbox = new HBox();
		mainPane.setTop(songDescription);
		hbox.getChildren().addAll(playTime, playButton, pauseButton, stopButton, nextButton, editorButton);
		mainPane.setCenter(hbox);
		Scene scene = new Scene(mainPane, 700, 90);
		primaryStage.setScene(scene);
		primaryStage.show();
		// ---------------------------------------------------------------------
	}

	void playCurrentSong() {
		this.stopped = false;
		setButtonStates(true, true, true, true, true);
		refreshUI();
		updateSongInfo(playList.getCurrentAudioFile());
		if (playList.getCurrentAudioFile() != null) {
			(new TimerThread()).start();
			(new PlayerThread()).start();
		}

		refreshUI();

	}

	private void stopCurrentSong() {
		this.stopped = true;
		if (playList.getCurrentAudioFile() != null) {
			playList.getCurrentAudioFile().stop();
		}
		updateSongInfo(playList.getCurrentAudioFile());
		playTime.setText("00:00");

		setButtonStates(false, false, false, false, false); // Negation
		refreshUI();
	}

	private void pauseCurrentSong() {
		refreshUI();
		if (playList.getCurrentAudioFile() != null) {
			playList.getCurrentAudioFile().togglePause();
		}
		setButtonStates(true, true, true, true, true);
	}

	private void nextFollowingSong() {
		refreshUI();
		if (!stopped) {
			stopCurrentSong();
		}
		playList.changeCurrent();
		playCurrentSong();
		updateSongInfo(playList.getCurrentAudioFile());
		setButtonStates(true, true, true, true, true);
	}

	private void updateSongInfo(AudioFile af) {
		if (af != null && stopped == false) {
			primaryStage.setTitle(currentTitle);
			currentTitle = playList.getCurrentAudioFile().toString();
			startTime = playList.getCurrentAudioFile().getFormattedPosition();
		} else if (af != null && stopped == true) {
			primaryStage.setTitle(currentTitle);
			currentTitle = playList.getCurrentAudioFile().toString();
			startTime = "00:00";
		} else {
			primaryStage.setTitle("no current song");
			startTime = "--:--";
		}
	}

	private void refreshUI() {
		Platform.runLater(() -> {
			if (playList != null && playList.size() > 0) {
				updateSongInfo(playList.getCurrentAudioFile());
				songDescription.setText(this.currentTitle);
				playTime.setText(this.startTime);
			} else {
				updateSongInfo(null);
				setButtonStates(false, false, false, false, false);
			}
		});
	}

	public void setDisabled(boolean b) {
		if (b == true) {
			playButton.setDisable(true);
			pauseButton.setDisable(false);
			stopButton.setDisable(false);
			nextButton.setDisable(false);
			editorButton.setDisable(false);
		} else {
			playButton.setDisable(false);
			pauseButton.setDisable(true);
			stopButton.setDisable(true);
			nextButton.setDisable(false);
			editorButton.setDisable(false);
		}
	}

	private void setButtonStates(boolean playButtonState, boolean stopButtonState, boolean nextButtonState,
			boolean pauseButtonState, boolean editorButtonState) {

		setDisabled(playButtonState);
		setDisabled(stopButtonState);
		setDisabled(nextButtonState);
		setDisabled(pauseButtonState);
		setDisabled(editorButtonState);

	}

	public void setPlayList(String playListPath) {
		if (playListPath == null) {
			playList.clear();
			refreshUI();
		} else {
			if (playListPath.isEmpty()) {
				playList.clear();
				refreshUI();
			} else {

				playList = new PlayList(playListPath);
				refreshUI();
			}
		}
	}

	public static void main(String[] args) {
		launch(args);

	}

	private Button createButton(String iconfile) {
		Button button = null;
		try {
			URL url = getClass().getResource("/icons/" + iconfile);
			Image icon = new Image(url.toString());
			ImageView imageView = new ImageView(icon);
			imageView.setFitHeight(48);
			imageView.setFitWidth(48);
			button = new Button("", imageView);
			button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		} catch (Exception e) {
			System.out.println("Image " + "icons/" + iconfile + "not found!");
			System.exit(-1);
		}
		return button;
	}

	// Setter & Getter
	public String getPlayListPathname() {
		return this.playListPathname;
	}

	public void setEditorVisible(boolean editorVisible) {
		this.editorVisible = editorVisible;
	}

	// Inner Thread-Classes here
	// update position
	private class TimerThread extends Thread {

		public void run() {
			while (stopped == false && !playList.isEmpty()) {
				try {
					refreshUI();
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// play audio files
	private class PlayerThread extends Thread {
		public void run() {
			while (!playList.isEmpty() && stopped == false) {
				try {
					playList.getCurrentAudioFile().play();
					setButtonStates(true, true, true, true, true);
					refreshUI();
				} catch (NotPlayableException e) {
					e.printStackTrace();
				}
				if (stopped == false) {
					playList.changeCurrent();
					updateSongInfo(playList.getCurrentAudioFile());
				}
			}
		}
	}

}
