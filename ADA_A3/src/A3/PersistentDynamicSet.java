/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package A3;

import java.util.ArrayList;

/**
 *
 * @author Owner
 */
public class PersistentDynamicSet extends BinarySearchTree {

    BinarySearchTree tree;

    // color constants for printing
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_BLACK = "\u001B[30m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_RESET = "\u001B[0m";

    //constructor taking in a bst
    PersistentDynamicSet(BinarySearchTree tree) {
        tree = this.tree;
    }

    public void printPath() {//print path
        ArrayList<RedBlackTree> path = new ArrayList<>();//making an arraylist of the node for paths
        this.printPathCode(rootNode, path);//calling print path code
    }

    //if leaf nodes than path gets printed
    public ArrayList<RedBlackTree> printPathCode(RedBlackTree tree, ArrayList<RedBlackTree> path) {
        //if not a leaf node, path not found so just returns path
        if (tree == null) {
            return path;
        }
        //tree added to path array
        path.add(tree);

        //If node is a leaf node
        if (tree.getLeft() == null && tree.getRight() == null) {
            path.forEach(node -> System.out.print(TEXT_GREEN + " " + node.getValue() + TEXT_RESET));//each path gets node values
            path.remove(path.size() - 1);//removing from path array
            System.out.println(" --> Success");
            return path;
        }
        //Not a leaf node, add this node to path and continue transverse
        printPathCode(tree.getLeft(), path);
        printPathCode(tree.getRight(), path);
        path.remove(path.size() - 1);//Remove the root node from the path
        return path;
    }

}
