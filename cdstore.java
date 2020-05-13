package forclass;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class CDstore{
	//private ArrayList<LinkedList<Integer>>forsale=new ArrayList<LinkedList<Integer>>();
	//private LinkedList<Integer>[]forsale=new LinkedList<Integer>()[10];
	LinkedList[] forsale=new LinkedList[10];
	void init() {
		for(int i=0;i<10;i++) {
			LinkedList<Integer>d=new LinkedList<Integer>();
			forsale[i]=d;
		}
	}
	private LinkedList<Integer>forrent=new LinkedList<Integer>();
	 boolean checkforsalefull() {
		// if(forsale.size()!=10) {return false;}
		 for(int i=0;i<10;i++) {
			 int len=forsale[i].size();
			 if(len<10)return false;
		 }
		return true;
	};
	boolean checkfornull(int i) {
		if(forsale[i].size()==0)return true;
		return false;
	}
	public synchronized void buy(String ThreadName) {
		
		while(checkforsalefull()) {
			try {
				System.out.println(ThreadName+"发现：仓库满，可出");
				this.wait();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.notifyAll();
		for(int i=0;i<10;i++) {
			LinkedList<Integer> s = new LinkedList<Integer>();
			for(int j=0;j<10;j++) {
				s.add(1);
			}
			forsale[i]=s;
		}
		for(int i=0;i<10;i++) {
			forrent.add(1);
		}
		/*Random r= new Random(1);
		int ran=r.nextInt(10);
		ran=ran%10;
		forsale[ran].add(1);*/
		System.out.println("进货加满");
	}
	public synchronized void sale(String ThreadName,int i) {
		while(forsale[i].size()==0) {
			try {
				System.out.println("售空，请加满");
				this.wait();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
			this.notifyAll();
			Object p=0;
			p= forsale[i].pollFirst();
			System.out.println(ThreadName+"买走"+i+"当前这种库存量"+forsale[i].size());
	}
	
	public synchronized void rent(String ThreadName,int i) {
		while(forrent.size()==0) {
			try {
				System.out.println("等待归还");
				this.wait();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.notifyAll();
		forrent.pollFirst();
		System.out.println(ThreadName+"借走一个");
	}
	public synchronized void bback(String ThreadName,int i) {
		while(forrent.size()==10) {
			try {
				System.out.println("借槽满");
				this.wait();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.notifyAll();
		forrent.add(1);
		System.out.println(ThreadName+"归还一个");
	}
}
class producer implements Runnable{
	//public volatile Integer Id=0;
	public volatile String name;
	CDstore st;
	boolean flag=true;
	public producer(CDstore s,String name) {
		this.st=s;
		this.name=name;
	}
	@Override
	public void run() {
		while(flag) {
			st.buy(name);
			try {
				Thread.sleep(1000);
			}catch(InterruptedException e){
	            e.printStackTrace();
	        }
		}
	}
}

class consumer implements Runnable{
	public volatile String name;
	CDstore st;
	public consumer(CDstore s,String name) {
		this.st=s;
		this.name=name;
	}
	boolean flag=true;
	
	
	@Override
	public void run() {
		Random r= new Random();
	   int ran;
		while(flag) {
		ran=r.nextInt(200);
		   try {
			   Thread.sleep(ran);
		   }catch(InterruptedException e){
	           e.printStackTrace();
	       }
	   for(int i=0;i<10;i++) {
			ran=r.nextInt(100);
			ran=ran%10;
			st.sale(name, ran);
		}
	   
	   }
	}
}

class renter implements Runnable{
	public volatile String name;
	CDstore st;
	public renter(CDstore s,String name) {
		this.st=s;
		this.name=name;
	}
	boolean flag=true;
	
	
	@Override
	public void run() {
		Random r= new Random();
	   int ran;
	   int q;
		while(flag) {
		   ran=r.nextInt(200);
		   try {
			   Thread.sleep(ran);
		   }catch(InterruptedException e){
	           e.printStackTrace();
	       }
			q=ran%10;
			st.rent(name, q);
			ran=r.nextInt(100);
			ran=ran+200;
			try {
				Thread.sleep(ran);
			}catch(InterruptedException e){
	            e.printStackTrace();
	        }
			st.bback(name, q);
		}
	}
}



 public class Test {
	 
	 
	 
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long sta=System.currentTimeMillis();
		CDstore res=new CDstore();
		res.init();
		producer p=new producer(res,"producer");
		consumer c1=new consumer(res,"c1");
		consumer c2=new consumer(res,"c2");
		consumer c3=new consumer(res,"c3");
		
		renter r1=new renter(res,"r1");
		renter r2=new renter(res,"r2");
		
		Thread t1=new Thread(p,"生产1");
		Thread t2=new Thread(c1,"销售1");
		Thread t3=new Thread(c2,"销售2");
		Thread t4=new Thread(c3,"销售3");
		Thread t5=new Thread(r1,"借阅1");
		Thread t6=new Thread(r2,"借阅2");
		t5.start();
		 t6.start();
		 t1.start();
		 t2.start();
		 t3.start();
		 t4.start();
		 long nowt=System.currentTimeMillis();
		 while(nowt!=sta+120000) {
			 nowt=System.currentTimeMillis();
		 }
		 System.out.println("程序准备退出了！");
	     System.exit(0);
	}	
}

 
 
 


 
 
 
 
 
 
 
 
 
 
 
 
 
















