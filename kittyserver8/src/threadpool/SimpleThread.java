package threadpool;

public class SimpleThread extends Thread {
	private boolean runningFlag; // 运行的状态false
	private Taskable task; // 要执行的操作
	private int threadNumber; // 线程的编�?
	private MyNotify myNotify; // 通知接口

	// 标志runningFlag用以�?��线程
	public boolean isRunning() {
		return this.runningFlag;
	}

	public synchronized void setRunning(boolean flag) {
		this.runningFlag = flag; // 设置当前线程为true，表示当前线程已经被占用
		if (flag) {
			this.notifyAll(); // 唤醒其他线程就绪
		}
	}

	public Taskable getTask() {
		return task;
	}

	public void setTask(Taskable task) {
		this.task = task;
	}

	// 提示哪个线程工作
	public  SimpleThread(int threadNumber,MyNotify notify) {
		runningFlag = false;
		this.threadNumber = threadNumber;
		System.out.println("Thread " + threadNumber + " started.");
		this.myNotify = notify;
	}

	public synchronized void run() {
		try {
			while (true) {
				if (!runningFlag) { // 无线循环
					this.wait(); // 判断标志位是否为true，如果runningFlag为false，等待调�?
				} else {
					System.out.println("线程"+threadNumber+"开始执行任务");
					Object returnValue=this.task.doTask();
					
					if (myNotify!=null) {
						myNotify.notifyResult(returnValue);
					}
					sleep(5000);
					System.out.println("线程" + threadNumber + "重新准备...");
					setRunning(false);
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Interrupt");
		}
	}

}
