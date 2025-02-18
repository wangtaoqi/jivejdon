package com.jdon.jivejdon.spi.component.mapreduce;

import com.jdon.jivejdon.domain.model.ForumThread;

import java.util.Comparator;
import java.util.concurrent.TimeUnit;

/**
 * Home page approve threads Comparator
 */
public class HomePageComparator implements Comparator<ForumThread> {
	@Override
	public int compare(ForumThread thread1, ForumThread thread2) {

		if (thread1.getThreadId().longValue() == thread2.getThreadId().longValue())
			return 0;

		double countRs1 = algorithm(thread1, thread2);
		double countRs2 = algorithm(thread2, thread1);

		return compareResult(countRs1, countRs2, thread1, thread2);
	}

	private int compareResult(double countRs1, double countRs2, ForumThread thread1, ForumThread thread2) {
		if (countRs1 > countRs2)
			return -1; // returning the first object
		else if (countRs1 < countRs2)
			return 1;
		else if (countRs1 == countRs2) {
			if (thread1.getThreadId() > thread2.getThreadId())
				return -1;
			else if (thread1.getThreadId() < thread2.getThreadId())
				return 1;
		}
		return 0;
	}

	private double algorithm(ForumThread thread, ForumThread threadPrev) {
		double p = thread.getViewCount();
		if (thread.getRootMessage().getDigCount() > 0)
			p = p * thread.getRootMessage().getDigCount();

		long diffInMillis = Math.abs(System.currentTimeMillis() - thread.getState().getModifiedDate2());
		long diff = TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS);
		if (diff > 48)
			p = p / diff;

		long diff2 = thread.getViewCount() - thread.getViewCounter().getLastSavedCount();
		if (diff2 > 2)
			p = p * diff2;

		return p;
	}
}
