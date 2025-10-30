public class Node {
    private String name;
    private String type;

    private Node next;
    private Node prev;

    public Node(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName(){
        return this.name;
    }
    public String getType(){
        return this.type;
    }
    public Node getNext(){
        return this.next;
    }
    public Node getPrev(){
        return this.prev;
    }
    public void setNext(Node newNode){
        this.next = newNode;
    }
    public void setPrev(Node newNode){
        this.prev = newNode;
    }
}
