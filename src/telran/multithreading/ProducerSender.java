package telran.multithreading;

import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

public class ProducerSender extends Thread {
	//HW #44 definition
	//dispatching functionality
	//two message boxes
	//even messages should be put to even message box
	//odd messages should be put to odd message box
	BlockingQueue<String>[] messageBoxes;
	private int nMessages;

	public ProducerSender(BlockingQueue<String>[] messageBoxes, int nMessages) {
		this.messageBoxes = messageBoxes;
		this.nMessages = nMessages;
	}
	public void run()
	{
		for(int i=1;i<=nMessages;i++)
		{
			String str="message" + i;
			try {
				messageBoxes[i%2].put(str);
			} catch (InterruptedException e) {
				//no interrupt logics
			}
			
		}
	}
	
}
