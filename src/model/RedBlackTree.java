package model;

import java.text.Collator;
import java.util.Locale;

public class RedBlackTree {
    private Node root;
    private int size;
    public RedBlackTree() {
       root=new Node((String)null);
        size = 0;
    }
    public RedBlackTree(String word) {
        size = 1;
        root = new Node(word);
        root.setColour(Colour.BLACK);
        root.setLeft(new Node((String)null));
        root.getLeft().setParent(root);
        root.getLeft().setColour(Colour.BLACK);
        root.setRight(new Node((String)null));
        root.getRight().setParent(root);
        root.getRight().setColour(Colour.BLACK);
        root.setParent(null);
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    public int getBlackHeight(Node node){
            if(node.getWord()==null)return 0;
            if(node.getColour()==Colour.BLACK)
                return 1 + getBlackHeight(node.getRight());
            else
                return 0 + getBlackHeight(node.getRight());
    };


    public Node search(String word, Node node) {
        if (node.getWord() == null) return null;
        Locale locale = Locale.UK;
        Collator collator = Collator.getInstance(locale);
        int result = collator.compare(word, node.getWord());
        if (result == 0) {
            return node;
        } else if (result < 0) {
            return search(word, node.getLeft());
        } else {
            return search(word, node.getRight());
        }

    }
    public Node getMostlyAlike(String word){
        Node mostAlikeNode=this.root;
        Node lastNode=mostAlikeNode;
        while(mostAlikeNode.getRight().getWord()!=null&&mostAlikeNode.getLeft().getWord()!=null){
            lastNode=mostAlikeNode;
            Locale locale = Locale.UK;
            Collator collator = Collator.getInstance(locale);
            int result = collator.compare(word, mostAlikeNode.getWord());
            if (result < 0) {
                mostAlikeNode=mostAlikeNode.getLeft();
            } else {
                mostAlikeNode=mostAlikeNode.getRight();
            }
        }
       return lastNode;

    }
    private Node getNextReplacingNode(Node node){
        if (node.getLeft().getWord()!= null && node.getRight().getWord() != null)
        return node.getRight().getMin();

        if (node.getLeft().getWord() == null && node.getRight().getWord() == null)//leaf
        return null;

        if (node.getLeft().getWord() != null)//single child
            return node.getLeft();
        else
            return node.getRight();
    }
    private void rotateLeft(Node node) {
       Node newNode = node.getRight();
       node.setRight(newNode.getLeft());
       if (newNode.getLeft() != null) {
           newNode.getLeft().setParent(node);
       }
       newNode.setParent(node.getParent());
       if (newNode.getParent() == null) {
           root = newNode;
       } else if (node == node.getParent().getLeft()) {
           node.getParent().setLeft(newNode);
       } else {
           node.getParent().setRight(newNode);
       }
       newNode.setLeft(node);
       node.setParent(newNode);
    }
    private void rotateRight(Node node) {
      Node newNode = node.getLeft();
      node.setLeft(newNode.getRight());
      if (newNode.getRight() != null) {
          newNode.getRight().setParent(node);
      }
      newNode.setParent(node.getParent());
      if (newNode.getParent() == null) {
          root = newNode;
      } else if (node == node.getParent().getLeft()) {
          node.getParent().setLeft(newNode);
      } else {
          node.getParent().setRight(newNode);
      }
      newNode.setRight(node);
      node.setParent(newNode);
    }
    private Node insertBT(String word, Node root) {
        if (this.root.getWord() == null) {
            this.root.setWord(word);
            this.root.setRight(new Node((String)null));
            this.root.getRight().setColour(Colour.BLACK);
            this.root.setLeft(new Node((String)null));
            this.root.getLeft().setColour(Colour.BLACK);
            size = 1;
            return this.root;
        }
        Locale locale = Locale.UK;
        Collator collator = Collator.getInstance(locale);
        int result = collator.compare(word, root.getWord());
        if (result == 0) {
            return null;
        } else if (result < 0) {
            if (root.getLeft().getWord() != null) {
                return insertBT(word, root.getLeft());
            } else {
                root.getLeft().initEmptyChildrenNode(word);
                size++;
                return root.getLeft();
            }
        } else {
            if (root.getRight().getWord() != null) {
                return insertBT(word, root.getRight());
            } else {
                root.getRight().initEmptyChildrenNode(word);
                size++;
                return root.getRight();
            }
        }
    }

    public boolean add(String word) {
        Node newNode = insertBT(word,this.root);
        if (newNode != null) {
            int caseRB = checkInsertRBCase(newNode);
            if (caseRB == 0) {
                insertFixCase_0(newNode);
                return true;
            }
            while (newNode.parent().getColour() != Colour.BLACK) {
                switch (caseRB) {
                    case 1:
                        newNode = insertFixCase_1(newNode);
                        break;
                    case 2:
                        newNode = insertFixCase_2(newNode);
                        break;
                    case 3:
                        newNode = insertFixCase_3(newNode);
                        break;
                    case 4:
                        newNode = insertFixCase_4(newNode);
                        break;
                    case 5:
                        newNode = insertFixCase_5(newNode);
                        break;
                    case 6:
                        return true;
                }
                caseRB = checkInsertRBCase(newNode);
                if (caseRB==0) {
                    insertFixCase_0(newNode);
                    return true;
                }
            }
            return true;
        } else return false;
    }

    private int checkInsertRBCase(Node node) {
        if (node.parent() == root)
            return 6;              /**case_6----No need for fix since the root is always black----**/
        if (this.root == node) {
            return 0;                                  /**insertFixCase_0-----the node inserted will be the root**/
        } else if (node.uncle().getColour() == Colour.RED) {
            return 1;                                  /**insertFixCase_1----uncle is red**/
        } else {
            if (node == node.parent().getRight()) {
                if (node.parent() == node.grandParent().getLeft()) {
                    return 2;                          /**insertFixCase_2-----uncle is black and inserted node is right child and it's parent is left child**/
                } else {
                    return 4;                          /**insertFixCase_4-----uncle is black and inserted node is right child and it's parent is right child**/
                }
            } else {
                if (node.parent() == node.grandParent().getLeft()) {
                    return 5;                          /**insertFixCase_5-----uncle is black and inserted node is left child and it's parent is left child**/
                } else {
                    return 3;                          /**insertFixCase_3-----uncle is black and inserted node is left child and it's parent is right child**/
                }
            }
        }
    }

    private Node insertFixCase_0(Node node) {
        node.setColour(Colour.BLACK);
        return node;
    }

    private Node insertFixCase_1(Node node) {
        node.parent().changeColour();
        node.uncle().changeColour();
        node.grandParent().changeColour();
        return node.grandParent();
    }

    private Node insertFixCase_2(Node node) {
        Node temp = node.parent();
        rotateLeft(node.parent());
        return temp;
    }

    private Node insertFixCase_3(Node node) {
        Node temp = node.parent();
        rotateRight(node.parent());
        return temp;
    }

    private Node insertFixCase_4(Node node) {
        node.parent().setColour(Colour.BLACK);
        node.grandParent().setColour(Colour.RED);
        rotateLeft(node.grandParent());
        return node;
    }

    private Node insertFixCase_5(Node node) {
        node.parent().setColour(Colour.BLACK);
        node.grandParent().setColour(Colour.RED);
        rotateRight(node.grandParent());
        return node;
    }
    public void delete(Node node){
        if(node == null)return;
        size--;
        Node nextReplacingNode =getNextReplacingNode(node);
        boolean bothAreBlack=((nextReplacingNode == null || nextReplacingNode.getColour() == Colour.BLACK) && (node.getColour() == Colour.BLACK));
        Node parent= node.getParent();
        if (nextReplacingNode == null) {//no other node in the tree that can substitute this node(leaf)
            if (node == root) {//we are trying to delete root
                root.setWord(null);
            } else {
                if (bothAreBlack) {//both 'node' and 'nextReplacingNode' are black
                    //deletion case double black
                    fixDoubleBlack(node);
                } else {
                    if (node.getSibling() != null){
                        //set sibling red so after deletion height stays the same
                        node.getSibling().setColour(Colour.RED);
                    }
                }
                if (node==parent.getLeft()) {//if this is a left node
                    node.getParent().setLeft(new Node(node));
                } else {
                    node.getParent().setRight(new Node(node));
                }
            }
            return;
        }
        //'node' NOT A LEAF and have one child only
        if (node.getLeft().getWord() == null || node.getRight().getWord() == null) {
            if (node == root) {
                node.setWord( nextReplacingNode.getWord());//swap with next child
                //null valeus leaves
                node.setLeft(new Node(node));
                node.setRight(new Node(node));
            } else {
                if (node==parent.getLeft()) {
                    parent.setLeft(nextReplacingNode);
                } else {
                    parent.setRight(nextReplacingNode);
                }
                nextReplacingNode.setParent( parent);
                if (bothAreBlack) {
                    fixDoubleBlack(nextReplacingNode);//double black case
                } else {
                    nextReplacingNode.setColour( Colour.BLACK);//set the replaced member to be black
                }
            }
            return;
        }
        //'node' NOT A LEAF & HAVE 2 CHILDREN
        String tmp=nextReplacingNode.getWord();
        nextReplacingNode.setWord(node.getWord());
        node.setWord(tmp);
        delete(nextReplacingNode);
    }
    private void fixDoubleBlack(Node node){
        if (node == root)//base case
            return;
        Node sibling = node.getSibling();
        Node parent = node.getParent();
        if (sibling.getColour() == Colour.RED) {//my cousin is red but node is black
                parent.setColour(Colour.RED);
                sibling.setColour(Colour.BLACK);
                if (sibling==parent.getLeft()) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
                fixDoubleBlack(node);//recursive until root
            } else {
                if (sibling.hasRedChild()) {//if sibling has right/left red child
                    //is it left child ?
                    if (sibling.getLeft() != null && sibling.getLeft().getColour() == Colour.RED) {
                        if (sibling==parent.getLeft()) {
                            sibling.getLeft().setColour( sibling.getColour());
                            sibling.setColour(parent.getColour());
                            rotateRight(parent);
                        } else {
                            sibling.getLeft().setColour(parent.getColour());
                            rotateRight(sibling);
                            rotateLeft(parent);
                        }
                     // no its right child
                    } else {
                        if (sibling==parent.getLeft()) {
                            sibling.getRight().setColour(parent.getColour());
                            rotateLeft(sibling);
                            rotateRight(parent);
                        } else {
                            sibling.getRight().setColour(sibling.getColour());
                            sibling.setColour(parent.getColour());
                            rotateLeft(parent);
                        }
                    }
                    parent.setColour(Colour.BLACK);

                } else {//both sibling children are black
                    sibling.setColour(Colour.RED);
                    if (parent.getColour() == Colour.BLACK)
                        fixDoubleBlack(parent);
                    else
                        parent.setColour(Colour.BLACK);
                }
            }
    }
}
