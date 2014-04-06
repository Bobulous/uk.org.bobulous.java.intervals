/*
 * Copyright © 2014 Bobulous <http://www.bobulous.org.uk/>.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/.
 */
package uk.org.bobulous.java.intervals;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * A set of disjoint intervals of the same type. Disjoint means that no interval
 * held within an <code>IntervalSet</code> will ever intersect with any of the
 * other intervals held within the same <code>IntervalSet</code>.
 * <p>
 * An <code>IntervalSet</code> can only be produced by using the
 * {@link IntervalSet.Builder} class. The <code>Builder</code> type is mutable
 * and allows additional intervals to be added to its set, replacing
 * intersecting intervals with their union so that the resulting set is always
 * disjoint. A <code>Builder</code> can produce an immutable
 * <code>IntervalSet</code> at any time.</p>
 * <p>
 * <code>IntervalSet</code> will only be immutable if objects of the basis type
 * <code>T</code> are immutable. So <code>IntervalSet&lt;Double&gt;</code> is
 * immutable, but <code>IntervalSet&lt;Date&gt;</code> is not, because
 * <code>Date</code> objects can be changed even once they are used as endpoint
 * values in an <code>Interval</code>. Be careful when using a mutable type as
 * the basis for an <code>Interval</code> or <code>IntervalSet</code>.</p>
 *
 * @author Bobulous <http://www.bobulous.org.uk/>
 * @param <T> the basis type of <code>Interval</code> objects which will be held
 * by this <code>IntervalSet</code>.
 * @see Interval
 */
public final class IntervalSet<T extends Comparable<T>> implements
		Iterable<Interval<T>> {

	private final Set<Interval<T>> intervalSet;

	/*
	 * Private constructor required because IntervalSet is immutable, and it
	 * would be very ugly to use varargs to permit a variable number of Interval
	 * objects as parameters in the IntervalSet constructor.
	 * So the Builder pattern is used instead.
	 */
	private IntervalSet(IntervalSet.Builder<T> builder) {
		// A SortedSet implementation is necessary, otherwise the order in which
		// intervals are added causes the hashCode result to vary, even though
		// the set of intervals might be exactly the same. Using an
		// IntervalComparator and a TreeSet forces the hashCode method to find
		// the intervals in the same order regardless of the insertion sequence.
		this.intervalSet = new TreeSet<>(IntervalComparator.<T>getInstance());
		for (Interval<T> interval : builder.workingSet) {
			if (interval == null) {
				throw new IllegalStateException("IntervalSet.Builder object "
						+ "contained a null value where only Interval objects "
						+ "are permitted.");
			}
			this.intervalSet.add(interval);
		}
	}

	/**
	 * Reports on whether this <code>IntervalSet</code> permits the specified
	 * value. That is, returns <code>true</code> if the specified value is
	 * included by any of the intervals contained by this
	 * <code>IntervalSet</code>.
	 *
	 * @param value the value to check against the intervals found within this
	 * <code>IntervalSet</code>.
	 * @return <code>true</code> if the specified value is permitted by any of
	 * the intervals found within this <code>IntervalSet</code>.
	 */
	public boolean includes(T value) {
		for (Interval<T> interval : intervalSet) {
			if (interval.includes(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Reports on whether the specified <code>Interval</code> is contained by
	 * this <code>IntervalSet</code>.
	 *
	 * @param interval the search interval.
	 * @return true if the specified interval is found within this
	 * <code>IntervalSet</code>.
	 */
	public boolean contains(Interval<T> interval) {
		return intervalSet.contains(interval);
	}

	// TODO: Consider removing this.
	Set<Interval<T>> getIntervalSet() {
		return new HashSet<>(this.intervalSet);
	}

	/**
	 * Returns the number of intervals in this <code>IntervalSet</code>.
	 *
	 * @return the number of <code>Interval</code> objects contained by this
	 * <code>IntervalSet</code>.
	 */
	public int size() {
		return this.intervalSet.size();
	}

	/**
	 * Returns <code>true</code> if this <code>IntervalSet</code> is empty.
	 *
	 * @return <code>true</code> if this <code>IntervalSet</code> is empty;
	 * <code>false</code> otherwise..
	 */
	public boolean isEmpty() {
		return this.intervalSet.isEmpty();
	}

	/**
	 * Reports on whether the specified <code>Object</code> is an
	 * <code>IntervalSet</code> containing exactly the same intervals as this
	 * <code>IntervalSet</code>.
	 * <p>
	 * <strong>Warning:</strong> Two <code>IntervalSet</code> objects of
	 * different basis types will appear to be equal if they each contain a
	 * single, unbounded interval (both endpoints <code>null</code>). This is
	 * because it is not possible to determine the type of a <code>null</code>
	 * object. Be aware of this if your code might compare
	 * <code>IntervalSet</code> objects of different basis types.</p>
	 *
	 * @param obj the <code>Object</code> to check for equality with this
	 * <code>IntervalSet</code>.
	 * @return <code>true</code> if the specified <code>Object</code> is an
	 * <code>IntervalSet</code> containing the same intervals as this
	 * <code>IntervalSet</code>; <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof IntervalSet)) {
			return false;
		}

		IntervalSet<?> that = (IntervalSet<?>) obj;
		Set<Interval<?>> thatIntervalSet = new HashSet<Interval<?>>(
				that.intervalSet);
		Set<Interval<T>> thisIntervalSet = this.getIntervalSet();

		int thatSize = thatIntervalSet.size();
		int thisSize = thisIntervalSet.size();

		if (thisSize != thatSize) {
			return false;
		}
		if (thisSize == 0) {
			// Both interval sets have exactly zero members, so both represent
			// the empty set and are therefore equal.
			return true;
		}
		// Check that every Interval contained by this IntervalSet is found in
		// thatIntervalSet.
		for (Interval<T> thisInterval : thisIntervalSet) {
			if (thatIntervalSet.contains(thisInterval) == false) {
				return false;
			}
		}

		return true;
	}

	@Override
	public int hashCode() {
		int hash = 11;
		for (Interval<T> interval : this.intervalSet) {
			hash = 41 * hash + interval.hashCode();
		}
		return hash;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(
				"IntervalSet composed of intervals ");
		for (Interval<T> interval : this.intervalSet) {
			sb.append(interval.toString());
		}
		return sb.toString();
	}

	/**
	 * Returns a read-only iterator over the intervals contained by this
	 * <code>IntervalSet</code>.
	 *
	 * @return an <code>Iterator</code> which does not permit the
	 * <code>remove</code> method.
	 */
	@Override
	public Iterator<Interval<T>> iterator() {
		Iterator<Interval<T>> readOnlyIterator
				= new Iterator<Interval<T>>() {

					private final Iterator<Interval<T>> actual = intervalSet.
					iterator();

					@Override
					public boolean hasNext() {
						return actual.hasNext();
					}

					@Override
					public Interval<T> next() {
						return actual.next();
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException(
								"IntervalSet does not "
								+ "permit removal of its members.");
					}
				};
		return readOnlyIterator;
	}

	/**
	 * A mutable builder which allows construction of an IntervalSet.
	 *
	 * @param <K> the type which forms the basis of the Interval objects to be
	 * found in the IntervalSet which this Builder will produce.
	 */
	public static class Builder<K extends Comparable<K>> implements
			uk.org.bobulous.java.toolkit.Builder<IntervalSet<K>> {

		private Set<Interval<K>> workingSet = new HashSet<>();

		/**
		 * Includes the specified Interval in the set represented by this
		 * IntervalSet.Builder object. If this new interval intersects with an
		 * one or more existing intervals inside this IntervalSet, then all
		 * intersecting intervals will be replaced by the union of those
		 * intervals.
		 *
		 * @param interval the interval to be included in the IntervalSet being
		 * assembled by this Builder object.
		 * @return this Builder object, after inclusion of the specified
		 * Interval.
		 */
		public IntervalSet.Builder<K> include(Interval<K> interval) {
			if (interval == null) {
				throw new NullPointerException("Not permitted to insert a "
						+ "null value into an IntervalSet.Builder object.");
			}
			// Check each interval already in the working set, to see whether an
			// intersection occurs. If there is intersection, then remove the
			// existing member which intersects, and include a new interval
			// which contains all of the points contained by the union of that
			// interval and the new interval.
			Set<Interval<K>> unionSet = new HashSet<>();
			for (Interval<K> existingInterval : workingSet) {
				if (existingInterval.includes(interval)) {
					// The new interval is wholly contained by an existing
					// interval in this union, so there is no more work
					// to be done here.
					return this;
				}
				if (singleUnionExistsFor(existingInterval, interval)) {
					unionSet.add(existingInterval);
				}
			}
			if (unionSet.isEmpty()) {
				// The new interval does not intersect with any existing
				// intervals in this union, and no existing interval
				// wholly contains the new interval, so add the new interval
				// to the union and return.
				workingSet.add(interval);
				return this;
			}

			// The new interval intersects with one or more existing intervals
			// found in this union, so create a single interval which contains
			// all values permitted by their union, and remove those existing
			// intervals from the set.
			boolean leastLowerEndpointInIntersectionIsInfinite = false, greatestUpperEndpointInIntersectionIsInfinite
					= false;
			K leastLowerEndpointInIntersection = null, greatestUpperEndpointInIntersection
					= null;
			Interval.EndpointMode modeOfLeastLowerEndpointInIntersection = null, modeOfGreatestUpperEndpointInIntersection
					= null;
			unionSet.add(interval);
			for (Interval<K> intersectionInterval : unionSet) {
				// Remove the intersection interval from the working set,
				// because we want to replace all of the intersecting intervals
				// with a single interval which contains all values in the union.
				workingSet.remove(intersectionInterval);

				// Find the lowest lower endpoint value (and mode) of all the
				// intersecting intervals.
				if (!leastLowerEndpointInIntersectionIsInfinite) {
					// Lowest lower endpoint not yet known to be infinite
					if (intersectionInterval.getLowerEndpoint() == null) {
						// Current lower endpoint is infinite, so record this.
						leastLowerEndpointInIntersectionIsInfinite = true;
						modeOfLeastLowerEndpointInIntersection
								= Interval.EndpointMode.OPEN;
					} else {
						// Current lower endpoint is not infinite, so compare
						// its value to the current lowest value found.
						if (leastLowerEndpointInIntersection == null) {
							// No lowest value yet, so use the current value
							// and mode.
							leastLowerEndpointInIntersection
									= intersectionInterval.getLowerEndpoint();
							modeOfLeastLowerEndpointInIntersection
									= intersectionInterval.
									getLowerEndpointMode();
						} else {
							// A lowest-so-far value has already been set, so
							// compare the current value to see whether it
							// is lower.
							if (intersectionInterval.getLowerEndpoint().
									compareTo(leastLowerEndpointInIntersection)
									< 0) {
								// Current value is lower, so set the lowest-so-far
								// value (and mode) to match the current lower
								// endpoint.
								leastLowerEndpointInIntersection
										= intersectionInterval.
										getLowerEndpoint();
								modeOfLeastLowerEndpointInIntersection
										= intersectionInterval.
										getLowerEndpointMode();
							} else if (intersectionInterval.getLowerEndpoint().
									compareTo(leastLowerEndpointInIntersection)
									== 0) {
								// Current value is equal to the lowest-so-far
								// so check to see whether the current lower
								// endpoint has a CLOSED mode. Change the
								// lowest-so-far mode to CLOSED if so.
								if (intersectionInterval.getLowerEndpointMode().
										equals(Interval.EndpointMode.CLOSED)) {
									modeOfLeastLowerEndpointInIntersection
											= Interval.EndpointMode.CLOSED;
								}
							}
						}
					}
				}

				// Find the greatest upper endpoint value (and mode) of all the
				// intersecting intervals.
				// (Just mirror the process used for the lowest lower endpoint,
				// in the previous block of code just a few lines above here.)
				if (!greatestUpperEndpointInIntersectionIsInfinite) {
					if (intersectionInterval.getUpperEndpoint() == null) {
						greatestUpperEndpointInIntersectionIsInfinite = true;
						modeOfGreatestUpperEndpointInIntersection
								= Interval.EndpointMode.OPEN;
					} else {
						if (greatestUpperEndpointInIntersection == null) {
							greatestUpperEndpointInIntersection
									= intersectionInterval.getUpperEndpoint();
							modeOfGreatestUpperEndpointInIntersection
									= intersectionInterval.
									getUpperEndpointMode();
						} else {
							if (intersectionInterval.getUpperEndpoint().
									compareTo(
											greatestUpperEndpointInIntersection)
									> 0) {
								greatestUpperEndpointInIntersection
										= intersectionInterval.
										getUpperEndpoint();
								modeOfGreatestUpperEndpointInIntersection
										= intersectionInterval.
										getUpperEndpointMode();
							} else if (intersectionInterval.getUpperEndpoint().
									compareTo(
											greatestUpperEndpointInIntersection)
									== 0) {
								if (intersectionInterval.getUpperEndpointMode().
										equals(Interval.EndpointMode.CLOSED)) {
									modeOfGreatestUpperEndpointInIntersection
											= Interval.EndpointMode.CLOSED;
								}
							}
						}
					}
				}
			}

			// Using the minimum and maximum values discovered in the
			// intersection set, create one interval to rule them all. Well,
			// one interval which contains all values contained by the
			// union of the intersecting intervals.
			Interval<K> theOneInterval = new GenericInterval<>(
					modeOfLeastLowerEndpointInIntersection,
					(leastLowerEndpointInIntersectionIsInfinite ? null
					: leastLowerEndpointInIntersection),
					(greatestUpperEndpointInIntersectionIsInfinite ? null
					: greatestUpperEndpointInIntersection),
					modeOfGreatestUpperEndpointInIntersection);

			// Now add the one interval (representing the same values as the
			// intersection set) to the set of intervals represented by this
			// union, and then return.
			workingSet.add(theOneInterval);
			return this;
		}

		/**
		 * Includes the specified IntervalSet in the union represented by this
		 * IntervalSet.Builder object.
		 *
		 * @param union the IntervalSet which should be included in the union
		 * represented by this Builder object.
		 * @return this Builder object, once the specified IntervalSet has been
		 * included.
		 */
		public IntervalSet.Builder<K> include(IntervalSet<K> union) {
			if (union == null) {
				throw new NullPointerException("Not permitted to insert a "
						+ "null value into an IntervalSet.Builder object.");
			}
			for (Interval<K> interval : union.getIntervalSet()) {
				this.include(interval);
			}
			return this;
		}

		/**
		 * Includes every Interval from the supplied Collection in the union
		 * represented by this IntervalSet.Builder object.
		 *
		 * @param intervals a Collection of Interval objects having the same
		 * parameter type as this IntervalSet.Builder.
		 * @return this Builder object, after every Interval from the supplied
		 * Collection has been included.
		 */
		public IntervalSet.Builder<K> includeAll(
				Collection<Interval<K>> intervals) {
			if (intervals == null) {
				throw new NullPointerException("Not permitted to insert a "
						+ "null value into an IntervalSet.Builder object.");
			}
			for (Interval<K> interval : intervals) {
				this.include(interval);
			}
			return this;
		}

		/*
		 * Returns true if the two specified intervals can be exactly
		 * represented by a single interval.
		 * For example, [0, 1] and [1, 2] can be represented by a single
		 * interval of [0, 2] because every member permitted by the intervals
		 * [0, 1] and [1, 2] is permitted by the interval [0, 2], and every
		 * value excluded by [0, 1] and [1, 2] is excluded by [0, 2].
		 */
		private static <Q extends Comparable<Q>> boolean singleUnionExistsFor(
				Interval<Q> first, Interval<Q> second) {
			// If the two intervals intersect, then they can be replaced by a
			// single interval which contains all of the same values contained
			// by the pair, without containing any values not contained by one
			// of the pair.
			if (first.intersects(second)) {
				return true;
			}
			// Even if the two intervals do not intersect, it is possible that
			// they share an endpoint value such that one interval has a CLOSED
			// endpoint for the value while the other has an OPEN endpoint, and
			// this will also form a seamless union so that the two intervals
			// can be replaced by a single interval. For example,
			// (0, 1) forms a union with [1, 2] so both can be replaced with
			// the single interval (0, 2]. Similarly, a degenerate interval
			// [1, 1] forms a union with (1, 3) to produce the single interval
			// [1, 3).
			// This will occur if the upper endpoint of the first interval
			// has the same value as the lower endpoint of the second interval,
			// and one of them has mode CLOSED; or if the lower endpoint of the
			// first interval has the same value as the upper endpoint of the
			// second interval, and one of them has mode CLOSED.
			if (first.getLowerEndpoint().compareTo(second.getUpperEndpoint())
					== 0) {
				if (first.getLowerEndpointMode().equals(
						Interval.EndpointMode.CLOSED) || second.
						getUpperEndpointMode().equals(
								Interval.EndpointMode.CLOSED)) {
					return true;
				}
			}
			if (first.getUpperEndpoint().compareTo(second.getLowerEndpoint())
					== 0) {
				if (first.getUpperEndpointMode().equals(
						Interval.EndpointMode.CLOSED) || second.
						getLowerEndpointMode().equals(
								Interval.EndpointMode.CLOSED)) {
					return true;
				}
			}
			// There is no intersection between these intervals, and they do not
			// butt up against each other with a CLOSED + OPEN shared value, so
			// it is not possible to replace the pair of them with a single
			// interval which contains all the values they contain but none of
			// the values contained by neither of them.
			return false;
		}

		@Override
		public IntervalSet<K> build() {
			return new IntervalSet<>(this);
		}
	}
}
