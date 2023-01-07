import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;




public class basicTest {
    @Test
    public void test() {
        FibonacciHeap heap = new FibonacciHeap();
        heap.insert(15);
        heap.insert(10);
        heap.insert(20);
        heap.insert(5);

    }

    @Test
    public void testInsert() {
        int key = 15;
        FibonacciHeap heap = new FibonacciHeap();
        heap.insert(key);
        assertEquals(heap.size(), 1);
        assertEquals(heap.findMin().getKey(), key);
    }

    @Test
    public void testLink() {
        FibonacciHeap heap = new FibonacciHeap();
        FibonacciHeap.HeapNode node1 = heap.insert(10);
        FibonacciHeap.HeapNode node2 = heap.insert(15);
        heap.link(node1, node2);
        assertEquals(2, heap.size());
        assertEquals(10,heap.findMin().getKey());
        assertEquals(1, node1.getRank());
        assertEquals(node2,node1.getChildren().get(0));
        assertEquals(node1,node2.getParent());
    }

    @Test
    public void testConsolidate() {
        FibonacciHeap heap = new FibonacciHeap();
        FibonacciHeap.HeapNode node1 = heap.insert(10);
        FibonacciHeap.HeapNode node2 = heap.insert(15);
        FibonacciHeap.HeapNode node3 = heap.insert(20);
        FibonacciHeap.HeapNode node4 = heap.insert(25);
        heap.consolidate();
        System.out.println("First heap");
        heap.printHeap();
        assertEquals(4, heap.size());
        assertEquals(10, heap.findMin().getKey());
        assertEquals(2, node1.getRank());
        assertEquals(0, node2.getRank());
        assertEquals(1, node3.getRank());
        assertEquals(0, node4.getRank());
        assertEquals(node3, node1.getChildren().get(0));
        assertEquals(node2, node1.getChildren().get(1));
        assertEquals(node4, node3.getChildren().get(0));
        assertEquals(node1, node2.getParent());
        assertEquals(node1, node3.getParent());
        assertEquals(node3, node4.getParent());

        FibonacciHeap heap2 = new FibonacciHeap();
        FibonacciHeap.HeapNode node5 = heap2.insert(40);
        FibonacciHeap.HeapNode node6 = heap2.insert(50);
        FibonacciHeap.HeapNode node7 = heap2.insert(30);
        FibonacciHeap.HeapNode node8 = heap2.insert(60);
        FibonacciHeap.HeapNode node9 = heap2.insert(70);
        FibonacciHeap.HeapNode node10 = heap2.insert(80);
        FibonacciHeap.HeapNode node11 = heap2.insert(90);
        FibonacciHeap.HeapNode node12 = heap2.insert(100);
        heap2.consolidate();
        System.out.println("Second heap");
        heap2.printHeap();
        assertEquals(8, heap2.size());
        assertEquals(30, heap2.findMin().getKey());
        assertEquals(3, node7.getRank());
        assertEquals(1, node5.getRank());
        assertEquals(0, node6.getRank());
        assertEquals(0, node8.getRank());
        assertEquals(2, node9.getRank());
        assertEquals(0, node10.getRank());
        assertEquals(1, node11.getRank());
        assertEquals(0, node12.getRank());
        assertEquals(node9, node7.getChildren().get(0));
        assertEquals(node5, node7.getChildren().get(1));
        assertEquals(node8, node7.getChildren().get(2));
        assertEquals(node6, node5.getChildren().get(0));
        assertEquals(node11, node9.getChildren().get(0));
        assertEquals(node10, node9.getChildren().get(1));
        assertTrue(node6.getChildren().isEmpty());
        assertEquals(node7, node5.getParent());
        assertEquals(node7, node9.getParent());
        assertEquals(node7, node8.getParent());
        assertEquals(node5, node6.getParent());
        assertEquals(node9, node10.getParent());
        assertEquals(node9, node11.getParent());
        assertEquals(node11, node12.getParent());

        FibonacciHeap heap3 = new FibonacciHeap();
        FibonacciHeap.HeapNode n200 = heap3.insert(200);
        FibonacciHeap.HeapNode n300 = heap3.insert(300);
        FibonacciHeap.HeapNode n400 = heap3.insert(400);
        FibonacciHeap.HeapNode n500 = heap3.insert(500);
        FibonacciHeap.HeapNode n600 = heap3.insert(600);
        FibonacciHeap.HeapNode n700 = heap3.insert(700);
        FibonacciHeap.HeapNode n800 = heap3.insert(800);

        heap3.consolidate();
        System.out.println("Third heap");
        heap3.printHeap();
    }

    @Test
    public void testMeld(){
        FibonacciHeap heap = new FibonacciHeap();
        FibonacciHeap.HeapNode node1 = heap.insert(10);
        FibonacciHeap.HeapNode node2 = heap.insert(15);
        FibonacciHeap.HeapNode node3 = heap.insert(20);
        FibonacciHeap.HeapNode node4 = heap.insert(25);
        FibonacciHeap heap2 = new FibonacciHeap();
        FibonacciHeap.HeapNode node5 = heap2.insert(40);
        FibonacciHeap.HeapNode node6 = heap2.insert(50);
        FibonacciHeap.HeapNode node7 = heap2.insert(30);
        FibonacciHeap.HeapNode node8 = heap2.insert(60);
        FibonacciHeap.HeapNode node9 = heap2.insert(70);
        FibonacciHeap.HeapNode node10 = heap2.insert(80);
        FibonacciHeap.HeapNode node11 = heap2.insert(90);
        FibonacciHeap.HeapNode node12 = heap2.insert(100);
        heap.meld(heap2);
        System.out.println("Melded heap");
        heap.printHeap();
        assertEquals(12, heap.size());
        assertEquals(10, heap.findMin().getKey());

    }
}
