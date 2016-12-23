package com.runesource.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.IntStream;

public final class IndexQueue {

	private final Deque<Integer> container = new ArrayDeque<>();
	
	public IndexQueue(int capacity) {
		IntStream.rangeClosed(1, capacity).forEach(container::add);
	}
	
	public void openValue(int value) {
		container.push(value);
	}
	
	public int nextValue() {
		return container.poll();
	}
}
