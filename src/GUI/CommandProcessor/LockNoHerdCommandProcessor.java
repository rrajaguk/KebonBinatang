package GUI.CommandProcessor;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

import GUI.MainWindow;
import Keeper.ZooKeeperConnection;


public class LockNoHerdCommandProcessor  implements SelectionListener,  Watcher {

	private Button CurrentButton;
	private ZooKeeperConnection conn;
	private boolean isNodeCreated;
	private String nodeBefore;
	public final String LOCK_PREFIX = "LockNoHerd-";
	private String curNodeName;
	private ZooKeeper zk2 ;
	private MainWindow parent;
	public LockNoHerdCommandProcessor(Button btn, MainWindow _parent){
		CurrentButton= btn;
		isNodeCreated = false;
		parent = _parent;
	}
	@Override
	public void widgetSelected(SelectionEvent e) {
		Thread thread = new Thread(){
			public void run(){
				if (!isNodeCreated){
					TryToObtainLock();
				}else {
					ReleaseLock();
				}
			}

		};
		thread.start();

	}
	private void ReleaseLock() {
		try {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					CurrentButton.setText("Releasing lock.... ");
					CurrentButton.setEnabled(false);
				}});
			conn.close();
			conn = null;
			isNodeCreated = false;
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					CurrentButton.setText("Get Lock");
					CurrentButton.setEnabled(true);
				}});
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
	private void TryToObtainLock() {
		try {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					CurrentButton.setText("Getting lock.... ");
					CurrentButton.setEnabled(false);
				}});
			conn = new ZooKeeperConnection();
			zk2 = conn.connect("localhost");
			curNodeName = zk2.create("/"+LOCK_PREFIX, "data loh".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL_SEQUENTIAL);
			isNodeCreated = true;
			checkLock();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
	private void checkLock() throws KeeperException, InterruptedException {
		nodeBefore = getNodeBefore(zk2.getChildren("/", false));
		
		if (!nodeBefore.equals(curNodeName)){
			// can't get the lock, watch out for the deletion of the node before this
			zk2.exists("/"+nodeBefore, this);
		}
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				CurrentButton.setEnabled(true);
				if (nodeBefore.equals(curNodeName)){
					CurrentButton.setText("Holding the lock, remove?");
				}else {
					CurrentButton.setText("Waiting for [" + nodeBefore +"] remove?");					
				}
			}
		});
	}
	private String getNodeBefore(List<String> children) {
		int currentID = Integer.parseInt(curNodeName.substring(LOCK_PREFIX.length()+1));
		String result = curNodeName;
		int Biggest = Integer.MIN_VALUE;
		for(String st : children){
			if (st.startsWith(LOCK_PREFIX)){
				int nodeID = Integer.parseInt(st.substring(LOCK_PREFIX.length()+1));
				if (nodeID < currentID){
					if (Biggest != Math.max(Biggest, nodeID)){
						result = st;
						Biggest = nodeID;
					}
				}
			}
		}
		return result;
	}
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void process(WatchedEvent event) {
		parent.incrementNoHerdCounter();
		// TODO Auto-generated method stub
		if (event.getType() == EventType.NodeDeleted){
			try {
				checkLock();
			} catch (KeeperException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}