package Keeper;
import java.util.List;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;


public class GetNode {

	private static ZooKeeper zk;
	private static ZooKeeperConnection conn;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "/";
		try {
			conn = new ZooKeeperConnection();
			zk = conn.connect("localhost");
			Stat stat = zk.exists(path, true);
			if (stat != null){
				System.out.println(stat.getNumChildren());
				List<String> childs =  zk.getChildren(path, true);
				for (String child : childs){
					System.out.print(child);
					Stat childStat = zk.exists("/"+child, true);
					if (childStat!= null){
						 System.out.println("Num Children = " +childStat.getNumChildren());
					}
				}
			}			
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

}
