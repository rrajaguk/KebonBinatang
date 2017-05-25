package GUI.CommandProcessor;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.eclipse.swt.widgets.Display;

import GUI.MainWindow;

public class ChildWatcher implements Watcher {


	private MainWindow disp;
	private ZooKeeper zk;
	private ChildProcessor cp;
	public ChildWatcher(MainWindow _disp, ZooKeeper _zk,ChildProcessor _cp){
		this.disp = _disp;
		this.zk = _zk;
		this.cp = _cp;
	}

	@Override
	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub
		Display.getDefault().asyncExec(new Runnable() {
			 public void run() {
				 disp.EventList.add("Receive event " +event.getType());
						 }
			});
		if (event.getType() == EventType.NodeChildrenChanged ){
			zk.getChildren("/", this, cp, null);	
		}
	}

}
