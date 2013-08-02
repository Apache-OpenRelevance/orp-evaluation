package org.orp.eval.test;

public class testThread implements Runnable{

	@Override
	public void run() {
		for(int i = 0; i < 10; i ++){
			System.out.println("It is thread.");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
