package GUI;
import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import GUI.CommandProcessor.ChildProcessor;
import GUI.CommandProcessor.ChildWatcher;
import GUI.CommandProcessor.EphemeralCommandProcessor;
import GUI.CommandProcessor.LockHerdCommandProcessor;
import GUI.CommandProcessor.LockNoHerdCommandProcessor;
import GUI.CommandProcessor.WatcherCommandProcessor;
import Keeper.ZooKeeperConnection;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class MainWindow {

	protected Shell shell;

	private static MainWindow window;

	public List list;
	public List EventList;
	private int herdCounter = 0;
	private int noHerdCounter = 0;
	private Text herdtext;
	private Text noHerdText;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	 public synchronized void incrementNoHerdCounter() {
	        //here you hold the mutex
		 noHerdCounter++;
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					noHerdText.setText(Integer.toString(noHerdCounter));
				}});
	    }

	 public synchronized void incrementHerdCounter() {
	        //here you hold the mutex
		 herdCounter++;
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					herdtext.setText(Integer.toString(herdCounter));
				}});
	    }

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(984, 360);
		shell.setText("SWT Application");
		

		list = new List(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		list.setBounds(23, 35, 260, 143);
		

		EventList = new List(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		EventList.setBounds(23, 203, 260, 109);
		
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new WatcherCommandProcessor(this,btnNewButton));
		btnNewButton.setBounds(304, 34, 166, 25);
		btnNewButton.setText("Start Monitoring ZooKeeper");
		
		Label lblCaughtEvents = new Label(shell, SWT.NONE);
		lblCaughtEvents.setBounds(23, 184, 104, 15);
		lblCaughtEvents.setText("Caught Events :");
		
		Label lblNodes = new Label(shell, SWT.NONE);
		lblNodes.setBounds(23, 14, 55, 15);
		lblNodes.setText("Nodes :");
		
		Button btnCreateEph_1 = new Button(shell, SWT.NONE);
		btnCreateEph_1.addSelectionListener(new EphemeralCommandProcessor(btnCreateEph_1));
		btnCreateEph_1.setText("Create Ephemeral");
		btnCreateEph_1.setBounds(304, 165, 225, 25);
		
		Button btnCreateEph_2 = new Button(shell, SWT.NONE);
		btnCreateEph_2.addSelectionListener(new EphemeralCommandProcessor(btnCreateEph_2));
		btnCreateEph_2.setText("Create Ephemeral");
		btnCreateEph_2.setBounds(304, 196, 225, 25);
		
		Button btnCreateEph_3 = new Button(shell, SWT.NONE);
		btnCreateEph_3.addSelectionListener(new EphemeralCommandProcessor(btnCreateEph_3));
		btnCreateEph_3.setText("Create Ephemeral");
		btnCreateEph_3.setBounds(304, 225, 225, 25);
		
		Button btnCreateEph_4 = new Button(shell, SWT.NONE);
		btnCreateEph_4.addSelectionListener(new EphemeralCommandProcessor(btnCreateEph_4));
		btnCreateEph_4.setText("Create Ephemeral");
		btnCreateEph_4.setBounds(304, 256, 225, 25);
		
		Button btnCreateEph_5 = new Button(shell, SWT.NONE);
		btnCreateEph_5.addSelectionListener(new EphemeralCommandProcessor(btnCreateEph_5));
		btnCreateEph_5.setText("Create Ephemeral");
		btnCreateEph_5.setBounds(304, 287, 225, 25);
		
		Button btnHerdGetLock_1 = new Button(shell, SWT.NONE);
		btnHerdGetLock_1.addSelectionListener(new LockHerdCommandProcessor(btnHerdGetLock_1,this));
		btnHerdGetLock_1.setBounds(568, 165, 165, 25);
		btnHerdGetLock_1.setText("Get Lock");
		
		Button btnHerdGetLock_2 = new Button(shell, SWT.NONE);
		btnHerdGetLock_2.addSelectionListener(new LockHerdCommandProcessor(btnHerdGetLock_2,this));
		btnHerdGetLock_2.setText("Get Lock");
		btnHerdGetLock_2.setBounds(568, 196, 165, 25);
		
		Button btnHerdGetLock_3 = new Button(shell, SWT.NONE);
		btnHerdGetLock_3.addSelectionListener(new LockHerdCommandProcessor(btnHerdGetLock_3,this));
		btnHerdGetLock_3.setText("Get Lock");
		btnHerdGetLock_3.setBounds(568, 225, 165, 25);
		
		Button btnHerdGetLock_4 = new Button(shell, SWT.NONE);
		btnHerdGetLock_4.addSelectionListener(new LockHerdCommandProcessor(btnHerdGetLock_4,this));
		btnHerdGetLock_4.setText("Get Lock");
		btnHerdGetLock_4.setBounds(568, 256, 165, 25);
		
		Button btnHerdGetLock_5 = new Button(shell, SWT.NONE);
		btnHerdGetLock_5.addSelectionListener(new LockHerdCommandProcessor(btnHerdGetLock_5,this));
		btnHerdGetLock_5.setText("Get Lock");
		btnHerdGetLock_5.setBounds(568, 287, 165, 25);
		
		herdtext = new Text(shell, SWT.BORDER);
		herdtext.setBounds(697, 138, 36, 21);
		
		Label lblNumOfEvents = new Label(shell, SWT.NONE);
		lblNumOfEvents.setText("Num of Events =  ");
		lblNumOfEvents.setBounds(568, 141, 104, 15);
		
		Label label = new Label(shell, SWT.NONE);
		label.setText("Num of Events =  ");
		label.setBounds(758, 141, 104, 15);
		
		noHerdText = new Text(shell, SWT.BORDER);
		noHerdText.setBounds(887, 138, 36, 21);
		
		Button btnNoHerdGetLock_1 = new Button(shell, SWT.NONE);
		btnNoHerdGetLock_1.addSelectionListener(new LockNoHerdCommandProcessor(btnNoHerdGetLock_1,this));
		btnNoHerdGetLock_1.setText("Get Lock");
		btnNoHerdGetLock_1.setBounds(758, 165, 165, 25);
		
		Button btnNoHerdGetLock_2 = new Button(shell, SWT.NONE);
		btnNoHerdGetLock_2.addSelectionListener(new LockNoHerdCommandProcessor(btnNoHerdGetLock_2,this));
		btnNoHerdGetLock_2.setText("Get Lock");
		btnNoHerdGetLock_2.setBounds(758, 196, 165, 25);
		
		Button btnNoHerdGetLock_3 = new Button(shell, SWT.NONE);
		btnNoHerdGetLock_3.addSelectionListener(new LockNoHerdCommandProcessor(btnNoHerdGetLock_3,this));
		btnNoHerdGetLock_3.setText("Get Lock");
		btnNoHerdGetLock_3.setBounds(758, 225, 165, 25);
		
		Button btnNoHerdGetLock_4 = new Button(shell, SWT.NONE);
		btnNoHerdGetLock_4.addSelectionListener(new LockNoHerdCommandProcessor(btnNoHerdGetLock_4,this));
		btnNoHerdGetLock_4.setText("Get Lock");
		btnNoHerdGetLock_4.setBounds(758, 256, 165, 25);
		
		Button btnNoHerdGetLock_5 = new Button(shell, SWT.NONE);
		btnNoHerdGetLock_5.addSelectionListener(new LockNoHerdCommandProcessor(btnNoHerdGetLock_5,this));
		btnNoHerdGetLock_5.setText("Get Lock");
		btnNoHerdGetLock_5.setBounds(758, 287, 165, 25);
		

	}
}
