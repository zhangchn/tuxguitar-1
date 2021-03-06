/*
 * Created on 25-nov-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.app;

import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.action.TGActionManager;
import org.herac.tuxguitar.app.action.TGActionAdapterManager;
import org.herac.tuxguitar.app.action.TGActionLock;
import org.herac.tuxguitar.app.action.TGActionProcessor;
import org.herac.tuxguitar.app.action.impl.file.FileActionUtils;
import org.herac.tuxguitar.app.action.impl.file.NewFileAction;
import org.herac.tuxguitar.app.action.impl.system.DisposeAction;
import org.herac.tuxguitar.app.editors.EditorCache;
import org.herac.tuxguitar.app.editors.FretBoardEditor;
import org.herac.tuxguitar.app.editors.PianoEditor;
import org.herac.tuxguitar.app.editors.TGEditorManager;
import org.herac.tuxguitar.app.editors.TGRedrawEvent;
import org.herac.tuxguitar.app.editors.TGUpdateEvent;
import org.herac.tuxguitar.app.editors.TablatureEditor;
import org.herac.tuxguitar.app.editors.channel.TGChannelManagerDialog;
import org.herac.tuxguitar.app.editors.chord.CustomChordManager;
import org.herac.tuxguitar.app.editors.lyric.LyricEditor;
import org.herac.tuxguitar.app.editors.matrix.MatrixEditor;
import org.herac.tuxguitar.app.helper.FileHistory;
import org.herac.tuxguitar.app.helper.SyncThread;
import org.herac.tuxguitar.app.items.ItemManager;
import org.herac.tuxguitar.app.marker.MarkerList;
import org.herac.tuxguitar.app.system.config.TGConfigKeys;
import org.herac.tuxguitar.app.system.config.TGConfigManager;
import org.herac.tuxguitar.app.system.error.TGErrorAdapter;
import org.herac.tuxguitar.app.system.icons.TGIconManager;
import org.herac.tuxguitar.app.system.keybindings.KeyBindingActionManager;
import org.herac.tuxguitar.app.system.language.TGLanguageManager;
import org.herac.tuxguitar.app.system.properties.TGPropertiesAdapter;
import org.herac.tuxguitar.app.table.TGTableViewer;
import org.herac.tuxguitar.app.tools.browser.dialog.TGBrowserDialog;
import org.herac.tuxguitar.app.tools.scale.ScaleManager;
import org.herac.tuxguitar.app.tools.template.TGTemplate;
import org.herac.tuxguitar.app.tools.template.TGTemplateManager;
import org.herac.tuxguitar.app.transport.TGTransport;
import org.herac.tuxguitar.app.transport.TGTransportListener;
import org.herac.tuxguitar.app.undo.UndoableManager;
import org.herac.tuxguitar.app.util.ArgumentParser;
import org.herac.tuxguitar.app.util.TGFileUtils;
import org.herac.tuxguitar.app.util.TGSplash;
import org.herac.tuxguitar.app.util.WindowTitleUtil;
import org.herac.tuxguitar.document.TGDocumentManager;
import org.herac.tuxguitar.event.TGEvent;
import org.herac.tuxguitar.event.TGEventListener;
import org.herac.tuxguitar.event.TGEventManager;
import org.herac.tuxguitar.io.base.TGFileFormatManager;
import org.herac.tuxguitar.player.base.MidiPlayer;
import org.herac.tuxguitar.player.base.MidiPlayerException;
import org.herac.tuxguitar.player.impl.sequencer.MidiSequencerProviderImpl;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.TGException;
import org.herac.tuxguitar.util.TGLock;
import org.herac.tuxguitar.util.TGSynchronizer;
import org.herac.tuxguitar.util.error.TGErrorManager;
import org.herac.tuxguitar.util.plugin.TGPluginManager;
import org.herac.tuxguitar.util.properties.TGPropertiesManager;

/**
 * @author julian
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class TuxGuitar {
	
	public static final String APPLICATION_NAME = "TuxGuitar";
	
	public static final int MARGIN_WIDTH = 5;
	
	private static TuxGuitar instance;
	
	private boolean initialized;
	
	private TGLock lock;
	
	private Display display;
	
	private Shell shell;
	
	private TGContext context;
	
	private TGLanguageManager languageManager;
	
	private KeyBindingActionManager keyBindingManager;
	
	private TGIconManager iconManager;
	
	private EditorCache editorCache;
	
	private TablatureEditor tablatureEditor;
	
	private TGTableViewer table;
	
	private TGChannelManagerDialog channelManager;
	
	private TGTransport songTransport;
	
	private FretBoardEditor fretBoardEditor;
	
	private PianoEditor pianoEditor;
	
	private MatrixEditor matrixEditor;
	
	private LyricEditor lyricEditor;
	
	private TGBrowserDialog browser;
	
	private UndoableManager undoableManager;
	
	private ScaleManager scaleManager;
	
	private TGActionAdapterManager actionAdapterManager;
	
	private ItemManager itemManager;
	
	private CustomChordManager customChordManager;
	
	private FileHistory fileHistory;
	
	protected Sash sash;
	
	protected Composite sashComposite;
	
	public TuxGuitar() {
		this.lock = new TGLock();
		this.context = new TGContext();
		this.initialized = false;
	}
	
	public static TuxGuitar getInstance() {
		if (instance == null) {
			synchronized (TuxGuitar.class) {
				instance = new TuxGuitar();
			}
		}
		return instance;
	}
	
	private void initSynchronizer(){
		TGSynchronizer.instance().setController(new TGSynchronizer.TGSynchronizerController() {
			public void execute(final TGSynchronizer.TGSynchronizerTask task) {
				final Display display = getDisplay();
				if(display != null && !display.isDisposed()){
					display.syncExec(new Runnable() {
						public void run() throws TGException {
							task.run();
						}
					});
				}
			}
			
			public void executeLater(final TGSynchronizer.TGSynchronizerTask task) {
				final Display display = getDisplay();
				if(display != null && !display.isDisposed()){
					display.asyncExec(new Runnable() {
						public void run() throws TGException {
							task.run();
						}
					});
				}
			}
		});
	}
	
	public void displayGUI(String[] args) {
		//checkeo los argumentos
		ArgumentParser argumentParser = new ArgumentParser(args);
		if(argumentParser.processAndExit()){
			return;
		}
		
		// Priority 1 ----------------------------------------------//
		TGFileUtils.loadLibraries(this.context);
		TGFileUtils.loadClasspath();
		TGErrorAdapter.initialize(this.context);
		TGPropertiesAdapter.initialize(this.context);
		
		// Priority 2 ----------------------------------------------//
		Display.setAppName(APPLICATION_NAME);
		
		this.display = new Display();
		this.initSynchronizer();
		
		TGSplash.instance().init();
		
		this.shell = new Shell(getDisplay());
		this.shell.setLayout(getShellLayout());
		this.shell.setImage(getIconManager().getAppIcon());
		
		this.createComposites(getShell());
		
		// Priority 3 ----------------------------------------------//
		this.initMidiPlayer();
		this.getActionAdapterManager().initialize();
		this.getPluginManager().openPlugins();
		this.restoreControlsConfig();
		this.restorePlayerConfig();
		this.updateCache(true);
		this.showTitle();
		
		TGSplash.instance().finish();
		
		// Priority 4 ----------------------------------------------//
		this.shell.addShellListener(new TGActionProcessor(DisposeAction.NAME));
		this.shell.open();
		this.startSong(argumentParser);
		this.setInitialized( true );
		while (!getDisplay().isDisposed() && !getShell().isDisposed()) {
			if (!getDisplay().readAndDispatch()) {
				getDisplay().sleep();
			}
		}
		getDisplay().dispose();
	}
	
	private FormLayout getShellLayout(){
		FormLayout layout = new FormLayout();
		layout.marginWidth = MARGIN_WIDTH;
		layout.marginHeight = MARGIN_WIDTH;
		return layout;
	}
	
	private void startSong(final ArgumentParser parser){
		final URL url = parser.getURL();
		if(url != null){
			TGActionLock.lock();
			new SyncThread(new Runnable() {
				public void run() throws TGException {
					TuxGuitar.getInstance().loadCursor(SWT.CURSOR_WAIT);
					new Thread(new Runnable() {
						public void run() throws TGException {
							if(!TuxGuitar.isDisposed()){
								FileActionUtils.open(url);
								TuxGuitar.getInstance().loadCursor(SWT.CURSOR_ARROW);
								TGActionLock.unlock();
							}
						}
					}).start();
				}
			}).start();
		}else{
			TGSynchronizer.instance().executeLater(new TGSynchronizer.TGRunnable() {
				public void run() throws TGException {
					getActionManager().execute(NewFileAction.NAME);
				}
			});
		}
	}
	
	public void createComposites(Composite composite) {
		FormData data = new FormData();
		data.left = new FormAttachment(0,0);
		data.right = new FormAttachment(100,0);
		data.top = new FormAttachment(getItemManager().getCoolbar(),MARGIN_WIDTH);
		data.bottom = new FormAttachment(100,0);
		this.sashComposite = new Composite(composite,SWT.NONE);
		this.sashComposite.setLayout(new FormLayout());
		this.sashComposite.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(0,0);
		data.right = new FormAttachment(100,0);
		data.bottom = new FormAttachment(100,-150);
		data.height = MARGIN_WIDTH;
		this.sash = new Sash(this.sashComposite, SWT.HORIZONTAL);
		this.sash.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(0,0);
		data.right = new FormAttachment(100,0);
		data.top = new FormAttachment(0,0);
		data.bottom = new FormAttachment(this.sash, 0);
		getTablatureEditor().showTablature(this.sashComposite);
		getTablatureEditor().getTablature().setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(0,0);
		data.right = new FormAttachment(100,0);
		data.top = new FormAttachment(this.sash,0);
		data.bottom = new FormAttachment(100,0);
		getTable().init(this.sashComposite);
		getTable().getComposite().setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(0,0);
		data.right = new FormAttachment(100,0);
		data.top = new FormAttachment(this.sashComposite,0);
		data.bottom = new FormAttachment(100,0);
		
		Composite footer = new Composite(composite,SWT.NONE);
		footer.setLayout(new FormLayout());
		footer.setLayoutData(data);
		getFretBoardEditor().showFretBoard(footer);
		
		this.sash.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				TuxGuitar.this.sashComposite.layout(true,true);
			}
		});
		this.sash.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int maximumHeight = (TuxGuitar.this.sashComposite.getBounds().height - TuxGuitar.this.sash.getBounds().height);
				int height = (maximumHeight - event.y);
				height = Math.max(height,0);
				height = Math.min(height,maximumHeight);
				((FormData) TuxGuitar.this.sash.getLayoutData()).bottom = new FormAttachment(100, -height);
			}
		});
		this.sash.addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseEnter(MouseEvent e) {
				TuxGuitar.this.sash.setCursor( getDisplay().getSystemCursor( SWT.CURSOR_SIZENS ) );
			}
		});
		this.sashComposite.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event arg0) {
				FormData data = ((FormData) TuxGuitar.this.sash.getLayoutData());
				int height = -data.bottom.offset;
				int maximumHeight = (TuxGuitar.this.sashComposite.getBounds().height - TuxGuitar.this.sash.getBounds().height);
				if(height > maximumHeight){
					data.bottom = new FormAttachment(100, -maximumHeight);
				}
			}
		});
	}
	
	public void restoreControlsConfig(){
		final TGConfigManager config = getConfig();
		
		//---Main Shell---
		boolean maximized = config.getBooleanValue(TGConfigKeys.MAXIMIZED);
		getShell().setMaximized(maximized);
		if(!maximized){
			int width = config.getIntegerValue(TGConfigKeys.WIDTH);
			int height = config.getIntegerValue(TGConfigKeys.HEIGHT);
			if(width > 0 && height > 0){
				getShell().setSize(width,height);
			}
		}
		getShell().setMinimumSize(640,480);
		//---Fretboard---
		if(config.getBooleanValue(TGConfigKeys.SHOW_FRETBOARD)){
			getFretBoardEditor().showFretBoard();
		}else{
			getFretBoardEditor().hideFretBoard();
		}
		//---Instruments---
		if(config.getBooleanValue(TGConfigKeys.SHOW_INSTRUMENTS)){
			new SyncThread(new Runnable() {
				public void run() throws TGException {
					getChannelManager().show();
				}
			}).start();
		}
		//---Transport---
		if(config.getBooleanValue(TGConfigKeys.SHOW_TRANSPORT)){
			new SyncThread(new Runnable() {
				public void run() throws TGException {
					getTransport().show();
				}
			}).start();
		}
		//---Matrix---
		if(config.getBooleanValue(TGConfigKeys.SHOW_MATRIX)){
			new SyncThread(new Runnable() {
				public void run() throws TGException {
					getMatrixEditor().show();
				}
			}).start();
		}
		//---Piano---
		if(config.getBooleanValue(TGConfigKeys.SHOW_PIANO)){
			new SyncThread(new Runnable() {
				public void run() throws TGException {
					getPianoEditor().show();
				}
			}).start();
		}
		//---Markers---
		if(config.getBooleanValue(TGConfigKeys.SHOW_MARKERS)){
			new SyncThread(new Runnable() {
				public void run() throws TGException {
					MarkerList.instance().show();
				}
			}).start();
		}
	}
	
	public void setTableHeight(int value){
		int offset = ((FormData) getTable().getComposite().getLayoutData()).top.offset;
		int sashHeight = this.sash.getBounds().height;
		int maximumHeight = (this.sashComposite.getBounds().height - sashHeight);
		int height = (value + offset);
		height = Math.max( height,0);
		height = Math.min( height,maximumHeight);
		((FormData) TuxGuitar.this.sash.getLayoutData()).bottom = new FormAttachment(100, -height);
		this.sashComposite.layout(true,true);
	}
	
	public void updateShellFooter(int offset,int minimumWith,int minimumHeight){
		FormData data = ((FormData)this.sashComposite.getLayoutData());
		data.bottom.offset = -offset;
		getShell().setMinimumSize(Math.max(640,minimumWith),Math.max(480,minimumHeight));
		getShell().layout(true,true);
		getShell().redraw();
	}
	
	public TGTableViewer getTable(){
		if( this.table == null ){
			this.table = new TGTableViewer();
		}
		return this.table;
	}
	
	public TablatureEditor getTablatureEditor(){
		if( this.tablatureEditor == null ){
			this.tablatureEditor = new TablatureEditor();
		}
		return this.tablatureEditor;
	}
	
	public FretBoardEditor getFretBoardEditor(){
		if( this.fretBoardEditor == null ){
			this.fretBoardEditor = new FretBoardEditor();
		}
		return this.fretBoardEditor;
	}
	
	public PianoEditor getPianoEditor(){
		if( this.pianoEditor == null ){
			this.pianoEditor = new PianoEditor();
		}
		return this.pianoEditor;
	}
	
	public MatrixEditor getMatrixEditor(){
		if( this.matrixEditor == null ){
			this.matrixEditor = new MatrixEditor();
		}
		return this.matrixEditor;
	}
	
	public TGChannelManagerDialog getChannelManager(){
		if( this.channelManager == null ){
			this.channelManager = new TGChannelManagerDialog();
		}
		return this.channelManager;
	}
	
	public TGTransport getTransport(){
		if( this.songTransport == null ){
			this.songTransport = new TGTransport();
		}
		return this.songTransport;
	}
	
	public EditorCache getEditorCache(){
		if( this.editorCache == null ){
			this.editorCache = new EditorCache();
		}
		return this.editorCache;
	}
	
	public TGEditorManager getEditorManager(){
		return TGEditorManager.getInstance(this.context);
	}
	
	public LyricEditor getLyricEditor(){
		if( this.lyricEditor == null ){
			this.lyricEditor = new LyricEditor();
		}
		return this.lyricEditor;
	}
	
	public TGBrowserDialog getBrowser(){
		if( this.browser == null ){
			this.browser = new TGBrowserDialog();
		}
		return this.browser;
	}
	
	public UndoableManager getUndoableManager(){
		if( this.undoableManager == null ){
			this.undoableManager = new UndoableManager();
		}
		return this.undoableManager;
	}
	
	public ScaleManager getScaleManager(){
		if( this.scaleManager == null ){
			this.scaleManager = new ScaleManager(this.context);
		}
		return this.scaleManager;
	}
	
	public TGSongManager getSongManager(){
		return getDocumentManager().getSongManager();
	}
	
	public TGDocumentManager getDocumentManager(){
		return TGDocumentManager.getInstance(this.context);
	}
	
	public TGPluginManager getPluginManager(){
		return TGPluginManager.getInstance(this.context);
	}
	
	public TGErrorManager getErrorManager(){
		return TGErrorManager.getInstance(this.context);
	}
	
	public TGEventManager getEventManager(){
		return TGEventManager.getInstance(this.context);
	}
	
	public TGPropertiesManager getPropertiesManager(){
		return TGPropertiesManager.getInstance(this.context);
	}
	
	public TGConfigManager getConfig(){
		return TGConfigManager.getInstance(this.context);
	}
	
	public TGFileFormatManager getFileFormatManager(){
		return TGFileFormatManager.getInstance(this.context);
	}
	
	public TGIconManager getIconManager(){
		if( this.iconManager == null ){
			this.iconManager = new TGIconManager(this.context);
			this.iconManager.addLoader(new TGEventListener() {
				public void processEvent(TGEvent event) {
					getShell().setImage(getIconManager().getAppIcon());
					getShell().layout(true);
				}
			});
		}
		return this.iconManager;
	}
	
	public CustomChordManager getCustomChordManager(){
		if( this.customChordManager == null ){
			this.customChordManager = new CustomChordManager();
		}
		return this.customChordManager;
	}
	
	public ItemManager getItemManager() {
		if( this.itemManager == null ){
			this.itemManager = new ItemManager();
		}
		return this.itemManager;
	}
	
	public TGActionAdapterManager getActionAdapterManager() {
		if( this.actionAdapterManager == null ){
			this.actionAdapterManager = new TGActionAdapterManager();
		}
		return this.actionAdapterManager;
	}
	
	public TGActionManager getActionManager(){
		return TGActionManager.getInstance(this.context);
	}
	
	public TGLanguageManager getLanguageManager() {
		if( this.languageManager == null ){
			this.languageManager = new TGLanguageManager(this.context);
			this.loadLanguage();
		}
		return this.languageManager;
	}

	public KeyBindingActionManager getKeyBindingManager(){
		if( this.keyBindingManager == null ){
			this.keyBindingManager = new KeyBindingActionManager();
		}
		return this.keyBindingManager;
	}
	
	public FileHistory getFileHistory(){
		if( this.fileHistory == null ){
			this.fileHistory = new FileHistory();
		}
		return this.fileHistory;
	}
	
	public TGTemplateManager getTemplateManager(){
		return TGTemplateManager.getInstance(this.context);
	}
	
	public MidiPlayer getPlayer(){
		return MidiPlayer.getInstance(this.context);
	}
	
	public void initMidiPlayer(){
		MidiPlayer midiPlayer = MidiPlayer.getInstance(this.context);
		midiPlayer.init(getDocumentManager());
		midiPlayer.addListener( new TGTransportListener() );
		try {
			getPlayer().addSequencerProvider(new MidiSequencerProviderImpl(), false);
		} catch (MidiPlayerException e) {
			e.printStackTrace();
		}
	}
	
	public void restorePlayerConfig(){
		//try to open first device when the configured port not found.
		getPlayer().setTryOpenFistDevice( true );
		
		//check midi sequencer
		getPlayer().openSequencer(getConfig().getStringValue(TGConfigKeys.MIDI_SEQUENCER), true);
		
		//check midi port
		getPlayer().openOutputPort(getConfig().getStringValue(TGConfigKeys.MIDI_PORT), true);
	}
	
	public void showTitle(){
		new SyncThread(new Runnable() {
			public void run() throws TGException {
				if(!isDisposed()){
					getShell().setText(WindowTitleUtil.parseTitle());
				}
			}
		}).start();
	}
	
	public void updateCache(final boolean updateItems){
		if(!this.isLocked()){
			this.lock();
			this.getEditorCache().updateEditMode();
			this.unlock();
			new SyncThread(new Runnable() {
				public void run() throws TGException {
					if(!isDisposed() && !isLocked()){
						if(updateItems){
							lock();
							getEditorManager().doUpdate(TGUpdateEvent.SELECTION);
							unlock();
						}
						redraw();
					}
				}
			}).start();
		}
	}
	
	protected void redraw(){
		if(!isDisposed() && !this.isLocked()){
			this.lock();
			this.getEditorManager().doRedraw(TGRedrawEvent.NORMAL);
			this.unlock();
		}
	}
	
	public void redrawPlayingMode(){
		if(!isDisposed() && !this.isLocked()){
			this.lock();
			this.getEditorCache().updatePlayMode();
			this.getEditorManager().doRedraw( this.getEditorCache().shouldRedraw() ? TGRedrawEvent.PLAYING_NEW_BEAT : TGRedrawEvent.PLAYING_THREAD );
			this.unlock();
		}
	}
	
	public void showExternalBeat( TGBeat beat ){
		if(!isDisposed() && !this.isLocked()){
			this.lock();
			this.getEditorManager().showExternalBeat(beat);
			this.updateCache(true);
			this.unlock();
		}
	}
	
	public void hideExternalBeat(){
		if(!isDisposed() && !this.isLocked()){
			this.lock();
			this.getEditorManager().hideExternalBeat();
			this.updateCache(true);
			this.unlock();
		}
	}
	
	public Display getDisplay(){
		return this.display;
	}
	
	public Shell getShell(){
		return this.shell;
	}
	
	public static String getProperty(String key) {
		return TuxGuitar.getInstance().getLanguageManager().getProperty(key);
	}
	
	public static String getProperty(String key,String[] arguments) {
		return  TuxGuitar.getInstance().getLanguageManager().getProperty(key,arguments);
	}
	
	public static boolean isDisposed(){
		return (TuxGuitar.getInstance().getDisplay().isDisposed() || TuxGuitar.getInstance().getShell().isDisposed());
	}
	
	public void loadLanguage(){
		this.lock();
		
		getLanguageManager().setLanguage(getConfig().getStringValue(TGConfigKeys.LANGUAGE));
		
		this.unlock();
	}
	
	public void loadToolBars(){
		this.lock();
		
		getItemManager().createCoolbar();
		
		this.unlock();
	}
	
	public void loadStyles(){
		this.lock();
		
		getTablatureEditor().getTablature().reloadStyles();
		
		this.unlock();
	}
	
	public void loadSkin(){
		this.lock();
		
		getIconManager().reloadIcons();
		
		this.unlock();
	}
	
	public void newSong(){
		this.newSong(TuxGuitar.getInstance().getTemplateManager().getDefaultTemplate());
	}
	
	public void newSong(TGTemplate tgTemplate){
		TGSong tgSong = null;
		if( tgTemplate != null ){
			tgSong = TuxGuitar.getInstance().getTemplateManager().getTemplateAsSong(tgTemplate);
		}
		if( tgSong == null ){
			tgSong = TuxGuitar.getInstance().getSongManager().newSong();
		}
		TuxGuitar.getInstance().fireNewSong(tgSong,null);
	}
	
	public void fireNewSong(TGSong song,URL url){
		this.lock();
		
		getDocumentManager().setSong(song);
		getFileHistory().reset(url);
		getPlayer().reset();
		getPlayer().getMode().clear();
		getPlayer().resetChannels();
		getEditorCache().reset();
		getUndoableManager().discardAllEdits();
		getEditorManager().doUpdate(TGUpdateEvent.SONG_LOADED);
		
		this.unlock();
		
		updateCache(true);
		showTitle();
	}
	
	public void fireSaveSong(URL url){
		this.lock();
		
		getFileHistory().reset(url);
		getEditorCache().reset();
		getUndoableManager().discardAllEdits();
		getEditorManager().doUpdate(TGUpdateEvent.SONG_SAVED);
		
		this.unlock();
		
		updateCache(true);
		showTitle();
	}
	
	public void updateMeasure(int number){
		this.lock();
		this.getEditorManager().doUpdateMeasure(number);
		this.unlock();
	}
	
	public void updateSong(){
		this.lock();
		this.getEditorCache().reset();
		this.getEditorManager().doUpdate(TGUpdateEvent.SONG_UPDATED);
		this.unlock();
	}
	
	public void loadCursor(int style){
		this.loadCursor(getShell(),style);
	}
	
	public void loadCursor(final Control control,final int style){
		try {
			TGSynchronizer.instance().execute(new TGSynchronizer.TGRunnable() {
				public void run() throws TGException {
					if(!control.isDisposed()){
						control.setCursor(getDisplay().getSystemCursor(style));
					}
				}
			});
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void playBeat( final TGBeat beat ){
		new Thread(new Runnable() {
			public void run() throws TGException {
				if(!isDisposed() && !getPlayer().isRunning() ){
					getPlayer().playBeat(beat);
				}
			}
		}).start();
	}
	
	public boolean isInitialized() {
		return this.initialized;
	}
	
	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
	
	public TGContext getContext() {
		return context;
	}

	public void lock(){
		this.lock.lock();
	}
	
	public void unlock(){
		this.lock.unlock();
	}
	
	public boolean isLocked(){
		return this.lock.isLocked();
	}
}