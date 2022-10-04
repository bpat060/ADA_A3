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
//reference
//https://canvas.aut.ac.nz/courses/10962/pages/course-material
//coded from the psuedo code in chapter 5 of the manual.
public class BalancedPersistentDynamicSet<E extends Comparable<E>> extends BinarySearchTree {

    public static final boolean RED = true;
    public static final boolean BLACK = false;
    public RedBlackTree checkNode, x, y, r, w;

    //empty constructor for now
    public BalancedPersistentDynamicSet() {
        this.checkNode = checkNode;
        this.x = x;
        this.y = y;
        this.r = r;
        this.w = w;
    }

    private void insert(BinarySearchTree tree, RedBlackTree z) {
        //finding the y node which will be the parent node of z
        y = null;
        x = tree.root;
        do {
            y = x;
            if (z.key < x.key) {
                x = x.leftChild;
            } else {
                x = x.rightChild;
            }
        } while (x != null);
        //adding node z as a child of y
        z.parent = y;
        if (y == null) {//if y is null
            tree.root = z;
        } else if (z.key < y.key) {//else if z's key is smaller than y's key
            y.leftChild = z;
        } else {
            y.rightChild = z;
        }
        //making sure the added node does not have a child
        z.leftChild = null;
        z.rightChild = null;
        z.color = RED;//declaring the node z as red
        insertFixUp(tree, z);//fixing the labeling of the tree
    }

    private void insertFixUp(BinarySearchTree tree, RedBlackTree z) {
        do {
            //while parent of z is not null and z is red...
            if (z.parent == z.parent.parent.leftChild) {// if parent z is a left child
                y = z.parent.parent.rightChild; //y is uncle of z or null
                if (y != null && y.color == RED) {
                    //if y is not null and red
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    //if z is the same as the rightchild of z's parent
                    if (z == z.parent.rightChild) {
                        z = z.parent;
                        rotateLeft(tree, z);//rotate tree left
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;//z's parents parent iss red
                    rotateRight(tree, z.parent.parent);//rotate tree right
                }
            } else {
                y = z.parent.parent.leftChild;
                //if y is not null and red
                if (y != null && y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent; //z equals to z's parents parent
                } else {
                    if (z == z.parent.leftChild) {
                        z = z.parent;
                        rotateRight(tree, z); //rotate tree right
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(tree, z.parent.parent);//rotate tree left
                }
            }
        } while (z.parent != null && z.parent.color == RED);
        tree.root.color = BLACK;//root node of tree set to black
    }

    private void delete(BinarySearchTree tree, RedBlackTree z) {
        //finding node r that will take the place of z
        checkNode = null;
        //if z's left and right childs are not null, it has 2 children
        if (z.leftChild != null && z.rightChild != null) {
            //finding successor node
            r = z.rightChild;
            //do...
            do {
                r = r.leftChild;
            } while (r.leftChild == null);//while r's left child is null
            r.parent.leftChild = r.rightChild;
            if (r.rightChild != null) {//if r's right child is null
                r.rightChild.parent = r.parent;//sets the r's right child parent to r's parent
            }
            if (r.color == BLACK) {//if r is black
                checkNode = r.rightChild;
            }
//r adopting both child nodes of z
            r.leftChild = z.leftChild;
            z.leftChild.parent = r;
            r.rightChild = z.rightChild;
            z.rightChild.parent = r;
            r.color = z.color;
            //if z has one child
        } else if (z.leftChild != null || z.rightChild == null) {
            if (z.leftChild != null) { //if left child is not null
                r = z.leftChild; // r is z's leftchild
            } else {
                r = z.rightChild;//r is z's rightchild
            }
            if (z.color == BLACK) {
                checkNode = r;
            }
        } else {//if z has no children
            r = null;
            if (z.color == BLACK) {
                checkNode = z.parent;
            }
        }
        //updating the link with the parent of z
        //if r is not null
        if (r != null) {
            r.parent = z.parent;//z and r have the same parent
        }
        if (z.parent == null) {//if z's parent is null
            tree.root = r;//the root of the tree is r
        } else if (z == z.parent.leftChild) {//is z is z's parents leftchild
            z.parent.leftChild = r;//z's parents leftchild is r
        } else {
            z.parent.rightChild = r;
        }
        //if checknode is not null
        if (checkNode != null) {
            deleteFixUp(tree, checkNode);//running the delete fixup function
        }
    }

    private void deleteFixUp(BinarySearchTree tree, RedBlackTree x) {
        do {
            if (x == x.parent.leftChild) {//if x is x's parents leftchild
                w = x.parent.rightChild;//w is sibling of x
                if (w != null && w.color == RED) {//case one
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(tree, x.parent);
                    w = x.parent.rightChild; //sibling w is not black
                }
                //case two
                if ((w == null || (w.leftChild == null || w.leftChild.color == BLACK)) && (w.rightChild == null || w.rightChild.color == BLACK)) {
                    if (w != null) {
                        w.color = RED;
                    }
                    x = x.parent;
                } else {
                    //case three
                    if (w.rightChild == null || w.rightChild.color == BLACK) {
                        w.leftChild.color = BLACK;
                        w.color = RED;
                        rotateRight(tree, w);
                        w = x.parent.rightChild;//right child of w is now red
                    }
                    //case four
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.rightChild.color = BLACK;
                    rotateLeft(tree, x.parent);
                    x = tree.root;
                }
            } else { //if x is rightchild
                w = x.parent.leftChild;
                if (w != null && w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(tree, x.parent);
                    w = x.parent.leftChild;
                }
                if ((w == null || (w.rightChild == null || w.rightChild.color == BLACK)) && (w.leftChild == null || w.leftChild.color == BLACK)) {
                    if (w != null) {
                        w.color = RED;
                    }
                    x = x.parent;
                } else {
                    if (w.leftChild == null || w.leftChild.color == BLACK) {
                        w.rightChild.color = BLACK;
                        w.color = RED;
                        rotateLeft(tree, w);
                        w = x.parent.leftChild;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.leftChild.color = BLACK;
                    rotateRight(tree, x.parent);
                    x = tree.root;
                }
            }
        } while (x != tree.root && x.color == BLACK);
        x.color = BLACK;
    }

    private void rotateLeft(BinarySearchTree tree, RedBlackTree x) {
        y = x.rightChild; //y is going up one level
        //leftchild of y becomes the right child of x
        x.rightChild = y.leftChild;
        //if y's leftchild is not null
        if (y.leftChild != null) {
            y.leftChild.parent = x;
        }
        //making y the parent
        y.parent = x.parent; //reassigning y's parent
        //if x's parent is null
        if (x.parent == null) { //if x is root node of the tree
            tree.root = y;
        } else {
            //loops to find whether x is a left child or right of its parent
            if (x == x.parent.leftChild) {
                x.parent.leftChild = y;
            } else {
                x.parent.rightChild = y;
            }
        }
        y.leftChild = x;
        x.parent = y;
    }

    private void rotateRight(BinarySearchTree tree, RedBlackTree x) {
        y = x.leftChild;//y is going up one level
        //leftchild of y becomes the right child of x
        x.leftChild = y.rightChild;
        //if y's right child is not null
        if (y.rightChild != null) {
            y.rightChild.parent = x;
        }
        //making y the parent
        y.parent = x.parent; //reassigning y's parent
        if (x.parent == null) {//if x is root node of the tree
            tree.root = y;
        } else {
            //loops to find whether x is a left child or right of its parent
            if (x == x.parent.rightChild) {
                x.parent.rightChild = y;
            } else {
                x.parent.leftChild = y;
            }
        }
        y.rightChild = x;
        x.parent = y;

    }

}
