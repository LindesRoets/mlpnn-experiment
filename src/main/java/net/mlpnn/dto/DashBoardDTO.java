
package net.mlpnn.dto;

/**
 *
 * @author Lindes Roets
 */
public class DashBoardDTO {

	private long runningThreadCount;

	private long pausedThreadCount;

	private long stoppedThreadCount;

	public long getRunningThreadCount() {
		return runningThreadCount;
	}

	public void setRunningThreadCount(long runningThreadCount) {
		this.runningThreadCount = runningThreadCount;
	}

	public long getPausedThreadCount() {
		return pausedThreadCount;
	}

	public void setPausedThreadCount(long pausedThreadCount) {
		this.pausedThreadCount = pausedThreadCount;
	}

	public long getStoppedThreadCount() {
		return stoppedThreadCount;
	}

	public void setStoppedThreadCount(long stoppedThreadCount) {
		this.stoppedThreadCount = stoppedThreadCount;
	}


}
