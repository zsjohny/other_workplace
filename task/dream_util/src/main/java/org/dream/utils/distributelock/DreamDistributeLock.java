package org.dream.utils.distributelock;

/**
 * @author Boyce
 * 2016年6月22日 下午7:56:55 
 */
public class DreamDistributeLock extends AbstractLock{

	/**
	 * @param monitor
	 */
	public DreamDistributeLock(String monitor) {
		super(monitor);
	}

	@Override
	public void lock0() {
//		LockClient.reqLock(monitor);
	}

	@Override
	public void unlock0() {
		
	}

}
