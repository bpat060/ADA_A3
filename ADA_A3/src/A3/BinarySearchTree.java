/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package A3;

/**
 *
 * @author Owner
 */
//reference:
//used the template given. added node class and variables and methods needed, changed the main class
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.SortedSet;

public class BinarySearchTree<E> extends AbstractSet<E>
        implements SortedSet<E> {

    // color constants for printing
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_BLACK = "\u001B[30m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_RESET = "\u001B[0m";

    private int numElements;
    protected RedBlackTree rootNode;
    private Comparator<? super E> comparator;//null for natural ordering
    private E fromElement, toElement; // bounds for visible view of tree
    RedBlackTree root;

    public BinarySearchTree() {
        super();
        numElements = 0;
        rootNode = null;
        comparator = null;
        fromElement = null;
        toElement = null;
    }

    public BinarySearchTree(Collection<? extends E> c) {
        this();
        for (E element : c) {
            add(element);
        }
    }

    public BinarySearchTree(Comparator<? super E> comparator) {
        this();
        this.comparator = comparator;
    }

    public BinarySearchTree(SortedSet<E> s) {
        this();
        this.comparator = s.comparator();
        for (E element : s) {
            add(element);
        }
    }

    // private constructor used to create a view of a portion of tree
    private BinarySearchTree(RedBlackTree rootNode,
            Comparator<? super E> comparator, E fromElement, E toElement) {
        this(comparator);
        this.rootNode = rootNode;
        this.fromElement = fromElement;
        this.toElement = toElement;
        // calculate the number of elements
        this.numElements = countNodes(rootNode);
    }

    // recursive helper method that counts number of descendants of node
    private int countNodes(RedBlackTree node) {
        if (node == null) {
            return 0;
        } else {
            return countNodes(node.leftChild) + 1
                    + countNodes(node.rightChild);
        }
    }

    // helper method that determines whether an element is within the
    // specified view
    private boolean withinView(E element) {
        boolean inside = true;
        if (fromElement != null && compare(element, fromElement) < 0) {
            inside = false;
        }
        if (toElement != null && compare(element, toElement) >= 0) {
            inside = false;
        }
        return inside;
    }

    // adds the element to the sorted pathSet provided it is not already in
    // the pathSet, and returns true if the sorted pathSet did not already
    // contain the element
    public boolean add(E o) {
        if (!withinView(o)) {
            throw new IllegalArgumentException("Outside view");
        }
        RedBlackTree newNode = new RedBlackTree(o);
        boolean added = false;
        if (rootNode == null) {
            rootNode = newNode;
            added = true;
        } else {  // find where to add newNode
            RedBlackTree currentNode = rootNode;
            boolean done = false;
            while (!done) {
                int comparison = compare(o, currentNode.element);
                if (comparison < 0) // newNode is less than currentNode
                {
                    if (currentNode.leftChild == null) {  // add newNode as leftChild
                        currentNode.leftChild = newNode;
                        done = true;
                        added = true;
                    } else {
                        currentNode = currentNode.leftChild;
                    }
                } else if (comparison > 0)//newNode is greater than currentNode
                {
                    if (currentNode.rightChild == null) {  // add newNode as rightChild
                        currentNode.rightChild = newNode;
                        done = true;
                        added = true;
                    } else {
                        currentNode = currentNode.rightChild;
                    }
                } else if (comparison == 0) // newNode equal to currentNode
                {
                    done = true; // no duplicates in this binary tree impl.
                }
            }
        }
        if (added) {
            numElements++;
        }
        return added;
    }

    // performs a comparison of the two elements, using the comparator
    // if not null, otherwise using the compareTo method
    private int compare(E element1, E element2) {
        if (comparator != null) {
            return comparator.compare(element1, element2);
        } else if (element1 != null && element1 instanceof Comparable) {
            return ((Comparable) element1).compareTo(element2); //unchecked
        } else if (element2 != null && element2 instanceof Comparable) {
            return -((Comparable) element2).compareTo(element1);//unchecked
        } else {
            return 0;
        }
    }

    // remove the element from the sorted pathSet and returns true if the
    // element was in the sorted pathSet
    public boolean remove(Object o) {
        boolean removed = false;
        E element = (E) o; // unchecked, could throw exception
        if (!withinView(element)) {
            throw new IllegalArgumentException("Outside view");
        }
        if (rootNode != null) {  // check if root to be removed
            if (compare(element, rootNode.element) == 0) {
                rootNode = makeReplacement(rootNode);
                removed = true;
            } else {  // search for the element o
                RedBlackTree parentNode = rootNode;
                RedBlackTree removalNode;
                // determine whether to traverse to left or right of root
                if (compare(element, rootNode.element) < 0) {
                    removalNode = rootNode.leftChild;
                } else // compare(element, rootNode.element)>0
                {
                    removalNode = rootNode.rightChild;
                }
                while (removalNode != null && !removed) {  // determine whether the removalNode has been found
                    int comparison = compare(element, removalNode.element);
                    if (comparison == 0) {
                        if (removalNode == parentNode.leftChild) {
                            parentNode.leftChild
                                    = makeReplacement(removalNode);
                        } else // removalNode==parentNode.rightChild
                        {
                            parentNode.rightChild
                                    = makeReplacement(removalNode);
                        }
                        removed = true;
                    } else // determine whether to traverse to left or right
                    {
                        parentNode = removalNode;
                        if (comparison < 0) {
                            removalNode = removalNode.leftChild;
                        } else // comparison>0
                        {
                            removalNode = removalNode.rightChild;
                        }
                    }
                }
            }
        }
        if (removed) {
            numElements--;
        }
        return removed;
    }

    // helper method which removes removalNode (presumed not null) and
    // returns a reference to node that should take place of removalNode
    private RedBlackTree makeReplacement(RedBlackTree removalNode) {
        RedBlackTree replacementNode = null;
        // check cases when removalNode has only one child
        if (removalNode.leftChild != null && removalNode.rightChild == null) {
            replacementNode = removalNode.leftChild;
        } else if (removalNode.leftChild == null
                && removalNode.rightChild != null) {
            replacementNode = removalNode.rightChild;
        } // check case when removalNode has two children
        else if (removalNode.leftChild != null
                && removalNode.rightChild != null) {  // find the inorder successor and use it as replacementNode
            RedBlackTree parentNode = removalNode;
            replacementNode = removalNode.rightChild;
            if (replacementNode.leftChild == null) // replacementNode can be pushed up one level to replace
            // removalNode, move the left child of removalNode to be
            // the left child of replacementNode
            {
                replacementNode.leftChild = removalNode.leftChild;
            } else {  //find left-most descendant of right subtree of removalNode
                do {
                    parentNode = replacementNode;
                    replacementNode = replacementNode.leftChild;
                } while (replacementNode.leftChild != null);
                // move the right child of replacementNode to be the left
                // child of the parent of replacementNode
                parentNode.leftChild = replacementNode.rightChild;
                // move the children of removalNode to be children of
                // replacementNode
                replacementNode.leftChild = removalNode.leftChild;
                replacementNode.rightChild = removalNode.rightChild;
            }
        }
        // else both leftChild and rightChild null so no replacementNode
        return replacementNode;
    }

    public Iterator<E> iterator() {
        return new BinaryTreeIterator(rootNode);
    }

    // returns the number of elements in the tree
    public int size() {
        return numElements;
    }

    // removes all elements from the collection
    public void clear() {
        rootNode = null; // all nodes will be garbage collected as well
    }

    // overridden method with an efficient O(log n) search algorithm
    // rather than the superclasses O(n) linear search using iterator
    public boolean contains(Object o) {
        boolean found = false;
        E element = (E) o; // unchecked, could throw exception
        if (!withinView(element)) {
            return false;
        }
        RedBlackTree currentNode = rootNode;
        while (!found && currentNode != null) {
            int comparison = compare(currentNode.element, element);
            if (comparison == 0) {
                found = true;
            } else if (comparison < 0) {
                currentNode = currentNode.rightChild;
            } else // comparison>0
            {
                currentNode = currentNode.leftChild;
            }
        }
        return found;
    }

    // returns the Comparator used to compare elements or null if
    // the element natural ordering is used
    public Comparator<? super E> comparator() {
        return comparator;
    }

    // returns the first (lowest) element currently in sorted pathSet that
    // is at least as big as fromElement, returns null if none found
    public E first() {
        if (rootNode == null) {
            throw new NoSuchElementException("empty tree");
        }
        // find the least descendant of rootNode that is at least
        // as big as fromElement by traversing down tree from root
        RedBlackTree currentNode = rootNode;
        RedBlackTree leastYetNode = null; // smallest found so far
        while (currentNode != null) {
            if (compare(currentNode.element, fromElement) >= 0) {
                if (compare(currentNode.element, toElement) < 0) {
                    leastYetNode = currentNode;
                }
                // move to the left child to see if a smaller element okay
                // since all in right subtree will be larger
                currentNode = currentNode.leftChild;
            } else // compare(currentNode.element, fromElement)<0
            {  // move to the right child since this element too small
                // so all in left subtree will also be too small
                currentNode = currentNode.rightChild;
            }
        }
        if (leastYetNode == null) // no satisfactory node found
        {
            return null;
        } else {
            return leastYetNode.element;
        }
    }

    public SortedSet<E> headSet(E toElement) {
        return subSet(null, toElement);
    }

    // returns the last (highest) element currently in sorted pathSet that
    // is less than toElement, return null if none found
    public E last() {
        if (rootNode == null) {
            throw new NoSuchElementException("empty tree");
        }
        // find the greatest descendant of rootNode that is less than
        // toElement by traversing down tree from root
        RedBlackTree currentNode = rootNode;
        RedBlackTree greatestYetNode = null; // greatest found so far
        while (currentNode != null) {
            if (compare(currentNode.element, toElement) < 0) {
                if (compare(currentNode.element, fromElement) >= 0) {
                    greatestYetNode = currentNode;
                }
                // move to the right child to see if a greater element okay
                // since all in left subtree will be smaller
                currentNode = currentNode.rightChild;
            } else // compare(currentNode.element, toElement)>=0
            {  // move to the left child since this element too large
                // so all in right subtree will also be too large
                currentNode = currentNode.leftChild;
            }
        }
        if (greatestYetNode == null) // no satisfactory node found
        {
            return null;
        } else {
            return greatestYetNode.element;
        }
    }

    public SortedSet<E> subSet(E fromElement, E toElement) {
        return new BinarySearchTree<>(rootNode, comparator, fromElement,
                toElement);
    }

    public SortedSet<E> tailSet(E fromElement) {
        return subSet(fromElement, null);
    }

    // outputs the elements stored in the full binary tree (not just
    // the view) using inorder traversal
    public String toString() {
        return " " + rootNode;
    }

    public static void main(String[] args) {  // create the binary search tree
        SortedSet<String> tree = new BinarySearchTree<>();
        PersistentDynamicSet pathSet = new PersistentDynamicSet((BinarySearchTree) tree);
        BalancedPersistentDynamicSet RBset = new BalancedPersistentDynamicSet<>();

        // adding to the trees
        tree.add("cow");
        tree.add("fly");
        tree.add("dog");
        tree.add("bat");
        tree.add("fox");
        tree.add("cat");
        tree.add("eel");
        tree.add("ant");
        pathSet.add("cow");
        pathSet.add("fly");
        pathSet.add("dog");
        pathSet.add("bat");
        pathSet.add("fox");
        pathSet.add("cat");
        pathSet.add("eel");
        pathSet.add("ant");
        RBset.add("cow");
        RBset.add("fly");
        RBset.add("dog");
        RBset.add("bat");
        RBset.add("fox");
        RBset.add("cat");
        RBset.add("eel");
        RBset.add("ant");

        System.out.println("Current BST: " + tree);
        System.out.println("Red-Black Tree: " + RBset);

        System.out.println("Path Tree: ");
        pathSet.printPath();

        //making changes for testing
        tree.remove("eel");
        pathSet.remove("eel");
        RBset.remove("eel");

        tree.add("owl");
        pathSet.add("owl");
        RBset.add("owl");

        System.out.println("Modified BST: " + tree);
        System.out.println("Modified Red-Black Tree: " + RBset);
        System.out.println("Path Tree: ");
        pathSet.printPath();

        SortedSet<String> subtree = tree.subSet("cat", "fox");
        System.out.print("Subtree iteration: ");
        Iterator<String> i = subtree.iterator();
        while (i.hasNext()) {
            System.out.print(i.next());
            if (i.hasNext()) {
                System.out.print(", ");
            }
        }
        System.out.println();
        System.out.println("first element in subtree: " + subtree.first());
        System.out.println("last element in subtree: " + subtree.last());
    }

    // inner class that represents a node in the binary tree where each node consists of the element and links to
    // left child and right child
    protected class RedBlackTree {

        public RedBlackTree leftChild, rightChild, parent;// links to left and right subtrees added parent for RB tree
        public E element;    //value
        public boolean color;     // color of parent link
        public int size;          // subtree count
        int key;

        public RedBlackTree(E element, boolean color, int size) {
            this.element = element;
            this.color = color;
            this.size = size;
        }

        public RedBlackTree(E element) {
            this.element = element;
            leftChild = null;
            rightChild = null;
        }

        //getting subtree count
        public int getSize() {
            return this.size;
        }

        //setting value
        public void setValue(E value) {
            this.element = value;
        }

        //getting element
        public E getValue() {
            return this.element;
        }

        //setting right child
        public void setRight(RedBlackTree node) {
            this.rightChild = node;
        }

        //setting left child
        public void setLeft(RedBlackTree node) {
            this.leftChild = node;
        }

        //getting right child
        public RedBlackTree getRight() {
            return this.rightChild;
        }

        //getting left child
        public RedBlackTree getLeft() {
            return this.leftChild;
        }

        // returns a string representation of the node and
        // its children using inorder (left-this-right) traversal
        public String toString() {
            String output = "[";
            if (leftChild != null) {
                output += "" + leftChild;
            }
            output += "" + element;
            if (rightChild != null) {
                output += "" + rightChild;
            }
            output += "]";
            return output;
        }
    }

    // inner class that represents an Iterator for a binary tree
    private class BinaryTreeIterator implements Iterator<E> {

        private LinkedList<E> list;
        private Iterator<E> iterator;

        public BinaryTreeIterator(RedBlackTree rootNode) {  // puts the elements in a linked list using inorder traversal
            list = new LinkedList<E>();
            traverseInOrder(rootNode);
            iterator = list.iterator();
        }

        // recursive helper method that traverses the subtree from node
        // adding the elements to the list collection
        private void traverseInOrder(RedBlackTree node) {
            if (node != null) {
                traverseInOrder(node.leftChild);
                if (withinView(node.element)) {
                    list.add(node.element);
                }
                traverseInOrder(node.rightChild);
            }
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public E next() {
            return iterator.next();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
