package org.herac.tuxguitar.document;

import org.herac.tuxguitar.graphics.control.TGFactoryImpl;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.singleton.TGSingletonFactory;
import org.herac.tuxguitar.util.singleton.TGSingletonUtil;

public class TGDocumentManager {
	
	private TGSongManager songManager;
	private TGSong song;
	
	private TGDocumentManager() {
		this.songManager = new TGSongManager(new TGFactoryImpl());
		this.song = this.songManager.newSong();
	}
	
	public TGSongManager getSongManager() {
		return songManager;
	}

	public TGSong getSong() {
		return song;
	}
	
	public void setSong(TGSong song) {
		if( song != null ){
			this.clearSong();
			this.song = song;
		}
	}

	public void clearSong() {
		if( this.getSong() != null ){
			this.getSongManager().clearSong(this.getSong());
		}
	}
	
	public static TGDocumentManager getInstance(TGContext context) {
		return (TGDocumentManager) TGSingletonUtil.getInstance(context, TGDocumentManager.class.getName(), new TGSingletonFactory() {
			public Object createInstance(TGContext context) {
				return new TGDocumentManager();
			}
		});
	}
}
