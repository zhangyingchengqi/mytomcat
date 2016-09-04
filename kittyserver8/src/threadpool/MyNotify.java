package threadpool;
//通知接口；用于�?知主线程，任务线程已经运行结�?
public interface MyNotify {
	public void notifyResult(Object result);
}
