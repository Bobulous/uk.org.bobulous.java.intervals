uk.org.bobulous.java.intervals
==============================

A package of classes which support mathematical intervals.

<p>See the page <a href="http://www.bobulous.org.uk/coding/intervals.html">intervals in Java</a> for an introduction to this package.</p>

<h2>Interval</h2>

<p>An interface which defines a type which represents an interval through a naturally ordered basis type. The basis type <code>T</code> must implement <code>Comparable&lt;T&gt;</code>. That is: the basis type must be a type whose instances can be compared with other instances of that type, thus giving the type a natural ordering. For example, an <code>Interval&lt;Double&gt;</code> is an interval through the basis type <code>Double</code>.</p>

<p>The interface defines an <code>Enum</code> called <code>EndpointMode</code> which holds the values <code>CLOSED</code> and <code>OPEN</code> for use in setting the mode of the lower and upper endpoints of an interval. The interface also declares methods which must return the mode and value of the lower and upper endpoints of an <code>Interval</code>, and methods which must report on whether this <code>Interval</code> includes a value  or <code>Interval</code> (of the same basis type as this <code>Interval</code>).</p>

<p>Fundamentally, an <code>Interval</code> is a type which says "include all values of the basis type which occur between the lower endpoint and upper endpoint, according to the natural ordering of the basis type".</p>

<h2>GenericInterval</h2>

<p>A concrete implementation of the <code>Interval</code> type, providing a constructor which allows an interval to be created in any basis type, with open and/or closed endpoints, and support for unbounded endpoints (having a <code>null</code> value which represents infinity).</p>

<p>See the JavaDoc within the source code for further details.</p>

<h2>NumericInterval</h2>

<p>An interface which defines an extension of Interval, adding methods which can operate on intervals through a numeric type. The basis type must implement both <code>Number</code> and <code>Comparable&lt;T&gt;</code>. This interface adds methods including <code>isEmpty()</code>, <code>intersectsWith(NumericInterval&lt;T&gt;)</code> and <code>unitesWith(NumericInterval&lt;T&gt;)</code> which can only be defined for intervals through numeric types.</p>

<h2>IntegerInterval</h2>

<p>A concrete implementation of <code>NumericInterval&lt;Integer&gt;</code>. Provides methods to check for and calculate intersections and unions between intervals of integers.</p>
