/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package old_A3;

import java.util.ArrayList;

/**
 *
 * @author Owner
 */
public class PersistentDynamicSet extends BinarySearchTreeGivenTemplate {

    BinarySearchTreeGivenTemplate tree;

    //class constructor, taking in a normal BST
    PersistentDynamicSet(BinarySearchTreeGivenTemplate tree) {
        tree = this.tree;
    }

    //calling print path recursive for the tree.
    public void printPath() {
        ArrayList<BinaryTreeNode> path = new ArrayList<>();
        this.printPathRecursive(rootNode, path);
    }

    //getting the path for leaf nodes...could possibly develop furthur for printing path for all nodes
    public ArrayList<BinaryTreeNode> printPathRecursive(BinaryTreeNode root, ArrayList<BinaryTreeNode> path) {
        //if not a leaf node, path not found
        if (root == null) {
            System.out.println("Path completed = " + rootNode.equals(root));
            //path.forEach(node -> System.out.print(" " + node.getValue()));
            return path;
        }

        path.add(root);

        //If node is a leaf node
        if (root.getLeft() == null && root.getRight() == null) {
            path.forEach(node -> System.out.print(" " + node.getValue()));
            path.remove(path.size() - 1);
            System.out.println(" -> Path Completed");

            return path;
        }

        //Not a leaf node, add this node to path and continue traverse
        printPathRecursive(root.getLeft(), path);
        printPathRecursive(root.getRight(), path);
        //Remove the root node from the path
        path.remove(path.size() - 1);

        return path;
    }

}
