package xyz.drhelius.load;

public class BusyLoad extends Thread {

    public static void createBlockingLoad(double load, long duration) {

        new BusyThread("Blocking Thread", load, duration).run();
    }

    public static void createNonBlockingLoad(int numCore, int numThreadsPerCore, double load, long duration) {

        for (int thread = 0; thread < numCore * numThreadsPerCore; thread++) {
            new BusyThread("Thread" + thread, load, duration).start();
        }
    }

    private static class BusyThread extends Thread {
        private double load;
        private long duration;

        public BusyThread(String name, double load, long duration) {
            super(name);
            this.load = load;
            this.duration = duration;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            try {
                // Loop for the given duration
                while (System.currentTimeMillis() - startTime < duration) {
                    // Every 100ms, sleep for the percentage of unladen time
                    if (System.currentTimeMillis() % 100 == 0) {
                        Thread.sleep((long) Math.floor((1 - load) * 100));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
