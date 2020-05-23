import java.util.ArrayList;

public class ZP{
    static int[] seq;

    public static void main(String args[]) {
        int count = 0;
        int x0 = Integer.parseInt(args[0]);
        int x1 = Integer.parseInt(args[1]);

        int N = Integer.parseInt(args[2]);

        seq = enumerateSeq(x0, x1, N);
        int negT = 0;
        for (int i = 0; i < seq.length; i++) {
            if (seq[i] < 0)
                negT -= seq[i];
        }
        for (int i = 1; i <= N; i++) {
            NonconsecutivePartition partial = new NonconsecutivePartition(seq, i, negT);
            partial.decompose(seq.length+1, i);
            ArrayList<String> result = partial.recompose(0);
            if(result.size()>1){
                count += 1;
            }
            System.out.println("Partitioning " + i + ": " + result.size() + " way(s)");

            for (String s : result) {
                System.out.print(s);
            }
            System.out.println("");
        }
        System.out.println("Number of natural numbers that do not have unique nonconsecutive partitions: " + count);
    }

    /** Enumerates the terms of the second-order linear recurrence sequence defined by x0 and x1
     *  up to the integer N.
     */

    private static int[] enumerateSeq(int x0, int x1, int N) {
        ArrayList<Integer> seq = new ArrayList<>();
        seq.add(x0);
        seq.add(x1);

        int z = x0 + x1;
        while (z <= N) {
            seq.add(z);
            x0 = x1;
            x1 = z;
            z = x0 + x1;
        }

        int[] intArray = new int[seq.size()];
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = seq.get(i);
        }
        return intArray;
    }

    /**
     * Static helper class that is used to define the nonconsecutive partition of the integer
     * N passed in the sequence. We also pass in the lowest negative number
     * in the sequence for array indexing purposes.
     */

    private static class NonconsecutivePartition{

    /** Memoization table that signifies true for a successful nonconsecutive partition,
     *  false otherwise. It's parameterized by two integers:
     *      - The sum of a partial successful nonconsecutive partition up to an index.
     *      - The next smallest integer index in our nonconsecutive partition.
     */

        boolean [][] memoPartition;
        int[] seq;
        int N, negT;

        public NonconsecutivePartition(int[] seq, int N, int negT) {
            this.seq = seq;
            this.N = N;
            this.negT = negT;

            memoPartition = new boolean[N+1+negT][seq.length];
        }

         /**
         * We do a partial nonconsecutive partition based off of the
         * last index in the nonconsecutive partition by subtracting all possible
         * indices (0, 1, ..., maxInd -2) from our remainder and using a recursive
         * call.
         * @param maxInd the last index used.
         * @param rem the remaining sum to the nonconsecutive partition.
         * @return true iff a nonconsecutive partition is possible, false otherwise.
         */

        private boolean decompose(int maxInd, int rem) {
            if (rem == 0) {
                return true;
            }

            boolean res, resT = false;
            int i;
            for(int j = 0; j <= maxInd-2; j++) {
                i = rem-seq[j];
                res = decompose(j, i);
                if (res) {
                    memoPartition[i+negT][j] = true;
                    resT = true;
                }
            }
            return resT;
        }

         /**
         * Returns any successful nonconsecutive partition based off the partial sum
         * passed through. The sum signifies the sum of the sequence numbers
         * starting from the lowest number and returns a list of space separated nonconsecutive
         * partitions.
         * @param sum partial sum
         */

        private ArrayList<String> recompose(int sum) {
            ArrayList<String> result = new ArrayList<>();
            if (sum == N) {
                result.add("\n");
            }

            for (int j = 0; j < seq.length; j++) {

            /**
            * If the sum that we have is successful in the memoization
            * table, we add the sequence number and recursively call
            * while also adding the index to all strings returned in the list.
            */

                if (memoPartition[sum+negT][j]) {
                    ArrayList<String> partial = recompose(sum+seq[j]);
                    for (int i = 0; i < partial.size(); i++) {
                        partial.set(i, j + " " + partial.get(i));
                    }
                    result.addAll(partial);
                }
            }
            return result;
        }
    }
}
