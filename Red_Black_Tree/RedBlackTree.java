import java.util.*;
/*
RED BLACK TREE IN JAVA
*/


public class RedBlackTree{
    public static int BLACK=1;
    public static int RED=0;
    public static class Node{
        int data;
        int color;
        Node left;
        Node right;
        Node parent;
        boolean isNull;
    }
    public static Node createNode(int data, int color, Node parent)
    {
        Node node=new Node();
        node.data=data;
        node.color=color;
        node.left=nullNode(node);
        node.isNull=false;
        node.right=nullNode(node);
        if(parent!=null)
        {
            node.parent=parent;
        }
        return node;
    }
    public static Node nullNode(Node parent)
    {
        Node leaf=new Node();
        leaf.color=BLACK;
        leaf.isNull=true;
        leaf.parent=parent;
        return leaf;
    }
    public void rotate(Node root, boolean changeColor, String type)
    {   
        /*
        Example of Left Rotation:
            x                               y
          /   \                           /   \ 
         t1    y                         x     z
              /  \   ----->            /  \   /  \
             t2   z                   t1  t2  t3  t4
                 / \
                t3  t4
        */
        /*
        Example of Right Rotation:
                x                            y
               / \                         /   \ 
              y  t4                       z     x
             / \      ------>>>          / \   / \
            z  t3                       t1 t2 t3 t4
           / \
          t1  t2
        */
        
        Node parent = root.parent;
            root.parent = parent.parent;
            if(parent.parent!=null)
            {
                if(parent.parent.right==parent)
                {
                    parent.parent.right = root;
                }
                else{
                    parent.parent.left = root;
                }
            }
        switch(type)
        {
            case "right":{   
                Node right = root.right;
                root.right = parent;
                parent.parent = root;
                parent.left = right;
                if(right != null){
                    right.parent = parent;
                }
                if(changeColor)
                {
                    root.color=BLACK;
                    parent.color = RED;
                }
                break;
            }
            case "left":{
                Node left = root.left;
                root.left = parent;
                parent.parent = root;
                parent.right = left;
                if(left!=null)
                {
                    left.parent = parent;
                }
                if(changeColor)
                {
                    root.color= BLACK;
                    parent.color = RED;
                }
            }
        }
    }
    public boolean isLeftChild(Node root)
    {
        Node parent= root.parent;
        if(parent !=null)
        {
            if(parent.left==root)
            {
                return true;
            }
        }
        else
        {
            return false;
        }
        return false;
    }
    public Node insert(Node root, int data)
    {   
        return insert(null, root, data);
    }
    private Node insert(Node parent, Node root, int data)
    {   
        boolean isLeft;

        // root == null means tree is empty and root.isNull signifies we have reached a null black node
        if(root == null || root.isNull)
        {   
            //is parent!=null signifies we are inserting node in a non-empty tree.
            //Thus the default color of node will be RED otherwise it will be BLACK.
            if(parent!=null)
            {
                return createNode(data,RED,parent);
            }
            else
            {
                return createNode(data,BLACK,null);
            }
        }
        if(root.data==data)
        {
            System.out.println("Insertion of Duplicate Data");
        }
        /* 
        Checking if the value of the root node is greater than the value to be inserted.
        If lesser than the root node move to left else right. 
        */
        if(root.data>data)
        {
            Node left=insert(root, root.left, data);
            //if left became the parent of the root means rotation occured at lower level of the root. 
            //Returning left will help changing the child of other nodes
            if(left==root.parent)
            {
                return left;
            }
            root.left=left;
            isLeft=true;
        }
        else
        {
            Node right=insert(root,root.right,data);
            //if left became the parent of the root means rotation occured at lower level of the root.
            //Returning left will help changing the child of other nodes

            if(right==root.parent)
            {
                return right;
            }
            root.right=right;
            isLeft=false;
        }
        //isLeft is true that means the parent of the node is the left child of its parent.
        if(isLeft)
        {   
            //RED-RED CONFLICT
            Node sibling = findSiblingNode(root);
            if(root.color==RED && root.left.color==RED)
            {   
                /*Two things are possible here.
                The uncle of the node to be inserted (say n) is red
                if so then 
                    (i) change the color of parent and uncle as black
                    (ii) change the color of grand parent as red 
                    Continue above two steps till you reach the root node
                else
                    The node has no uncle or colour of uncle is black
                */

                if(sibling==null || sibling.color==BLACK)
                {   /*Node is the left child of its parent.
                      Right rotation is required
                    */
                    if(isLeftChild(root))
                    {
                        rotate(root, true, "right");
                    }
                     else
                    {
                    rotate(root.left, false, "right");
                    root=root.parent;
                    rotate(root, true, "left");

                    }
                }
                else
                {
                    //This case is for when the uncle is red as well. In that case recolouring is required
                    root.color=BLACK;
                    sibling.color=BLACK;

                    if(root.parent.parent!=null)
                    {
                        root.parent.color=RED;
                    }
                }
            }
        }
        else
        {
           if(root.color==RED && root.right.color==RED)
           {  
               Node sibling = findSiblingNode(root);
               sibling=findSiblingNode(root);
               if(sibling == null || sibling.color==BLACK)
               {
                   if(!isLeftChild(root))
                   {
                       rotate(root,true,"left");
                   }
                   else{
                       rotate(root.right,false,"left");
                       root=root.parent;
                       rotate(root, true,"right");
                   }
               }
               else
               {
                   root.color=BLACK;
                   sibling.color=BLACK;
                   if(root.parent.parent!=null)
                   {
                       root.parent.color=RED;
                   }
               }
           } 
        }
        return root;
    }
     private Node findSiblingNode(Node root) {
        Node parent = root.parent;
        if(parent==null)
        {
            return null;
        }
        if(isLeftChild(root)) 
        {
            return parent.right.isNull ? null : parent.right;
        } 
        else 
        {
            return parent.left.isNull ? null : parent.left;
        }
    }
    
    private void printInOrder(Node root)
    {
        if (root == null || root.isNull) 
            return; 
        
        printInOrder(root.left); 
        System.out.print(root.data +(root.color == BLACK ? "B" : "R")+" "); 
        printInOrder(root.right); 
    }

    private void displayRedBlackTree(Node root, int space) {
        
        if(root == null || root.isNull) {
            return;
        }
        displayRedBlackTree(root.right, space + 20);
        for(int i=0; i < space; i++) {
            System.out.print(" ");
        }
        System.out.println(root.data + " " + (root.color == BLACK ? "B" : "R"));
        
        
        displayRedBlackTree(root.left, space + 20);
    }
    public static void main(String args[])
    {   
        Node root = null;
        RedBlackTree redBlackTree = new RedBlackTree();

        root = redBlackTree.insert(root, 10);
        root = redBlackTree.insert(root, 25);
        root = redBlackTree.insert(root, 5);
        root = redBlackTree.insert(root, 28);
        root = redBlackTree.insert(root, 76);
        root = redBlackTree.insert(root, 55);
        root = redBlackTree.insert(root, 50);
        root = redBlackTree.insert(root, 15);
        root = redBlackTree.insert(root, 18);
        
        redBlackTree.displayRedBlackTree(root,0);
        /* 10 is the root node
        OUTPUT:
                                                                            76 R
                                                55 B
                                                                    50 R
                            28 R
                                                                    25 R
                                                18 B
                                                                    15 R
        10 B
                            5 B
        */
        System.out.println("Inorder traversal:");
        redBlackTree.printInOrder(root);
        /* Inorder traversal:
           5B 10B 15R 18B 25R 28R 50R 55B 76R */
        System.out.println();
    }
}
