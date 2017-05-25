package GUI.CommandProcessor;

import java.util.List;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.data.Stat;
import org.eclipse.swt.widgets.Display;

import GUI.MainWindow;

public class ChildProcessor implements AsyncCallback.Children2Callback{

	private MainWindow disp;
	public ChildProcessor(MainWindow _disp){
		this.disp = _disp;

	}
	@Override
	public void processResult(int rc, String path, Object ctx,
			List<String> children, Stat stat) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				disp.list.removeAll();
				for (String child : children){
					disp.list.add(child);
				}
			}});
	}

}
