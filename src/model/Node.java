package model;

/**
 * ----false is black true is red----
 */
public class Node {
    private Node Parent;
    private Node left;
    private Node right;
    private Colour colour;
    private String word;

    public Node(String word) {
        left = null;
        right = null;
        this.word = word;
    }
    public Node(Node parent){
        left = null;
        right = null;
        Parent=parent;
        word = null;
        colour=Colour.BLACK;
    }

    public void initEmptyChildrenNode(String word){
        this.setWord(word);
        this.setColour(Colour.RED);
        this.setRight(new Node((String) null));
        this.getRight().setColour(Colour.BLACK);
        this.getRight().setParent(this);
        this.setLeft(new Node((String) null));
        this.getLeft().setParent(this);
        this.getLeft().setColour(Colour.BLACK);
    }
    public Node getMin(){
        Node minNode=this;
        while(minNode.getLeft().getWord()!=null){
            minNode=minNode.getLeft();
        }
        return minNode;
    }
    public Node getMax(){
        Node maxNode=this;
        while(maxNode.getRight().getWord()!=null){
            maxNode=maxNode.getRight();
        }
        return maxNode;
    }
    public Node getMostly(){
        Node maxNode=this;
        while(maxNode.getRight().getWord()!=null){
            maxNode=maxNode.getRight();
        }
        return maxNode;
    }
    public Node parent() {
        return this.getParent();
    }

    public Node grandParent() {
        return this.getParent().getParent();
    }

    public Node getParent() {
        return Parent;
    }

    public void setParent(Node parent) {
        Parent = parent;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }
    public void moveDown(Node newParent) {
        if (this.getParent() != null) {
            if (this==this.getParent().getLeft()) {
                this.getParent().setLeft(newParent);
            } else {
                this.getParent().setRight(newParent);
            }
        }
        newParent.setParent(this.getParent());
        this.setParent(newParent);
    }

public void setRight(Node right) {
        this.right = right;
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Node uncle() {
        if (Parent == Parent.getParent().getLeft()) {
            return Parent.getParent().getRight();
        } else {
            return Parent.getParent().getLeft();
        }
    }

    public void changeColour() {
        if (this.colour == Colour.BLACK) {
            this.colour = Colour.RED;
        } else {
            this.colour = Colour.BLACK;
        }
    }
    static void print2DUtil(Node root, int space ,int COUNT)
    {
        // Base case
        if (root == null)
            return;

        // Increase distance between levels
        space += COUNT;

        // Process right child first
        print2DUtil(root.right, space,COUNT);

        // Print current node after space
        // count
        System.out.print("\n");
        for (int i = COUNT; i < space; i++)
            System.out.print(" ");
        System.out.print(root.word+"."+root.getColour() + "\n");

        // Process left child
        print2DUtil(root.left, space,COUNT);
    }

    // Wrapper over print2DUtil()
    static void print2D(Node root,int COUNT)
    {
        // Pass initial space count as 0
        print2DUtil(root, 0,COUNT);
    }
    public Node getSibling(){
        if (this.getParent() == null)
            return null;

        if (this==this.getParent().getRight())
            return this.getParent().getLeft();

        return this.getParent().getRight();
    }
    public boolean hasRedChild(){
        return((this.getLeft()!=null&&this.getLeft().getColour()==Colour.RED)||(this.getRight()!=null&&this.getRight().getColour()==Colour.RED));
    }
}