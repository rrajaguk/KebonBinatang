package GUI.CommandProcessor;

import java.util.List;

import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

import GUI.MainWindow;
import Keeper.ZooKeeperConnection;

public class LockHerdCommandProcessor  implements SelectionListener, ChildrenCallback, Watcher {

	private Button CurrentButton;
	private ZooKeeperConnection conn;
	private boolean isNodeCreated;
	private int queueNumber;
	public final String LOCK_PREFIX = "Lock-";
	private String curNodeName;
	private ZooKeeper zk2 ;
	private MainWindow parent;
	public LockHerdCommandProcessor(Button btn, MainWindow _parent){
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
			queueNumber = getQueueNumber(zk2.getChildren("/", false));
			
			if (queueNumber > 0){
				// can't get the lock, watch out for the next change
				zk2.getChildren("/", true, this, null);
			}
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					CurrentButton.setEnabled(true);
					if (queueNumber == 0){
						CurrentButton.setText("Holding the lock, remove?");
					}else {
						CurrentButton.setText("Waiting in ["+queueNumber+"] remove?");					
					}
				}
			});
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
	private int getQueueNumber(List<String> children) {
		int result = 0;
		int currentID = Integer.parseInt(curNodeName.substring(LOCK_PREFIX.length()+1));
		for(String st : children){
			if (st.startsWith(LOCK_PREFIX)){
				int nodeID = Integer.parseInt(st.substring(LOCK_PREFIX.length()+1));
				if (nodeID < currentID){
					result++;
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
	public void processResult(int rc, String path, Object ctx,
			List<String> children) {
		queueNumber = getQueueNumber(children);
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				CurrentButton.setEnabled(true);
				if (queueNumber == 0){
					CurrentButton.setText("Holding the lock, remove?");
				}else {
					CurrentButton.setText("Waiting in ["+queueNumber+"] remove?");					
				}
			}
		});
		
		if (queueNumber > 0){
			// still can't get the lock, keep on watching
			try {
				zk2.getChildren("/",this);
			}  catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	public void process(WatchedEvent event) {
		parent.incrementHerdCounter();
		if (event.getType() == EventType.NodeChildrenChanged ){
			// process the change on child node
			zk2.getChildren("/", true, this, null);
		}
	}

}
