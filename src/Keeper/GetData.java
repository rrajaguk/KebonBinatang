package Keeper;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;


public class GetData {

	private static ZooKeeper zk;
	private static ZooKeeperConnection conn;
	public static void main(String[] args) {
	 //   String path = "/MyFirstZnode";
	    final CountDownLatch connectedSignal = new CountDownLatch(10);
	    try {
	    	conn = new ZooKeeperConnection();
	    	zk = conn.connect("localhost");
	    	Stat stat = zk.exists("/NodeOne", true);
	    	if (stat != null){
	    		byte[] b = zk.getData("/NodeOne",new Watcher(){

					@Override
					public void process(WatchedEvent event) {
						if (event.getType() == EventType.None){
							if (event.getState() == KeeperState.Expired){
								connectedSignal.countDown();
							}
						}else {
							String path = "/NodeOne";
							try {
								byte[] bn = zk.getData(path, false, null);
								String data = new String(bn,"UTF-8");
								System.out.println(data);
								connectedSignal.countDown();
							}catch (Exception ex){
								System.out.println(ex.getMessage());
							}
						}
						
					}
	    			
	    		},null);
	    		String data = new String(b,"UTF-8");
	    		System.out.println(data);
	    		connectedSignal.await();
	    	}
	    }catch (Exception ex){
	    	System.out.println(ex.getMessage());
	    }

	}

}
