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
}
