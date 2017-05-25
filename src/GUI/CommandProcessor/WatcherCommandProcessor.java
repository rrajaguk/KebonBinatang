package GUI.CommandProcessor;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

import GUI.MainWindow;
import Keeper.ZooKeeperConnection;

public class WatcherCommandProcessor implements SelectionListener  {

	private static ZooKeeperConnection conn;
	private static ZooKeeper zk;
	private MainWindow disp;
	private Button CurrentButton;
	
	public WatcherCommandProcessor(MainWindow _disp,Button btn){
		CurrentButton= btn;
		this.disp = _disp;		
	}
	@Override
	public void widgetSelected(SelectionEvent e) {
		Thread thread = new Thread(){
		    public void run(){
		    	try {
		    		Display.getDefault().asyncExec(new Runnable() {
		    			public void run() {
		    				CurrentButton.setEnabled(false);
		    				CurrentButton.setText("Connecting to Zookeeper....");;
		    			}});
					conn = new ZooKeeperConnection();
					zk = conn.connect("localhost");
					String path = "/";
					Stat stat = zk.exists(path, false);
					if (stat != null){
						ChildProcessor cp = new ChildProcessor(disp);
						ChildWatcher cw = new ChildWatcher(disp,zk,cp);
						zk.getChildren(path, cw, cp, null);								
					}			
		    		Display.getDefault().asyncExec(new Runnable() {
		    			public void run() {
		    				CurrentButton.setEnabled(false);
		    				CurrentButton.setText("Connected");;
		    			}});
				}catch (Exception e1){
					System.out.println(e1.getMessage());
				}
		    }
		  };

		  thread.start();
		  
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
