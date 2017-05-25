package GUI.CommandProcessor;


import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

import Keeper.ZooKeeperConnection;

public class EphemeralCommandProcessor implements SelectionListener {

	private Button CurrentButton;
	private ZooKeeperConnection conn;
	private boolean isNodeCreated;
	public EphemeralCommandProcessor(Button btn){
		CurrentButton= btn;
		isNodeCreated = false;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		Thread thread = new Thread(){
			public void run(){
				if (!isNodeCreated){
					createNewZNode();
				}else {
					deleteZNode();
				}
			}};
			thread.start();
	}

	private void deleteZNode(){
		try {
			Display.getDefault().asyncExec(new Runnable() {
				 public void run() {
						CurrentButton.setEnabled(false);
						CurrentButton.setText("Busy Deleting node.....");
							 }});
			conn.close();
			conn = null;
			isNodeCreated = false;
			Display.getDefault().asyncExec(new Runnable() {
				 public void run() {
					CurrentButton.setEnabled(true);
					CurrentButton.setText("Create Ephemeral");
							 }
				});
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	private void createNewZNode(){
		try {
			Display.getDefault().asyncExec(new Runnable() {
				 public void run() {
						CurrentButton.setText("Busy Creating node.....");
					CurrentButton.setEnabled(false);
							 }});
			conn = new ZooKeeperConnection();
			ZooKeeper zk2 = conn.connect("localhost");
			String nodeName = zk2.create("/Epen", "data loh".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL_SEQUENTIAL);
			isNodeCreated = true;
			Display.getDefault().asyncExec(new Runnable() {
				 public void run() {
					CurrentButton.setEnabled(true);
					CurrentButton.setText(nodeName.substring(1)+" is Created, remove?");
							 }
				});
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

}
