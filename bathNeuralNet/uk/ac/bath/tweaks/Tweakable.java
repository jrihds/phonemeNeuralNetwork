package uk.ac.bath.tweaks;

import java.util.*;

abstract public class Tweakable extends Observable{
	Number n;

	Comparable min;

	Comparable max;

	String label;

	Number stepSize;

	Tweakable(String label, Number n, Comparable min, Comparable max,
			Number stepSize) {
		this.label = label;
		this.n = n;
		this.stepSize = stepSize;
		this.min = min;
		this.max = max;
	}

	Tweakable(Collection<Tweakable> c, String label, Number n, Comparable min,
			Comparable max, Number step) {
		this(label, n, min, max, step);
		c.add(this);
	}

	public String getLabel() {
		return label;
	}

	public Number getNumber() {
		return n;
	}

	public int intValue() {
		return n.intValue();
	}

	public double doubleValue() {
		return n.doubleValue();
	}

	public Comparable getMinimum() {
		return min;
	}

	public Comparable getMaximum() {
		return max;
	}

	public Number getStepSize() {
		return stepSize;
	}

	public abstract void set(String s);

	public void set(Number n) {
		this.n = n;
		setChanged();
		notifyObservers();
	}

	public String toString() {
		return n.toString();
	}
}
