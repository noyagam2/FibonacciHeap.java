/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
    private static int  num_of_links=0;
    private static int  num_of_cuts=0;
    private int  num_of_marked=0;
    private HeapNode min = null;
    private int num_of_nodes=0;
    private DoublyLinkedList treeList;

    public FibonacciHeap() {
        treeList = new DoublyLinkedList();
    }


    /**
     * public boolean isEmpty()
     *
     * Returns true if and only if the heap is empty.
     *
     */
    public boolean isEmpty()
    {
        return (num_of_nodes == 0);
    }

    /**
     * public HeapNode insert(int key)
     *
     * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
     * The added key is assumed not to already belong to the heap.
     *
     * Returns the newly created node.
     */
    public HeapNode insert(int key)
    {
        HeapNode node = new HeapNode(key);
        if (this.isEmpty()){
            this.min = node;
        }
        else{
            this.min = key < this.min.getKey() ? node : this.min;
        }
        this.num_of_nodes++;
        this.treeList.addFirst(node);
        return node;

        //works in O(1)
    }

    /**
     * public void deleteMin()
     *
     * Deletes the node containing the minimum key.
     *
     */
    public void deleteMin()
    {
        if (this.isEmpty()){
            this.min = null;
            return;
        }
        DeleteMinIsRoot(); //Handle the case where the minimum is one of the binomial trees roots. O(log(n)).
        this.consolidate(); //O(log(n)) amortized
        this.num_of_nodes--;

        //works in O(log(n)) amortized
    }

    /**
     * public HeapNode findMin()
     *
     * Returns the node of the heap whose key is minimal, or null if the heap is empty.
     *
     */
    public HeapNode findMin()
    {
        return this.min;

        //works in O(1)
    }

    /**
     * public void meld (FibonacciHeap heap2)
     *
     * Melds heap2 with the current heap.
     *
     */
    public void meld (FibonacciHeap heap2)
    {

        this.num_of_nodes += heap2.num_of_nodes;
        this.min = this.min.getKey() < heap2.min.getKey() ? this.min : heap2.min;
        this.treeList.addAll(heap2.treeList);

        //works in O(1)
    }

    /**
     * public int size()
     *
     * Returns the number of elements in the heap.
     *
     */
    public int size()
    {
        return num_of_nodes;

        //works in O(1)
    }

    /**
     * public int[] countersRep()
     *
     * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
     * (Note: The size of of the array depends on the maximum order of a tree.)
     *
     */
    public int[] countersRep()
    {
        //We will assume log operations are O(1)
        int highest_rank = (int) (Math.log(this.num_of_nodes) / Math.log(2) +1);
        int[] ranks = new int[highest_rank];
        HeapNode[] array_for_iteration = createArrayForIteration(this.treeList);
        for (HeapNode node : array_for_iteration){
            ranks[node.getRank()]++;
        }
        return ranks;

        //works in O(n)
    }

    /**
     * public void delete(HeapNode x)
     *
     * Deletes the node x from the heap.
     * It is assumed that x indeed belongs to the heap.
     *
     */
    public void delete(HeapNode x)
    {
        int minus_infinity = Integer.MIN_VALUE;
        int delta = x.getKey() + minus_infinity;
        if(x != this.findMin()) {
            this.decreaseKey(x, delta); //O(1) amortized
        }
        this.deleteMin(); //O(log(n)) amortized

        //works in O(log(n)) amortized
    }

    /**
     * public void decreaseKey(HeapNode x, int delta)
     *
     * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
     * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
     */
    public void decreaseKey(HeapNode x, int delta)
    {
        x.setKey(x.getKey()-delta);
        this.min = x.getKey() < this.min.getKey() ? x : this.min;
        if (x.getParent()!= null && x.getKey() <= x.getParent().getKey()){
            this.cut(x);
        }

        //works in O(1) amortized

    }

    /**
     * public int nonMarked()
     *
     * This function returns the current number of non-marked items in the heap
     */
    public int nonMarked()
    {
        return this.num_of_nodes - this.num_of_marked;
    }

    /**
     * public int potential()
     *
     * This function returns the current potential of the heap, which is:
     * Potential = #trees + 2*#marked
     *
     * In words: The potential equals to the number of trees in the heap
     * plus twice the number of marked nodes in the heap.
     */
    public int potential()
    {
        return this.treeList.getSize() + 2*this.num_of_marked;
    }

    /**
     * public static int totalLinks()
     *
     * This static function returns the total number of link operations made during the
     * run-time of the program. A link operation is the operation which gets as input two
     * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
     * tree which has larger value in its root under the other tree.
     */
    public static int totalLinks()
    {
        return num_of_links; // should be replaced by student code
    }

    /**
     * public static int totalCuts()
     *
     * This static function returns the total number of cut operations made during the
     * run-time of the program. A cut operation is the operation which disconnects a subtree
     * from its parent (during decreaseKey/delete methods).
     */
    public static int totalCuts()
    {
        return num_of_cuts; // should be replaced by student code
    }

    /**
     * public static int[] kMin(FibonacciHeap H, int k)
     *
     * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
     * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
     *
     * ###CRITICAL### : you are NOT allowed to change H.
     */
    public static int[] kMin(FibonacciHeap H, int k)
    {
        int[] arr = new int[k];
        FibonacciHeap aux_heap = new FibonacciHeap();
        HeapNode min = H.findMin();
        HeapNode duplicate = min.duplicate();
        aux_heap.insertNode(duplicate);
        for (int i= 0 ; i<k ; i++){
            min = aux_heap.findMin();
            arr[i] = min.getKey();
            HeapNode org_min = min.original;
            DoublyLinkedList children = org_min.getChildren();
            HeapNode[] array_for_iteration = createArrayForIteration(children); // O(deg(H)),each node is a
            // root of a tree with equal or less than 2^deg(H) = 2^log(n) = n nodes
            // Therefore, each node has equal or less than deg(H) children (proved in ex4,q1,c)
            aux_heap.deleteMin();
            for (HeapNode child : array_for_iteration){ // O(deg(H))
                HeapNode duplicate_child = child.duplicate();
                aux_heap.insertNode(duplicate_child);
            }
        }
        return arr;
    }


    //------------------------------------Private Methods------------------------------------

    /**
     * a function that links two trees of the same rank
     * the tree with the smaller root will be the child of the other tree
     * works in O(1)
     * @pre node1 and node2 are trees of the same rank
     * @post node1 is the parent of node2 or vice versa, new tree has rank+1
     * @param node1 the root of the first tree
     * @param node2 the root of the second tree
     * @return the root of the new tree
     */
    private HeapNode link(HeapNode node1, HeapNode node2){
        boolean isNode1Min = (node1.getKey() < node2.getKey());
        HeapNode parent = isNode1Min ? node1 : node2;
        HeapNode child = isNode1Min ? node2 : node1;
        parent.addChild(child);
        parent.setRank(parent.getRank() + 1);
        child.setParent(parent);
        num_of_links++;
        return parent;
    }

    /**
     * a function that fills the buckets array with the trees in the heap
     * the function works recursively. If there's already a tree in the bucket with the same rank as the tree
     * we're currently looking at (the one which has node as root), we link the two trees and put the new tree in the
     * bucket with the rank+1. Since there can only be 1 tree in each bucket,and the number of buckets is O(log(n)),
     * the function works in O(log(n))
     * @pre the buckets array is empty
     * @post the buckets array contains the trees in the heap, after linking them
     * @param node the root of the tree to be added to the buckets array
     * @param ranks an array that contains the number of trees with each rank
     * @param buckets an array that contains the trees in the heap
     */

    private void fillBuckets(HeapNode node, int[] ranks, HeapNode[] buckets){
        int curr_tree_rank = node.getRank();
        if(ranks[curr_tree_rank] == 1){
            HeapNode other_tree = buckets[curr_tree_rank];
            HeapNode new_root = link(node, other_tree);
            buckets[curr_tree_rank] = null;
            ranks[curr_tree_rank] = 0;
            fillBuckets(new_root, ranks, buckets);
        }
        else{
            buckets[curr_tree_rank] = node;
            ranks[curr_tree_rank] = 1;
        }

    }

    /**
     * a function that consolidates the trees in the heap
     * works in O(log(n)) amortized time, O(n) worst case as described in the lecture
     * @pre the heap is not empty
     * @post the heap contains only trees of different ranks
     */
    private void consolidate(){
        if( this.treeList.getSize() == 0){
            return;
        }
        int highest_rank = (int) (Math.log(this.num_of_nodes) / Math.log(2))+3; //Plus 3 for safety, because of the log
        int[] ranks = new int[highest_rank]; //size of the array is log(n)
        HeapNode[] buckets = new HeapNode[highest_rank];
        HeapNode[] array_for_iteration = createArrayForIteration(this.treeList);
        HeapNode curr_min = this.treeList.getFirst();
        for (HeapNode node : array_for_iteration){
            curr_min = node.getKey() < curr_min.getKey() ? node : curr_min;
            fillBuckets(node, ranks, buckets);
        }
        this.min = curr_min;
        this.treeList= new DoublyLinkedList();
        for (HeapNode bucket : buckets) {
            if (bucket != null) {
                this.treeList.addLast(bucket);
            }
        }
    }


    /**
     * a function that creates an array from a list of heapNodes,
     * can be the list of roots or a list of children
     * used for iteration since we can't implement iterator for DoublyLinkedList
     * works in O(n) time
     * @param list the list to be converted to an array
     * @return an array of heapNodes
     */
    private static HeapNode[] createArrayForIteration(DoublyLinkedList list){
        HeapNode[] array_for_iteration = new HeapNode[list.getSize()];
        HeapNode node = list.getFirst();
        for (int i = 0; i < list.getSize(); i++){
            array_for_iteration[i] = node;
            node = node.getNext();
        }
        return array_for_iteration;
    }


    /**
     * a function that deletes the minimum node from the heap if it's a root of a binomial tree
     * works in O(log(n)) since a root of a binomial tree has O(log(n)) children, and we iterate over them
     * @pre the heap is not empty
     * @pre the minimum node is a root of a binomial tree
     * @post the minimum node is deleted from the heap
     */
    private void DeleteMinIsRoot() {
        HeapNode current_min = this.findMin();
        HeapNode insert_children_here = current_min.getPrev(); //we will insert the children of the min node after this node
        this.treeList.remove(current_min); //O(1)

        if (current_min.getChildren().getSize() > 0){
            HeapNode[] array_for_iteration = createArrayForIteration(current_min.getChildren());
            //createArrayForIteration is used to iterate since we can't implement iterator, works in O(n) time
            for (HeapNode child : array_for_iteration){ //O(deg(H)) = O(log(n))
                child.setParent(null);
                if (child.isMarked()){
                    child.setMarked(false);
                    this.num_of_marked--;
                }
                this.treeList.addAfter(child, insert_children_here);
            }
        }
        else if (this.treeList.getSize() == 0){ //if heap had only one node, now it's empty
            this.min = null;
        }
    }

    /**
     * a function that cuts a node from its parent and adds it to the root list
     * the function is recursive - if the parent is marked it cuts it as well
     * works in O(1) amortized time as described in the lecture for cascading cuts
     * @inv: each node looses at most one child if it is a child itself
     * @param x - the node to cut
     * @post - x is not a child of its parent and is a node in the root list
     * @post num_of_cuts >= prev(num_of_cuts)
     * @post num_of_marked <= prev(num_of_marked) (if parent was marked) or
     * num_of_marked = prev(num_of_marked) + 1 (if parent was not marked)
     */

    private void cut(HeapNode x) {
        if(x.getParent() == null){ //you can't cut a root
            return;
        }
        num_of_cuts++;
        HeapNode parent = x.getParent();
        if (x.isMarked()) {  //if x is marked, unmark it
            this.num_of_marked--;
        }
        x.setMarked(false);
        x.setParent(null);

        if (parent!=null) { //if x is not a root, remove it from its parent's children list and add it to the root list
            parent.getChildren().remove(x);
            parent.setRank(parent.getRank() - 1);
            this.treeList.addFirst(x);

            if (parent.isMarked()) { //handle the case where the parent is marked
                this.cut(parent);
            }
            else {  //handle the case where the parent is not marked
                handleParentNotMarked(parent);
            }
        }
    }

    /**
     * a function that handles the case where the parent of a node is not marked
     * works in amortized O(1) time
     * @param parent - the parent of the node that was cut
     * @post - cut operation is done correctly, the invariant of function cut is preserved
     */
    private void handleParentNotMarked(HeapNode parent) {
        HeapNode grandparent = parent.getParent();
        if (grandparent != null) { //if the parent is not a root
            parent.setMarked(true);
            this.num_of_marked++;
            int num_of_marked_siblings = 0;
            HeapNode[] array_for_iteration = createArrayForIteration(grandparent.getChildren());
            for (HeapNode child : array_for_iteration) {
                if (child.isMarked()) {
                    num_of_marked_siblings++;
                }
                if (num_of_marked_siblings > 1) {
                    cut(grandparent);
                    break;
                }
            }
        }
    }

    /**
     * a function that inserts a node to the heap
     * updates the total number of nodes in heap and the minimum node if needed
     * works in O(1) time
     * @pre - the node is not in the heap
     * @pre - the node is a single node (has no children)
     * @post - the node is in the heap
     * @param root the node to insert
     * @return the node that was inserted
     */
    private HeapNode insertNode(HeapNode root){
        this.treeList.addFirst(root);
        this.num_of_nodes+=1;
        if (this.min == null || root.getKey() < this.min.getKey()){
            this.min = root;
        }
        return root;
    }


    //------------------------------------HeapNode------------------------------------//

    /**
     * public class HeapNode
     *
     * If you wish to implement classes other than FibonacciHeap
     * (for example HeapNode), do it in this file, not in another file.
     *
     */

    public static class HeapNode {

        public int key;
        private HeapNode parent;
        private DoublyLinkedList children;
        private HeapNode next;
        private HeapNode prev;
        private HeapNode original;
        private boolean mark;
        private int rank;

        /**
         * constructor for HeapNode
         * @param key the key of the node
         */
        public HeapNode(int key) {
            this.key = key;
            this.parent = null;
            this.mark = false;
            this.children = new DoublyLinkedList();

        }

        /**
         * a function that returns the key of the node
         * @return the key of the node
         */
        public int getKey() {
            return this.key;
        }

        /**
         * a function that sets the key of the node to a new key
         * @param key the new key of the node
         */
        public void setKey(int key) {
            this.key = key;
        }

        /**
         * a function that returns the parent of the node
         * @return the parent of the node
         */
        public HeapNode getParent() {
            return parent;
        }

        /**
         * a function that sets the parent of the node to a new parent
         * @param parent the new parent of the node
         */
        public void setParent(HeapNode parent) {
            this.parent = parent;
        }

        /**
         * a function that returns a LinkedList representing the children of the node
         * @return the children of the node
         */
        public DoublyLinkedList getChildren() {
            return children;
        }

        /**
         * a functions that sets the children of the node to a new LinkedList
         * @param children a new LinkedList of children
         */
        public void setChildren(DoublyLinkedList children) {
            this.children = children;
        }

        /**
         * a function that returns the next node in the list
         * @return the next node in the  list
         */
        public HeapNode getNext() {
            return next;
        }

        /**
         * a function that sets the next node in the list to a new node
         * @param next the new next node in the list
         */
        public void setNext(HeapNode next) {
            this.next = next;
        }

        /**
         * a function that returns the previous node in the list
         * @return the previous node in the list
         */
        public HeapNode getPrev() {
            return prev;
        }

        /**
         * a function that sets the previous node in the list to a new node
         * @param prev the new previous node in the list
         */
        public void setPrev(HeapNode prev) {
            this.prev = prev;
        }

        /**
         * a function that returns true if the node is marked, false otherwise
         * @return true if the node is marked, false otherwise
         */
        public boolean isMarked() {
            return mark;
        }

        /**
         * a function that sets the mark of the node to a new mark
         * @param mark the new mark of the node
         */
        public void setMarked(boolean mark) {
            this.mark = mark;
        }

        /**
         * a function which adds a child to the node
         * works in O(1) time
         * @param child the child to be added
         */
        public void addChild(HeapNode child) {
            this.children.addFirst(child);
        }


        /**
         * a function that returns the rank of the node
         * @return the rank of the node
         */
        public int getRank() {
            return rank;
        }

        /**
         * a function that sets the rank of the node to a new rank
         * @param rank the new rank of the node
         */
        public void setRank(int rank) {
            this.rank = rank;
        }

        /**
         * a function that returns a duplicate node of the node
         * a duplicate node is a node with the same key as the original node, and a pointer to the original node.
         * it has no members other than original and key.
         * works in O(1) time
         * @return a duplicate node of the node
         */
        private HeapNode duplicate() {
            HeapNode duplicate = new HeapNode(this.key);
            duplicate.original = this;
            return duplicate;
        }


    }

    //------------------------------------DoublyLinkedList------------------------------------//

    /**
     * public class DoublyLinkedList
     *
     */
    public static class DoublyLinkedList {

        private HeapNode first;
        private HeapNode last;
        private int size;

        /**
         * constructor for DoublyLinkedList
         */
        public DoublyLinkedList() {
            this.first = null;
            this.last = null;
            this.size = 0;
        }

        /**
         * a function that returns the first node of the list
         * @return the first node of the list
         */
        public HeapNode getFirst() {
            return first;
        }


        /**
         * a function that returns the last node of the list
         * @return the last node of the list
         */
        public HeapNode getLast() {
            return last;
        }


        /**
         * a function that returns the size of the list
         * @return the size of the list
         */
        public int getSize() {
            return size;
        }

        /**
         * a function that adds a node to the end of the list
         * works in O(1) time
         * @param node the node to be added
         */
        public void addLast(HeapNode node) {
            if (this.size == 0) {
                this.first = node;
                this.last = node;
                this.size++;
                node.setPrev(null);
            } else {
                this.last.setNext(node);
                node.setPrev(this.last);
                this.last = node;
                this.size++;
            }
            node.setNext(null);
        }

        /**
         * a function that adds a node to the beginning of the list
         * works in O(1) time
         * @param node the node to be added
         */
        public void addFirst(HeapNode node) {
            if (this.size == 0) {
                this.first = node;
                this.last = node;
                node.setNext(null);
                this.size++;
            } else {
                this.first.setPrev(node);
                node.setNext(this.first);
                this.first = node;
                this.size++;
            }
            node.setPrev(null);
        }

        /**
         * a function that adds a node to the list after a given node
         * works in O(1) time
         * @pre after is a node in the list
         * @post after.getNext() == node
         * @param node the node to be added
         * @param after the node after which the node will be added
         */
        public void addAfter(HeapNode node, HeapNode after) {
            if (after == this.last) {
                this.addLast(node);
            }
            else if (after == null) {
                this.addFirst(node);
            }
            else {
                node.setNext(after.getNext());
                node.setPrev(after);
                after.getNext().setPrev(node);
                after.setNext(node);
                this.size++;
            }
        }

        /**
         * a function that concatenates two lists
         * works in O(1) time
         * @param list the list to be concatenated after our list
         */
        public void addAll(DoublyLinkedList list) {
            if (list.getSize() == 0) {
                return;
            }
            if (this.size == 0) {
                this.first = list.getFirst();
                this.last = list.getLast();
                this.size = list.getSize();
            } else {
                this.last.setNext(list.getFirst());
                list.getFirst().setPrev(this.last);
                this.last = list.getLast();
                this.size += list.getSize();
            }
        }

        /**
         * a function that removes a node from the list
         * works in O(1) time
         * @pre the node is in the list
         * @param node a pointer to the node to be removed
         */
        public void remove(HeapNode node) {
            if (this.size == 1) {
                this.first = null;
                this.last = null;
                this.size--;
            } else if (node == this.first) {
                this.first = this.first.getNext();
                if (this.first != null)
                    this.first.setPrev(null);
                this.size--;
            } else if (node == this.last) {
                this.last = this.last.getPrev();
                this.last.setNext(null);
                this.size--;
            } else {
                node.getPrev().setNext(node.getNext());
                node.getNext().setPrev(node.getPrev());
                node.setNext(null);
                node.setPrev(null);
                this.size--;
            }
        }
    }

}


