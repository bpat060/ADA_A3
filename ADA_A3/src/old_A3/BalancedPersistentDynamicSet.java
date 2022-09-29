/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package old_A3;

/**
 *
 * @author Owner
 */
//reference
//coded and developed from psuedo code and writing in https://algorithmtutor.com/Data-Structures/Tree/Red-Black-Trees/
public class BalancedPersistentDynamicSet<E extends Comparable<E>> extends BinarySearchTreeGivenTemplate {

    public static final boolean RED = true;
    public static final boolean BLACK = false;
    public BinaryTreeNode root;

    //empty constructor for now
    public BalancedPersistentDynamicSet() {

    }

    //if the node is already red
    private boolean isRed(BinaryTreeNode node) {
        if (node == null) {
            return false;
        }
        return node.color == RED;
    }

    // number of node in subtree rooted at x; 0 if x is null
    private int size(BinaryTreeNode node) {
        if (node == null) {
            return 0;
        }
        return node.size;
    }

    //size of node
    public int size() {
        return size(root);
    }

    //if node is empty
    public boolean isEmpty() {
        return root == null;
    }

    //adding node
    public void insert(E element) {
        //if element is null then
        if (element == null) {
            delete(element);
            return;
        }
        root = insert(root, element);
        root.color = BLACK;
        // assert check();
    }

    // insert the key-value pair in the subtree rooted at node
    private BinaryTreeNode insert(BinaryTreeNode node, E element) {
        if (node == null) {
            return new BinaryTreeNode(element, RED, 1);
        }

        //cannot compare coz E cannot turn into int...need to fix this
        int compare = element.compareTo(node.element);
        if (compare < 0) {
            node.leftChild = insert(node.leftChild, element);
        } else if (compare > 0) {
            node.rightChild = insert(node.rightChild, element);
        } else {
            node.element = element;
        }
        // fix-up any right-leaning links
        if (isRed(node.rightChild) && !isRed(node.leftChild)) {
            node = rotateLeft(node);
        }
        if (isRed(node.leftChild) && isRed(node.leftChild.leftChild)) {
            node = rotateRight(node);
        }
        if (isRed(node.leftChild) && isRed(node.rightChild)) {
            flipColors(node);
        }
        node.size = size(node.leftChild) + size(node.rightChild) + 1;

        return node;
    }

    public boolean contains(E element) {
        return element == root.getValue();
    }

    //deletes specified element from the tree
    public void delete(E element) {
        //if no element or if element is null, throws exception.
        if (!contains(element) || element == null) {
            throw new IllegalArgumentException("argument to delete() is null or does not exist");
        }

        // if both children of root are black, set root to red
        if (!isRed(root.leftChild) && !isRed(root.rightChild)) {
            root.color = RED;
        }

        root = delete(root, element);
        if (!isEmpty()) {
            root.color = BLACK;
        }
    }

    // delete the key-value pair with the given key rooted at node
    private BinaryTreeNode delete(BinaryTreeNode node, E element) {
        //if element compared to element in node is less than 0
        if (element.compareTo(node.element) < 0) {
            if (!isRed(node.leftChild) && !isRed(node.leftChild.leftChild)) {
                node = moveRedLeft(node); //moving red to left
            }
            node.leftChild = delete(node.leftChild, element);
        } else {
            //if nodes left child is red
            if (isRed(node.leftChild)) {
                node = rotateRight(node); //rotate node to right
            }
            //if element compared to node element is 0 and nodes right child is null
            if (element.compareTo(node.element) == 0 && (node.rightChild == null)) {
                return null;
            }
            //if nodes right child is not not and nodes right child's left child.
            if (!isRed(node.rightChild) && !isRed(node.rightChild.leftChild)) {
                node = moveRedRight(node); // node is red node moved to the right.
            }
            //if element compared to node element is 0
            if (element.compareTo(node.element) == 0) {
                BinaryTreeNode x = min(node.rightChild);
                node.element = x.element;
                node.rightChild = deleteMin(node.rightChild);
            } else {
                node.rightChild = delete(node.rightChild, element);
            }
        }
        return balance(node);
    }

    //classes yet to be filled out which are required, and many to add as this class is incomplete
    private BinaryTreeNode rotateLeft(BinaryTreeNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private BinaryTreeNode rotateRight(BinaryTreeNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void flipColors(BinaryTreeNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private BinaryTreeNode moveRedLeft(BinaryTreeNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private BinaryTreeNode moveRedRight(BinaryTreeNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private BinaryTreeNode balance(BinaryTreeNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private BinaryTreeNode min(BinaryTreeNode rightChild) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private BinaryTreeNode deleteMin(BinaryTreeNode rightChild) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
