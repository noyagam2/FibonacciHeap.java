import java.util.LinkedList;


/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
    private static int  num_of_links=0;
    private static int  num_of_cuts=0;
    private int max_rank=0;
    private HeapNode min;
    private int num_of_nodes;
    private LinkedList<HeapNode> treeList;

    public FibonacciHeap() {
        min = null;
        num_of_nodes = 0;
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
     	return; // should be replaced by student code

    }

   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    */
    public HeapNode findMin()
    {
    	return new HeapNode(678);// should be replaced by student code
    }

   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	  return; // should be replaced by student code
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
    	int[] arr = new int[100];
        return arr; //	 to be replaced by student code
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
    	return; // should be replaced by student code
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {
    	return; // should be replaced by student code
    }

   /**
    * public int nonMarked()
    *
    * This function returns the current number of non-marked items in the heap
    */
    public int nonMarked()
    {
        return -232; // should be replaced by student code
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
        return -234; // should be replaced by student code
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
    	return -345; // should be replaced by student code
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
    	return -456; // should be replaced by student code
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
        int[] arr = new int[100];
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
    private HeapNode link(HeapNode node1, HeapNode node2){
        boolean isNode1Min = (node1.getKey() < node2.getKey());
        HeapNode parent = isNode1Min ? node1 : node2;
        HeapNode child = isNode1Min ? node2 : node1;
        parent.addChild(child);
        parent.setRank(parent.getRank() + 1);
        child.setParent(parent);
        num_of_links++;
        max_rank = parent.getRank() > this.max_rank ? parent.getRank() : this.max_rank;
        return parent;
    }

    /**
     * a function that consolidates the trees in the heap
     * @post the heap contains only trees of different ranks
     */
    private void consolidate(){

    }

    /**
     * a function that consolidates the trees in the heap
     * @post the heap contains only trees of different ranks
     */





    //------------------------------------HeapNode------------------------------------//

   /**
    * public class HeapNode
    *
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file.
    *
    */

    public static class HeapNode{

    	public int key;
        private HeapNode parent;
        private LinkedList<HeapNode> children;
        private boolean mark;
        private int rank;


        public HeapNode(int key) {
            this.key = key;
            this.parent = null;
            this.mark = false;
            this.children = new LinkedList<>();

        }

        public int getKey() {
            return this.key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public HeapNode getParent() {
            return parent;
        }

        public void setParent(HeapNode parent) {
            this.parent = parent;
        }

        public LinkedList<HeapNode> getChildren() {
            return children;
        }

        public void setChildren(LinkedList<HeapNode> children) {
            this.children = children;
        }

        public boolean isMarked() {
            return mark;
        }

        public void setMark(boolean mark) {
            this.mark = mark;
        }

        public void addChild(HeapNode child) {
            this.children.addFirst(child);
        }

        public void removeChild(HeapNode child) {
            this.children.remove(child);
        }

        public void removeParent() {
            this.parent = null;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }


    }
}


