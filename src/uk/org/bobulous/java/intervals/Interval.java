/*
 * Copyright © 2014 Bobulous <http://www.bobulous.org.uk/>.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/.
 */
package uk.org.bobulous.java.intervals;

/**
 * An interval within a naturally ordered type.
 * <p>
 * Each <code>Interval</code> contains a lower endpoint value and an upper
 * endpoint value, such that the result of <code>lower.compareTo(upper)</code>
 * must be zero or greater. In other words, the upper endpoint must be equal to
 * or greater than the lower endpoint according to the natural ordering of the
 * interval basis type.</p>
 * <p>
 * For example, if an <code>Interval&lt;Integer&gt;</code> object (an interval
 * of integers) has a lower endpoint value of zero then its upper endpoint value
 * must be zero or greater. If an <code>Interval&lt;Character&gt;</code> (an
 * interval of character values) had a lower endpoint value of 'a' then its
 * upper endpoint value would have to be <code>'a'</code> or <code>'b'</code> or
 * any other character which causes <code>lower.compareTo(upper)</code> to
 * return zero or greater.</p>
 * <p>
 * If an endpoint is null then it means there is no limit to what is included in
 * that direction. So a <code>null</code> lower endpoint means that all values
 * lesser than the upper endpoint are contained by the interval; a
 * <code>null</code> upper endpoint means that all values greater than the lower
 * endpoint are included in the interval; and if both endpoints are
 * <code>null</code> then all values are included in this interval. Note that
 * <code>null</code> itself is never included in an interval, as a null object
 * represents the lack of a value. Be aware that null endpoints will allow any
 * value of the permitted type, including values such as <code>Double.NaN</code>
 * (which is supposed to represent a non-value value).</p>
 * <p>
 * Also note that a type constant which is intended to represent infinity, such
 * as <code>Double.POSITIVE_INFINITY</code>, is just another numeric value so
 * far as the <code>Comparable</code> interface is concerned, and is therefore
 * just another member of the naturally ordered set Double so far as
 * <code>Interval&lt;Double&gt;</code> is concerned. Specifying such a value for
 * an endpoint is not the same as specifying <code>null</code> because
 * <code>null</code> will admit any value belonging to the interval basis type,
 * whereas a pseudo-infinite value such as <code>Double.POSITIVE_INFINITY</code>
 * will exclude any values which are beyond it in the natural order of the basis
 * type. For example, <code>Double.NaN</code> is considered greater than
 * <code>Double.NEGATIVE_INFINITY</code> and also greater than
 * <code>Double.POSITIVE_INFINITY</code> according to the <code>compareTo</code>
 * method of <code>Double</code>.</p>
 * <p>
 * An implementation of <code>Interval</code> may or may not permit
 * <code>null</code> endpoint values, and it must make clear in its
 * documentation what is permitted.</p>
 * <p>
 * An interval can include zero, one or both of its endpoint values. This is
 * specified by the mode of each endpoint. {@link EndpointMode#CLOSED} means
 * that the endpoint value itself is included in the interval, while
 * {@link EndpointMode#OPEN} means that the endpoint value itself is not
 * included in the interval. For instance, an
 * <code>Interval&lt;Double&gt;</code> might have a lower endpoint value of zero
 * and a lower endpoint mode of <code>EndpointMode.OPEN</code> which means that
 * zero is the lower limit of the interval but is not included in the interval.
 * The endpoint mode is irrelevant for a null endpoint.</p>
 * <p>
 * An implementation of <code>Interval</code> may permit both open and closed
 * endpoints, or may permit only one mode, and it must make the options clear in
 * its documentation.</p>
 *
 *
 * @author Bobulous <http://www.bobulous.org.uk/>
 * @param <T> the basis type of this <code>Interval</code>. The basis type must
 * implement <code>Comparable&lt;T&gt;</code> so that each instance of the type
 * can be compared with other instances of the same type, thus being a type
 * which has a natural order.
 * @see GenericInterval
 * @see IntervalComparator
 * @see IntervalSet
 */
public interface Interval<T extends Comparable<T>> {

	/**
	 * An enumerated type which contains values to specify endpoint mode.
	 * <p>
	 * An endpoint can be either open (endpoint value is excluded from interval)
	 * or closed (endpoint value is included in interval).</p>
	 */
	public static enum EndpointMode {

		/**
		 * Indicates that the corresponding endpoint value is
		 * <strong>excluded from</strong> the interval.
		 */
		OPEN,
		/**
		 * Indicates that the corresponding endpoint value is
		 * <strong>included in</strong> the interval.
		 */
		CLOSED
	}

	/**
	 * Returns the lower endpoint value of this interval.
	 *
	 * @return the value of the lower endpoint, or <code>null</code> if there is
	 * no lower bound for this interval.
	 */
	public T getLowerEndpoint();

	/**
	 * Returns the upper endpoint value of this interval.
	 *
	 * @return the value of the upper endpoint, or <code>null</code> if there is
	 * no upper bound for this interval.
	 */
	public T getUpperEndpoint();

	/**
	 * Returns the endpoint mode of the lower endpoint of this interval.
	 *
	 * @return the mode of the lower endpoint.
	 */
	public EndpointMode getLowerEndpointMode();

	/**
	 * Returns the endpoint mode of the upper endpoint of this interval.
	 *
	 * @return the mode of the upper endpoint.
	 */
	public EndpointMode getUpperEndpointMode();

	/**
	 * Reports on whether this interval represents the empty set. If this
	 * interval does represent the empty set then there is no value which is
	 * considered to be contained by this interval.
	 * <p>
	 * Note that for any value <var>a</var> all of the following intervals
	 * represent the empty set: (<var>a</var>,<var>a</var>] and
	 * [<var>a</var>,<var>a</var>) and (<var>a</var>,<var>a</var>). In other
	 * words, if the lower and upper endpoints are the same value then the
	 * interval represents the empty set if one or both endpoints is
	 * <code>OPEN</code>.</p>
	 *
	 * @return <code>true</code> if this interval has no members;
	 * <code>false</code> if this interval has any members.
	 */
	public boolean isEmpty();

	/**
	 * Reports on whether this interval includes the specified value.
	 *
	 * @param value the value to test.
	 * @return <code>true</code> if the specified value is contained by this
	 * <code>Interval</code>; <code>false</code> otherwise.
	 * @throws NullPointerException if <code>null</code> is provided to this
	 * method.
	 */
	public boolean includes(T value);

	/**
	 * Reports on whether this interval wholly contains the specified interval.
	 * <p>
	 * This method will only return <code>true</code> if this interval includes
	 * every possible value included by the specified interval. Otherwise
	 * <code>false</code> is returned.</p>
	 *
	 * @param interval
	 * @return <code>true</code> if the specified interval is wholly contained
	 * by this interval.
	 * @throws NullPointerException if <code>null</code> is provided to this
	 * method.
	 */
	public boolean includes(Interval<T> interval);

	/**
	 * Reports on whether this interval intersects with the specified interval.
	 * <p>
	 * That is, returns <code>true</code> if there exists a value <var>v</var>
	 * such that <code>this.includes(v)</code> and
	 * <code>interval.includes(v)</code> both return <code>true</code>; returns
	 * <code>false</code> otherwise.</p>
	 *
	 * @param interval the other interval.
	 * @return <code>true</code> if this interval shares any members with the
	 * other interval.
	 * @throws NullPointerException if <code>null</code> is provided to this
	 * method.
	 */
	public boolean intersects(Interval<T> interval);

	/**
	 * Returns the intersection of this interval with the specified interval. In
	 * other words, returns an interval which contains all of the values
	 * contained by <strong>both</strong> this interval and the specified
	 * interval, or returns an interval (of arbitrary endpoint values) which
	 * represents the empty set (includes no values) if the two do not share any
	 * values.
	 *
	 * @param interval the other interval.
	 * @return an <code>Interval</code> which represents the intersection of
	 * this interval with the specified interval, or an empty interval if this
	 * interval does not intersect with the specified interval.
	 */
	public Interval<T> intersection(Interval<T> interval);

	/**
	 * Returns the union of this interval with the specified interval. In other
	 * words, returns a single interval (if one exists) which only includes all
	 * values included by this interval <strong>and</strong> all of the values
	 * included by the specified interval. Returns <code>null</code> if no such
	 * union exists.
	 * <p>
	 * Note that two intervals can produce a union if and only if they either
	 * intersect, or they adjoin at a shared endpoint and one of the intervals
	 * includes that endpoint value. For example, the intervals [<var>a</var>,
	 * <var>b</var>) and [<var>b</var>, <var>c</var>] form the union
	 * [<var>a</var>, <var>c</var>] because they adjoin at the value
	 * <var>b</var>, and one of the intervals does include the value
	 * <var>b</var>. But the two intervals [<var>a</var>,
	 * <var>b</var>) and (<var>b</var>, <var>c</var>] do not form a union
	 * because even though they adjoin at value <var>b</var>, neither interval
	 * actually includes the value <var>b</var>.</p>
	 *
	 * @param interval the interval with which this interval should form a
	 * union, if possible.
	 * @return the <code>Interval</code> which represents the union of this
	 * interval with the specified interval, or <code>null</code> if no union
	 * exists.
	 */
	public Interval<T> union(Interval<T> interval);
}
