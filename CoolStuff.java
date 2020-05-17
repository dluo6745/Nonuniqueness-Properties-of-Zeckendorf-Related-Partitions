import java.util.ArrayList;

public class CoolStuff {
    static int[] seq;

    public static void main(String args[]) {
        int n = 0;
        int x0 = Integer.parseInt(args[0]);
        int x1 = Integer.parseInt(args[1]);

        int y = Integer.parseInt(args[2]);

        seq = enumerateSeq(x0, x1, y);
        int negT = 0;
        for (int i = 0; i <= seq.length; i++) {
            if (seq[i] < 0)
                negT -= seq[i];
        }
        for (int i = 0; i < y; i++) {
            CoolerStuff hi = new CoolerStuff(seq, i, negT);
            hi.decompose(seq.length+1, i);
            ArrayList<String> result = hi.recompose(seq.length, 0);
            if(result.size()>1){
                n = n+1;
            }
            System.out.println("Decomposing " + i + ": " + result.size() + " way(s)");

            for (String s : result) {
                System.out.print(s);
            }
            System.out.println("");
        }
        System.out.println(n);
    }



    private static int[] enumerateSeq(int x0, int x1, int y) {
        ArrayList<Integer> seq = new ArrayList<>();
        seq.add(x0);
        seq.add(x1);

        int z = x0 + x1;
        while (z <= y) {
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

    private static class CoolerStuff {
        boolean [][]donezoed;
        int[] seq;
        int y, negT;

        public CoolerStuff(int[] seq, int y, int negT) {
            this.seq = seq;
            this.y = y;
            this.negT = negT;

            donezoed = new boolean[y+1+negT][seq.length];
        }

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
                    //System.out.println(i + " " + negT + " " + j);
                    donezoed[i+negT][j] = true;
                    resT = true;
                }
            }
            return resT;
        }

        private ArrayList<String> recompose(int l, int sum) {
            ArrayList<String> result = new ArrayList<>();
            if (sum == y) {
                result.add("\n");
            }

            for (int j = 0; j < l; j++) {
                if (donezoed[sum+negT][j]) {
                    ArrayList<String> hi = recompose(l, sum+seq[j]);
                    for (int i = 0; i < hi.size(); i++) {
                        hi.set(i, j + " " + hi.get(i));
                    }
                    result.addAll(hi);
                }
            }
            return result;
        }
    }
}