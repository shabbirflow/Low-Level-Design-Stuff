import java.util.concurrent.CyclicBarrier;

class MatrixProcessor {

    public static void main(String[] args) throws Exception {

        int numThreads = 4;
        CyclicBarrier barrier = new CyclicBarrier(numThreads, () -> {
            // This runs once when ALL threads hit the barrier
            // Perfect place to merge results before next round
            System.out.println("Round complete. Starting next round.");
        });

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            new Thread(() -> {
                for (int round = 0; round < 3; round++) {
//                    processChunk(threadId, round);
                    try {
                        barrier.await(); // wait for all threads to finish this round
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        }
    }
}
