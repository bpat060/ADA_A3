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
//
public class BalancedPersistentDynamicSet<E extends Comparable<E>> extends BinarySearchTree {

    public static final boolean RED = true;
    public static final boolean BLACK = false;
    public BinaryTreeNode checkNode, x, y, r, w;

    //empty constructor for now
    public BalancedPersistentDynamicSet() {

    }

    private void insert(BinarySearchTree tree, BinaryTreeNode z) {
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
        z.parent = y;

        if (y == null) {
            tree.root = z;
        } else if (z.key < y.key) {
            y.leftChild = z;
        } else {
            y.rightChild = z;
        }
        z.leftChild = null;
        z.rightChild = null;
        z.color = RED;
        insertFixUp(tree, z);
    }

    private void insertFixUp(BinarySearchTree tree, BinaryTreeNode z) {
        do {
            if (z.parent == z.parent.parent.leftChild) {
                y = z.parent.parent.rightChild;
                if (y != null && y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.rightChild) {
                        z = z.parent;
                        rotateLeft(tree, z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateRight(tree, z.parent.parent);
                }
            } else {
                //change this so that it is same as above but
                //left and right interchanged
                y = z.parent.parent.rightChild;
                if (y != null && y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.rightChild) {
                        z = z.parent;
                        rotateLeft(tree, z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateRight(tree, z.parent.parent);
                }
            }
        } while (z.parent != null && z.parent.color == RED);
        tree.root.color = BLACK;
    }

    private void delete(BinarySearchTree tree, BinaryTreeNode z) {
        checkNode = null;
        if (z.leftChild != null && z.rightChild != null) {
            r = z.rightChild;
            do {
                r = r.leftChild;
            } while (r.leftChild == null);
            r.parent.leftChild = r.rightChild;
            if (r.rightChild != null) {
                r.rightChild.parent = r.parent;
            }
            if (r.color == BLACK) {
                checkNode = r.rightChild;
            }
            r.leftChild = z.leftChild;
            z.leftChild.parent = r;
            r.rightChild = z.rightChild;
            z.rightChild.parent = r;
            r.color = z.color;
        } else if (z.leftChild != null || z.rightChild == null) {
            if (z.leftChild != null) {
                r = z.leftChild;
            } else {
                r = z.rightChild;
            }
            if (z.color == BLACK) {
                checkNode = r;
            }
        } else {
            r = null;
            if (z.color == BLACK) {
                checkNode = z.parent;
            }
        }
        if (r != null) {
            r.parent = z.parent;
        }
        if (z.parent == null) {
            tree.root = r;
        } else if (z == z.parent.leftChild) {
            z.parent.leftChild = r;
        } else {
            z.parent.rightChild = r;
        }
        if (checkNode != null) {
            deleteFixUp(tree, checkNode);
        }
    }

    private void deleteFixUp(BinarySearchTree tree, BinaryTreeNode x) {
        do {
            if (x == x.parent.leftChild) {
                w = x.parent.rightChild;
                if (w != null && w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(tree, x.parent);
                    w = x.parent.rightChild;
                }
                if ((w == null || (w.leftChild == null || w.leftChild.color == BLACK)) && (w.rightChild == null || w.rightChild.color == BLACK)) {
                    if (w != null) {
                        w.color = RED;
                    }
                    x = x.parent;
                } else {
                    if (w.rightChild == null || w.rightChild.color == BLACK) {
                        w.leftChild.color = BLACK;
                        w.color = RED;
                        rotateRight(tree, w);
                        w = x.parent.rightChild;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.rightChild.color = BLACK;
                    rotateLeft(tree, x.parent);
                    x = tree.root;
                }
            } else {
                //copied above but need to interchange
                //left and right
                w = x.parent.rightChild;
                if (w != null && w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(tree, x.parent);
                    w = x.parent.rightChild;
                }
                if ((w == null || (w.leftChild == null || w.leftChild.color == BLACK)) && (w.rightChild == null || w.rightChild.color == BLACK)) {
                    if (w != null) {
                        w.color = RED;
                    }
                    x = x.parent;
                } else {
                    if (w.rightChild == null || w.rightChild.color == BLACK) {
                        w.leftChild.color = BLACK;
                        w.color = RED;
                        rotateRight(tree, w);
                        w = x.parent.rightChild;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.rightChild.color = BLACK;
                    rotateLeft(tree, x.parent);
                    x = tree.root;
                }

            }

        } while (x != tree.root && x.color == BLACK);
        x.color = BLACK;
    }

    private void rotateLeft(BinarySearchTree tree, BinaryTreeNode x) {
        y = x.rightChild;
        x.rightChild = y.leftChild;
        if (y.leftChild != null) {
            y.leftChild.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            tree.root = y;
        } else {
            if (x == x.parent.leftChild) {
                x.parent.leftChild = y;
            } else {
                x.parent.rightChild = y;
            }
        }
        y.leftChild = x;
        x.parent = y;
    }

    private void rotateRight(BinarySearchTree tree, BinaryTreeNode x) {
        //find a way to flip the rotate left copied below
        y = x.rightChild;
        x.rightChild = y.leftChild;
        if (y.leftChild != null) {
            y.leftChild.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            tree.root = y;
        } else {
            if (x == x.parent.leftChild) {
                x.parent.leftChild = y;
            } else {
                x.parent.rightChild = y;
            }
        }
        y.leftChild = x;
        x.parent = y;

    }

}

//public class BinaryTreeNode {
//
//    public BinaryTreeNode leftChild, rightChild, parent;
//    public E element;
//
//    public BinaryTreeNode(E element) {
//        this.element = element;
//        leftChild = null;
//        rightChild = null;
//        parent = null;
//    }
//
//    // returns a string representation of the node and
//    // its children using inorder (left-this-right) traversal
//    public String toString() {
//        String output = "[";
//        if (leftChild != null) {
//            output += "" + leftChild;
//        }
//        output += "" + element;
//        if (rightChild != null) {
//            output += "" + rightChild;
//        }
//        output += "]";
//        return output;
//    }
//}
