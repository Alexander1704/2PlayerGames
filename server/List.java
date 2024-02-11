package server;

  
public class List<T>{
    private class Node<T>{
        private Node<T> next;
        private T content;

        public Node(T content){
            next = null;
            this.content = content;
        }

        public Node getNext(){
            return next;
        }

        public void setNext(Node<T> n){
            next = n;
        }

        public T getContent(){
            return content;
        }
        
        public void setContent(T content){
            this.content = content;
        }
    }

    private Node<T> first;
    private Node<T> last;
    private Node<T> current;

    public List(){
        first = null;
        last = null;
        current = null;
    }

    public void append(T content){
        Node<T> n = new Node<T>(content);
        if(isEmpty()){
            first = n;
            last = n;
        }
        else{
            last.setNext(n);
            last = n;
        }
    }

    public void insert(T content){
        //Leere Elemente werden nicht eingefügt.
        if(content == null){
            return;
        }
        //Falls die Liste leer ist, arbeitet insert
        //identisch zu append.
        if(this.isEmpty()){
            this.append(content);
        }
                
        //Falls die Liste nicht leer ist, aber ich kein
        //current habe, macht die Methode nichts.
        if(!this.hasAccess()){
            return;
        }
        
        Node<T> newNode = new Node<T>(content);
        if(first == current){
            //Fall, dass ich vor das erste Element einfüge
            first = newNode;
            newNode.setNext(current);
        }
        else{
            //Fall, dass ich nicht vor das erste einfüge
            getPrevious().setNext(newNode);
            newNode.setNext(current);
        }
    }
    
    private Node<T> getPrevious(){
        if(!hasAccess() || current == first){
            return null;
        }
        Node<T> previous = first;
        while(previous.getNext() != current){
            previous = previous.getNext();
        }
        return previous;
    }
    
    public boolean hasAccess(){
        return current != null;
    }
    
    public void next(){
        if(!hasAccess()){
            return;
        }
        current = current.getNext();
    }
    
    public void toFirst(){
        current = first;
    }
    
    public void toLast(){
        current = last;
    }
    
    public T getContent(){
        if(hasAccess()){
            return current.getContent();
        }
        else{
            return null;
        }
    }
    
    public void setContent(T content){
        if(!hasAccess() || content == null){
            return;
        }
        current.setContent(content);
    }

    public boolean isEmpty(){
        return first == null;
    }
    
    public void concat(List<T> otherList){
        if(otherList != null){
            otherList.current = otherList.first;
            while(otherList.hasAccess()){
                this.append(otherList.getContent());
                otherList.current = otherList.current.getNext();
            }
        }
    }
    
    public void remove(){
        if(!hasAccess()){
            return;
        }
        
        if(current == first){
            first = first.getNext();
            current = first;
            if(first == null){
                last = null;
            }
        }
        else{
            Node<T> prev = getPrevious();
            prev.setNext(current.getNext());
            current = current.getNext();
            
            //Das letzte Element wurde gelöscht
            if(current == null){
                last = prev;
            }
        }
    }
}
