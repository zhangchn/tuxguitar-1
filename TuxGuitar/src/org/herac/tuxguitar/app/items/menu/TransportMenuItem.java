/*
 * Created on 02-dic-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.app.items.menu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.action.TGActionProcessor;
import org.herac.tuxguitar.app.action.impl.transport.TransportCountDownAction;
import org.herac.tuxguitar.app.action.impl.transport.TransportMetronomeAction;
import org.herac.tuxguitar.app.action.impl.transport.TransportModeAction;
import org.herac.tuxguitar.app.action.impl.transport.TransportPlayAction;
import org.herac.tuxguitar.app.action.impl.transport.TransportSetLoopEHeaderAction;
import org.herac.tuxguitar.app.action.impl.transport.TransportSetLoopSHeaderAction;
import org.herac.tuxguitar.app.action.impl.transport.TransportStopAction;
import org.herac.tuxguitar.app.items.MenuItems;
import org.herac.tuxguitar.player.base.MidiPlayerMode;
import org.herac.tuxguitar.song.models.TGMeasure;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TransportMenuItem extends MenuItems{
	private static final int STATUS_STOPPED = 1;
	private static final int STATUS_PAUSED = 2;
	private static final int STATUS_RUNNING = 3;
	
	private MenuItem transportMenuItem;
	private Menu menu;
	private MenuItem play;
	private MenuItem stop;
	private MenuItem metronome;
	private MenuItem countDown;
	private MenuItem mode;
	private MenuItem loopSHeader;
	private MenuItem loopEHeader;
	
	private int status;
	
	public TransportMenuItem(Shell shell,Menu parent, int style) {
		this.transportMenuItem = new MenuItem(parent, style);
		this.menu = new Menu(shell, SWT.DROP_DOWN);
	}
	
	public void showItems(){
		this.play = new MenuItem(this.menu,SWT.PUSH);
		this.play.addSelectionListener(new TGActionProcessor(TransportPlayAction.NAME));
		
		this.stop = new MenuItem(this.menu, SWT.PUSH);
		this.stop.addSelectionListener(new TGActionProcessor(TransportStopAction.NAME));
		
		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);
		
		this.metronome = new MenuItem(this.menu, SWT.CHECK);
		this.metronome.addSelectionListener(new TGActionProcessor(TransportMetronomeAction.NAME));
		
		this.countDown = new MenuItem(this.menu, SWT.CHECK);
		this.countDown.addSelectionListener(new TGActionProcessor(TransportCountDownAction.NAME));
		
		this.mode = new MenuItem(this.menu, SWT.PUSH);
		this.mode.addSelectionListener(new TGActionProcessor(TransportModeAction.NAME));
		
		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);
		
		this.loopSHeader = new MenuItem(this.menu, SWT.CHECK);
		this.loopSHeader.addSelectionListener(new TGActionProcessor(TransportSetLoopSHeaderAction.NAME));
		
		this.loopEHeader = new MenuItem(this.menu, SWT.CHECK);
		this.loopEHeader.addSelectionListener(new TGActionProcessor(TransportSetLoopEHeaderAction.NAME));
		
		this.transportMenuItem.setMenu(this.menu);
		
		this.status = STATUS_STOPPED;
		this.loadIcons();
		this.loadProperties();
	}
	
	public void update(){
		TGMeasure measure = TuxGuitar.getInstance().getTablatureEditor().getTablature().getCaret().getMeasure();
		MidiPlayerMode pm = TuxGuitar.getInstance().getPlayer().getMode();
		this.metronome.setSelection(TuxGuitar.getInstance().getPlayer().isMetronomeEnabled());
		this.countDown.setSelection(TuxGuitar.getInstance().getPlayer().getCountDown().isEnabled());
		this.loopSHeader.setEnabled( pm.isLoop() );
		this.loopSHeader.setSelection( measure != null && measure.getNumber() == pm.getLoopSHeader() );
		this.loopEHeader.setEnabled( pm.isLoop() );
		this.loopEHeader.setSelection( measure != null && measure.getNumber() == pm.getLoopEHeader() );
		this.loadIcons(false);
	}
	
	public void loadProperties(){
		setMenuItemTextAndAccelerator(this.transportMenuItem, "transport", null);
		setMenuItemTextAndAccelerator(this.play, "transport.start", TransportPlayAction.NAME);
		setMenuItemTextAndAccelerator(this.stop, "transport.stop", TransportStopAction.NAME);
		setMenuItemTextAndAccelerator(this.mode, "transport.mode", TransportModeAction.NAME);
		setMenuItemTextAndAccelerator(this.metronome, "transport.metronome", TransportMetronomeAction.NAME);
		setMenuItemTextAndAccelerator(this.countDown, "transport.count-down", TransportCountDownAction.NAME);
		setMenuItemTextAndAccelerator(this.loopSHeader, "transport.set-loop-start", TransportSetLoopSHeaderAction.NAME);
		setMenuItemTextAndAccelerator(this.loopEHeader, "transport.set-loop-end", TransportSetLoopEHeaderAction.NAME);
	}
	
	public void loadIcons(){
		this.loadIcons(true);
		this.mode.setImage(TuxGuitar.getInstance().getIconManager().getTransportMode());
		this.metronome.setImage(TuxGuitar.getInstance().getIconManager().getTransportMetronome());
	}
	
	public void loadIcons(boolean force){
		int lastStatus = this.status;
		
		if(TuxGuitar.getInstance().getPlayer().isRunning()){
			this.status = STATUS_RUNNING;
		}else if(TuxGuitar.getInstance().getPlayer().isPaused()){
			this.status = STATUS_PAUSED;
		}else{
			this.status = STATUS_STOPPED;
		}
		
		if(force || lastStatus != this.status){
			if(this.status == STATUS_RUNNING){
				this.stop.setImage(TuxGuitar.getInstance().getIconManager().getTransportIconStop2());
				this.play.setImage(TuxGuitar.getInstance().getIconManager().getTransportIconPause());
			}else if(this.status == STATUS_PAUSED){
				this.stop.setImage(TuxGuitar.getInstance().getIconManager().getTransportIconStop2());
				this.play.setImage(TuxGuitar.getInstance().getIconManager().getTransportIconPlay2());
			}else if(this.status == STATUS_STOPPED){
				this.stop.setImage(TuxGuitar.getInstance().getIconManager().getTransportIconStop1());
				this.play.setImage(TuxGuitar.getInstance().getIconManager().getTransportIconPlay1());
			}
		}
	}
}
