package telran.multithreading;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class SenderReceiverAppl {
//TODO for HW #44 (ConsumerReceiver should not be updated)
	//Provide functionality of dispatching
	//Even messages must be processed by receiver threads with even id
	//Odd messages must be processed by receiver threads with odd id
	//Hints two message boxes: one for even messages and other for odd messages
	
	
	private static final int N_MESSAGES = 2000;
	private static final int N_RECEIVERS = 10;

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<String> messageBox1 = new LinkedBlockingQueue<String>();
		BlockingQueue<String> messageBox2 = new LinkedBlockingQueue<String>();
		@SuppressWarnings("unchecked")
		BlockingQueue<String>[] messageBoxes = new BlockingQueue[] {messageBox1,messageBox2};
		
		ProducerSender sender = startSender(messageBoxes, N_MESSAGES);
		ConsumerReceiver[] receivers = startReceivers(messageBoxes, N_RECEIVERS);
		sender.join();
		stopReceivers(receivers);
		displayResult(receivers);
		
		

	}

	private static void displayResult(ConsumerReceiver[] receivers)
	{
		System.out.printf("counter of processed messsages is %d\n",
				ConsumerReceiver.getMessagesCounter());
		for(int i=0;i<receivers.length;i++)
		{
			System.out.printf("%s ... %d\n", receivers[i].getName(),receivers[i].getLocalCounter());
		}
		
	}

	private static void stopReceivers(ConsumerReceiver[] receivers) throws InterruptedException {
		for(ConsumerReceiver receiver: receivers) {
			receiver.interrupt();
			receiver.join();
		}
		
	}

	private static ConsumerReceiver[] startReceivers(BlockingQueue<String>[] messageBoxes,
			int nReceivers) {
		ConsumerReceiver[] receivers = 
		IntStream.range(0, nReceivers).mapToObj(i -> {
			ConsumerReceiver receiver = new ConsumerReceiver();
			int index=getOddEvenIndex(receiver.getName());
			receiver.setMessageBox(messageBoxes[index]);
			return receiver;
		}).toArray(ConsumerReceiver[]::new);
		Arrays.stream(receivers).forEach(ConsumerReceiver::start);
		return receivers;
	}

	private static int getOddEvenIndex(String name)
	{
		String str=String.valueOf(name.charAt(name.length()-1));
		return Integer.parseInt(str)%2;
	}

	private static ProducerSender startSender(BlockingQueue<String>[] messageBoxes,
			int nMessages) {
		ProducerSender sender = new ProducerSender(messageBoxes, nMessages);
		sender.start();
		return sender;
	}

}
