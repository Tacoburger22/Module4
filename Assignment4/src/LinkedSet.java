import java.util.Iterator;

/**
 * Provides an implementation of the Set interface.
 * A doubly-linked list is used as the underlying data structure.
 * Although not required by the interface, this linked list is
 * maintained in ascending natural order. In those methods that
 * take a LinkedSet as a parameter, this order is used to increase
 * efficiency.
 *
 * @author Isaac Weiss (icw0001@auburn.edu)
 * @version 2020-10-10
 *
 */
public class LinkedSet<T extends Comparable<T>> implements Set<T> {
    //////////////////////////////////////////////////////////
    // Do not change the following three fields in any way. //
    //////////////////////////////////////////////////////////

    /** References to the first and last node of the list. */
    Node front;
    Node rear;

    /** The number of nodes in the list. */
    int size;

    /////////////////////////////////////////////////////////
    // Do not change the following constructor in any way. //
    /////////////////////////////////////////////////////////

    /**
     * Instantiates an empty LinkedSet.
     */
    public LinkedSet() {
        front = null;
        rear = null;
        size = 0;
    }

    /**public static void main(String[] args) {
        LinkedSet mySet = new LinkedSet();
        mySet.add(1);
        mySet.add(2);
        mySet.add(3);
        //mySet.add(7);
        //mySet.add(-1);
        if (mySet.contains(11)) {
            System.out.println("Found it!");
            System.out.println(mySet.toString());
        } else {
            System.out.println("Didn't find it!");
        }
        LinkedSet mySecondSet = new LinkedSet();
        //mySecondSet.add(2);
        //mySecondSet.add(6);
        mySecondSet.add(3);
        mySecondSet.add(4);
        mySecondSet.add(2);
        //mySecondSet.add(-2);
        if (mySet.equals(mySecondSet)) {
            System.out.println("Hooray!");
        }
        System.out.println("My set: " + mySet);
        System.out.println("My second set: " + mySecondSet);
        LinkedSet unionSet = (LinkedSet) mySet.union(mySecondSet);
        System.out.println("My union set: " + unionSet);
        LinkedSet intersectionSet = (LinkedSet) mySet.intersection(mySecondSet);
        System.out.println("My intersection set: " + intersectionSet);
        LinkedSet complementSet = (LinkedSet) mySet.complement(mySecondSet);
        System.out.println("My complement set: " + complementSet);
    }*/

    //////////////////////////////////////////////////
    // Public interface and class-specific methods. //
    //////////////////////////////////////////////////

    ///////////////////////////////////////
    // DO NOT CHANGE THE TOSTRING METHOD //
    ///////////////////////////////////////
    /**
     * Return a string representation of this LinkedSet.
     *
     * @return a string representation of this LinkedSet
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (T element : this) {
            result.append(element + ", ");
        }
        result.delete(result.length() - 2, result.length());
        result.append("]");
        return result.toString();
    }

    ///////////////////////////////////
    // DO NOT CHANGE THE SIZE METHOD //
    ///////////////////////////////////
    /**
     * Returns the current size of this collection.
     *
     * @return  the number of elements in this collection.
     */
    public int size() {
        return size;
    }

    //////////////////////////////////////
    // DO NOT CHANGE THE ISEMPTY METHOD //
    //////////////////////////////////////
    /**
     * Tests to see if this collection is empty.
     *
     * @return  true if this collection contains no elements, false otherwise.
     */
    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * Ensures the collection contains the specified element. Neither duplicate
     * nor null values are allowed. This method ensures that the elements in the
     * linked list are maintained in ascending natural order.
     *
     * @param  element  The element whose presence is to be ensured.
     * @return true if collection is changed, false otherwise.
     */
    public boolean add(T element) {
        if (element == null) { //return null if element is null
            return false;
        }
        Node n = new Node(element);
        if (isEmpty()) { //add first node to empty set
            front = n;
            rear = n;
        }
        if (!isEmpty()) {
            if (contains(element)) { //return false if element is detected (no duplicates)
                return false;
            }
            Node addWalker = front;
            while (addWalker.next != null && element.compareTo(addWalker.element) > 0) {
                addWalker = addWalker.next;
            }
            if (addWalker == front && element.compareTo(addWalker.element) < 0) { //special case, insert at front
                n.next = front;
                addWalker.prev = n;
                front = n;
            } else if (addWalker == rear && element.compareTo(addWalker.element) > 0) { //special case, at rear
                n.prev = rear;
                rear.next = n;
                rear = n;
            } else { //in middle
                n.prev = addWalker.prev; //element isn't null, non-empty set, not a duplicate... add!
                n.next = addWalker;
                addWalker.prev.next = n;
                addWalker.prev = n;
            }

        }
        size++;
        return true;
    }

    /**
     * Ensures the collection does not contain the specified element.
     * If the specified element is present, this method removes it
     * from the collection. This method, consistent with add, ensures
     * that the elements in the linked lists are maintained in ascending
     * natural order.
     *
     * @param   element  The element to be removed.
     * @return  true if collection is changed, false otherwise.
     */
    public boolean remove(T element) {
        if (element == null || isEmpty() || !contains(element)) {
            return false;
        }
        Node nodeWalker = front;
        while (nodeWalker != null) {
            if (nodeWalker.element == element) {
                if (size == 1) {
                    front = null;
                    rear = null;
                    break;
                }
                if (nodeWalker == rear) { //rear case
                    nodeWalker.prev.next = null;
                    rear = nodeWalker.prev;
                } else if (nodeWalker == front) { //front case
                    front = nodeWalker.next;
                    nodeWalker.next.prev = null;
                } else { //middle case
                    nodeWalker.prev.next = nodeWalker.next;
                    nodeWalker.next.prev = nodeWalker.prev;
                }
                break;
            }
            nodeWalker = nodeWalker.next;
        }
        size--;
        return true;
    }

    /**
     * Searches for specified element in this collection.
     *
     * @param   element  The element whose presence in this collection is to be tested.
     * @return  true if this collection contains the specified element, false otherwise.
     */
    public boolean contains(T element) {
        Node nodeWalker = front;
        while (nodeWalker != null) { //walk through set until end
            if (nodeWalker.element == element) { //if duplicate, return true for contains method
                return true;
            }
            nodeWalker = nodeWalker.next;
        }
        return false;
    }

    /**
     * Tests for equality between this set and the parameter set.
     * Returns true if this set contains exactly the same elements
     * as the parameter set, regardless of order.
     *
     * @return  true if this set contains exactly the same elements as
     *               the parameter set, false otherwise
     */
    public boolean equals(Set<T> s) {
        if (s == null || s.size() != this.size()) {
            return false;
        }
        if (s.isEmpty() && this.isEmpty()) {
            return true;
        }
        int numEquals = 0;
        int setSize = s.size();
        if (setSize != this.size()) {
            return false;
        }
        for (T setItem : s) {
            for (T thisItem : this) {
                if (thisItem.equals(setItem)) {
                    numEquals++;
                    break;
                }
            }
        }
        if (numEquals == setSize) {
            return true;
        }
        return false;
    }

    /**
     * Tests for equality between this set and the parameter set.
     * Returns true if this set contains exactly the same elements
     * as the parameter set, regardless of order.
     *
     * @return  true if this set contains exactly the same elements as
     *               the parameter set, false otherwise
     */
    public boolean equals(LinkedSet<T> s) {
        String toString = this.toString();
        return toString.equals(s.toString());
    }

    /**
     * Returns a set that is the union of this set and the parameter set.
     *
     * @return  a set that contains all the elements of this set and the parameter set
     */
    public Set<T> union(Set<T> s){
        LinkedSet<T> newSet = new LinkedSet<>();
        if (s == null) {
            return null;
        }
        if (s.isEmpty()) {
            return this;
        }
        if (this.isEmpty()) {
            return s;
        }
        for (T setItem : s) {
            newSet.add(setItem);
        }
        for (T thisItem : this) {
            newSet.add(thisItem);
        }
        return newSet;
    }

    /**
     * Returns a set that is the union of this set and the parameter set.
     *
     * @return  a set that contains all the elements of this set and the parameter set
     */
    public Set<T> union(LinkedSet<T> s){
        LinkedSet<T> newSet = new LinkedSet<>();
        if (s == null) {
            return null;
        }
        if (s.isEmpty()) {
            return this;
        }
        if (this.isEmpty()) {
            return s;
        }
        Node n = this.front;
        Node p = s.front;
        boolean done = false;
        if (n.element.compareTo(p.element) < 0) {
            Node q = new Node();
            q.element = n.element;
            newSet.front = q;
            newSet.rear = q;
            newSet.size++;
            n = n.next;
        } else if (n.element.equals(p.element)) {
            Node q = new Node();
            q.element = n.element;
            newSet.front = q;
            newSet.rear = q;
            newSet.size++;
            n = n.next;
            p = p.next;
        } else {
            Node r = new Node(p.element);
            r.element = p.element;
            newSet.front = r;
            newSet.rear = r;
            newSet.size++;
            p = p.next;
        }
        while (!done) {
            if (n != null) {
                if (p == null) {
                    do {
                        Node o = new Node();
                        o.element = n.element;
                        o.prev = newSet.rear;
                        newSet.rear.next = o;
                        newSet.rear = o;
                        newSet.size++;
                        n = n.next;
                        o = null;
                    } while (n != null && this.iterator().hasNext());
                    break;
                }
                if (n.element.compareTo(p.element) < 0) {
                    Node t = new Node();
                    t.element = n.element;
                    t.prev = newSet.rear;
                    newSet.rear.next = t;
                    newSet.rear = t;
                    newSet.size++;
                    n = n.next;
                    t = null;
                }
                if (p != null && n!= null && p.element.equals(n.element)) {
                    Node t = new Node();
                    t.element = n.element;
                    t.prev = newSet.rear;
                    newSet.rear.next = t;
                    newSet.rear = t;
                    newSet.size++;
                    n = n.next;
                    p = p.next;
                    t = null;
                }
            }
            if (p != null) {
                if (n == null) {
                    do {
                        Node o = new Node();
                        o.element = p.element;
                        o.prev = newSet.rear;
                        newSet.rear.next = o;
                        newSet.rear = o;
                        newSet.size++;
                        p = p.next;
                        o = null;
                    } while (p != null && s.iterator().hasNext());
                    break;
                }
                if (p.element.compareTo(n.element) < 0) {
                    Node t = new Node();
                    t.element = p.element;
                    t.prev = newSet.rear;
                    newSet.rear.next = t;
                    newSet.rear = t;
                    newSet.size++;
                    p = p.next;
                    t = null;
                }
                if (n != null && p != null && n.element.equals(p.element)) {
                    Node t = new Node();
                    t.element = n.element;
                    t.prev = newSet.rear;
                    newSet.rear.next = t;
                    newSet.rear = t;
                    newSet.size++;
                    n = n.next;
                    p = p.next;
                    t = null;
                }
            }
            if (p == null && n == null) {
                done = true;
            }
        }
        //for (T setItem : s) {
        //    this.add(setItem);
        //}
        //for (T thisItem : this) {
        //   newSet.add(thisItem);
        //}
        return newSet;
    }

    /**
     * Returns a set that is the intersection of this set and the parameter set.
     *
     * @return  a set that contains elements that are in both this set and the parameter set
     */
    public Set<T> intersection(Set<T> s) {
        LinkedSet<T> newSet = new LinkedSet<>();
        if (s == null) {
            return null;
        }
        if (s.isEmpty() || this.isEmpty()) {
            return newSet;
        }
        for (T setItem : s) {
            if (this.contains(setItem)) {
                newSet.add(setItem);
            }
        }
        return newSet;
    }

    /**
     * Returns a set that is the intersection of this set and
     * the parameter set.
     *
     * @return  a set that contains elements that are in both
     *            this set and the parameter set
     */
    public Set<T> intersection(LinkedSet<T> s) {
        LinkedSet<T> newSet = new LinkedSet<>();
        if (s == null) {
            return null;
        }
        if (s.isEmpty() || this.isEmpty()) {
            return newSet;
        }
        Node n = this.front;
        Node p = s.front;
        boolean done = false;
        boolean foundFirst = false;
        boolean validFirst = false;
        do {
            while (n != null) {
                if (n.element.equals(p.element)) {
                    Node t = new Node();
                    t.element = n.element;
                    newSet.front = t;
                    newSet.rear = t;
                    newSet.size++;
                    validFirst = true;
                    foundFirst = true;
                    n = n.next;
                    break;
                }
                if (!validFirst) {
                    n = n.next;
                }
            }
            if (n == null) {
                return newSet;
            }
        } while (!foundFirst);
        while (!done) {
            if (n != null) {
                if (p == null) {
                    break;
                }
                if (n.element.compareTo(p.element) > 0) {
                    p = p.next;
                    continue;
                }
                if (n.element.compareTo(p.element) < 0) {
                    n = n.next;
                    continue;
                }
                if (n.element.equals(p.element)) {
                    Node t = new Node();
                    t.element = n.element;
                    t.prev = newSet.rear;
                    newSet.rear.next = t;
                    newSet.rear = t;
                    newSet.size++;
                    n = n.next;
                    p = p.next;
                    t = null;
                }
            }
            if (p != null) {
                if (n == null) {
                    break;
                }
            }
            if (p == null && n == null) {
                done = true;
            }
        }
        return newSet;
    }

    /**
     * Returns a set that is the complement of this set and the parameter set.
     *
     * @return  a set that contains elements that are in this set but not the parameter set
     */
    public Set<T> complement(Set<T> s) {
        LinkedSet<T> newSet = new LinkedSet<>();
        if (s == null) {
            return null;
        }
        if (s.isEmpty()) {
            return this;
        }
        if (this.isEmpty()) {
            return newSet;
        }
        for (T thisItem : this) {
            if (!s.contains(thisItem)) {
                newSet.add(thisItem);
            }
        }
        return newSet;
    }

    /**
     * Returns a set that is the complement of this set and
     * the parameter set.
     *
     * @return  a set that contains elements that are in this
     *            set but not the parameter set
     */
    public Set<T> complement(LinkedSet<T> s) {
        LinkedSet<T> newSet = new LinkedSet<>();
        if (s == null) {
            return null;
        }
        if (s.isEmpty()) {
            return this;
        }
        if (this.isEmpty()) {
            return newSet;
        }
        Node n = this.front;
        while (n != null) {
            if (!s.contains(n.element)) {
                if (newSet.isEmpty()) {
                    Node t = new Node();
                    t.element = n.element;
                    newSet.front = t;
                    newSet.rear = t;
                    t = null;
                } else {
                    Node t = new Node();
                    t.element = n.element;
                    t.prev = newSet.rear;
                    newSet.rear.next = t;
                    newSet.rear = t;
                    t = null;
                }
                newSet.size++;
            }
            n = n.next;
        }
        return newSet;
    }

    /**
     * Returns an iterator over the elements in this LinkedSet.
     * Elements are returned in ascending natural order.
     *
     * @return  an iterator over the elements in this LinkedSet
     */
    public Iterator<T> iterator() {
        return new ascendingIterator();
    }

    private class ascendingIterator implements Iterator<T> {
        private Node n = front;
        private int current = 0;

        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public T next() {
            if (hasNext() && n != null) {
                current++;
                Node memory = new Node(n.element);
                n = n.next;
                return memory.element;
            }
            return null;
        }
    }

    /**
     * Returns an iterator over the elements in this LinkedSet.
     * Elements are returned in descending natural order.
     *
     * @return  an iterator over the elements in this LinkedSet
     */
    public Iterator<T> descendingIterator() {
        return new descendingIterator();
    }

    private class descendingIterator implements Iterator<T> {
        private Node n = rear;
        private int current = 0;

        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public T next() {
            if (hasNext() && n != null) {
                current++;
                Node memory = new Node(n.element);
                n = n.prev;
                return memory.element;
            }
            return null;
        }
    }

    /**
     * Returns an iterator over the members of the power set
     * of this LinkedSet. No specific order can be assumed.
     *
     * @return  an iterator over members of the power set
     */
    public Iterator<Set<T>> powerSetIterator() {
        return new powerSetIterator();
    }

    private class powerSetIterator implements Iterator<Set<T>> {
        private final double MAX_SETS = Math.pow(2, size);
        private int current = 0;
        private final LinkedSet<T> linkedSet = new LinkedSet<>();

        @Override
        public boolean hasNext() {
            return current < MAX_SETS;
        }

        @Override
        public Set<T> next() {
            if (isEmpty()) {
                current++;
                return linkedSet;
            }
            return null;
        }
    }

    //////////////////////////////
    // Private utility methods. //
    //////////////////////////////

    // Feel free to add as many private methods as you need.

    ////////////////////
    // Nested classes //
    ////////////////////

    //////////////////////////////////////////////
    // DO NOT CHANGE THE NODE CLASS IN ANY WAY. //
    //////////////////////////////////////////////

    /**
     * Defines a node class for a doubly-linked list.
     */
    class Node {
        /** the value stored in this node. */
        T element;
        /** a reference to the node after this node. */
        Node next;
        /** a reference to the node before this node. */
        Node prev;

        /**
         * Instantiate an empty node.
         */
        public Node() {
            element = null;
            next = null;
            prev = null;
        }

        /**
         * Instantiate a node that contains element
         * and with no node before or after it.
         */
        public Node(T e) {
            element = e;
            next = null;
            prev = null;
        }
    }
}
