import java.util.LinkedList;

// This imports are used for printing the heap
import java.util.List;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.decorator.BorderTreeNodeDecorator;
import hu.webarticum.treeprinter.printer.traditional.TraditionalTreePrinter;
import hu.webarticum.treeprinter.text.ConsoleText;


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
    private LinkedList<HeapNode> treeList;

    public FibonacciHeap() {
        treeList = new LinkedList<>();
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
        this.num_of_nodes--;
        int index_of_min = this.treeList.indexOf(this.min); // O(n)!!!!
        //TODO find a way to do this in O(1)

        this.treeList.remove(this.min);
        if (this.min.getChildren().size() > 0){
            for (HeapNode child : this.min.getChildren()){
                child.setParent(null);
                if (child.isMarked()){
                    child.setMarked(false);
                    this.num_of_marked--;
                }
                this.treeList.add(index_of_min, child);
                index_of_min++;
            }
        }
        this.consolidate();

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
        int highest_rank = (int) (Math.log(this.num_of_nodes) / Math.log(2) +1);
        int[] ranks = new int[highest_rank];
        for (HeapNode node : this.treeList){
            ranks[node.getRank()]++;
        }
        return ranks;
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
        this.decreaseKey(x, Integer.MIN_VALUE);
        this.deleteMin();
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
        if (x.getKey() < x.getParent().getKey()){
            this.cascadingCuts(x);
        }

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
        return this.treeList.size() + 2*this.num_of_marked;
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
        for (int i= 0 ; i<k ; i++){
            HeapNode min = H.findMin();
            arr[i] = min.getKey();
            H.deleteMin();
            for (HeapNode child : min.getChildren()){
                aux_heap.insert(child.getKey());
            }
        }
        return arr; // should be replaced by student code
    }


    /**
     * a function that links two trees of the same rank
     * the tree with the smaller root will be the child of the other tree
     * @pre node1 and node2 are trees of the same rank
     * @post node1 is the parent of node2 or vice versa, new tree has rank+1
     * @param node1 the root of the first tree
     * @param node2 the root of the second tree
     * @return the root of the new tree
     */
    protected HeapNode link(HeapNode node1, HeapNode node2){
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
     * @pre the buckets array is empty
     * @post the buckets array contains the trees in the heap, after linking them
     * @param node the root of the tree to be added to the buckets array
     * @param ranks an array that contains the number of trees with each rank
     * @param buckets an array that contains the trees in the heap
     */

    protected void fillBuckets(HeapNode node, int[] ranks, HeapNode[] buckets){
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
     * @pre the heap is not empty
     * @post the heap contains only trees of different ranks
     */
    protected void consolidate(){
        int highest_rank = (int) (Math.log(this.num_of_nodes) / Math.log(2) +1);
        int[] ranks = new int[highest_rank];
        HeapNode[] buckets = new HeapNode[highest_rank];
        HeapNode curr_min = this.treeList.getFirst();
        for (HeapNode node : this.treeList){
            curr_min = node.getKey() < curr_min.getKey() ? node : curr_min;
            fillBuckets(node, ranks, buckets);
        }
        this.min = curr_min;
        this.treeList.clear();
        for (HeapNode bucket : buckets) {
            if (bucket != null) {
                this.treeList.addLast(bucket);
            }
        }
    }

    protected void cascadingCuts(HeapNode node){
        HeapNode parent = node.getParent();
        if(parent != null){ // node is not the root
            if(!node.isMarked()){ // node is not marked
                node.setMarked(true);
                this.num_of_marked++;
            }
            else{
                parent.removeChild(node); // cut node from parent
                this.num_of_marked--;
                num_of_cuts++;
                this.treeList.addFirst(node);
                node.setParent(null);
                node.setMarked(false);
                cascadingCuts(parent); // cut parent recursively
            }
        }
    }

    protected void printHeap(){
        for (HeapNode node : this.treeList){
            new TraditionalTreePrinter().print(new BorderTreeNodeDecorator(node));
        }
    }









    //------------------------------------HeapNode------------------------------------//

   /**
    * public class HeapNode
    *
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file.
    *
    */

    public static class HeapNode implements TreeNode {

    	public int key;
        private HeapNode parent;
        private LinkedList<HeapNode> children;
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
            this.children = new LinkedList<>();

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
        public LinkedList<HeapNode> getChildren() {
            return children;
        }

       /**
        * a functions that sets the children of the node to a new LinkedList
        * @param children a new LinkedList of children
        */
        public void setChildren(LinkedList<HeapNode> children) {
            this.children = children;
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
        * @param child the child to be added
        */
       public void addChild(HeapNode child) {
            this.children.addFirst(child);
        }

        /**
         * a function which removes a child from the node
         * @param child the child to be removed
         */
        public void removeChild(HeapNode child) {
            this.children.remove(child);
        }

       /**
        * a function which severs the connection between the node and its parent
        * @post the node has no parent
        * @return the parent of the node
        */
       public HeapNode removeParent() {
            HeapNode parent = this.parent;
            this.parent = null;
            return parent;
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
        * Gets the content of this node.
        */
       @Override
       public ConsoleText content() {
              String s = " " + this.getKey() + " ";
                return  ConsoleText.of(s);
       }

       /**
        * Gets the list of child nodes of this node.
        */
       @Override
       public List<TreeNode> children() {
           return new LinkedList<>(this.getChildren());
       }

   }
}


