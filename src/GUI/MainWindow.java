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
import GUI.CommandProcessor.LockCommandProcessor;
import GUI.CommandProcessor.WatcherCommandProcessor;
import Keeper.ZooKeeperConnection;

import org.eclipse.swt.widgets.Label;


public class MainWindow {

	protected Shell shell;

	private static MainWindow window;

	public List list;
	public List EventList;
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

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(789, 360);
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
		
		Button btnGetLock_1 = new Button(shell, SWT.NONE);
		btnGetLock_1.addSelectionListener(new LockCommandProcessor(btnGetLock_1));
		btnGetLock_1.setBounds(568, 165, 165, 25);
		btnGetLock_1.setText("Get Lock");
		
		Button btnGetLock_2 = new Button(shell, SWT.NONE);
		btnGetLock_2.addSelectionListener(new LockCommandProcessor(btnGetLock_2));
		btnGetLock_2.setText("Get Lock");
		btnGetLock_2.setBounds(568, 196, 165, 25);
		
		Button btnGetLock_3 = new Button(shell, SWT.NONE);
		btnGetLock_3.addSelectionListener(new LockCommandProcessor(btnGetLock_3));
		btnGetLock_3.setText("Get Lock");
		btnGetLock_3.setBounds(568, 225, 165, 25);
		
		Button btnGetLock_4 = new Button(shell, SWT.NONE);
		btnGetLock_4.addSelectionListener(new LockCommandProcessor(btnGetLock_4));
		btnGetLock_4.setText("Get Lock");
		btnGetLock_4.setBounds(568, 256, 165, 25);
		
		Button btnGetLock_5 = new Button(shell, SWT.NONE);
		btnGetLock_5.addSelectionListener(new LockCommandProcessor(btnGetLock_5));
		btnGetLock_5.setText("Get Lock");
		btnGetLock_5.setBounds(568, 287, 165, 25);
		

	}
}
