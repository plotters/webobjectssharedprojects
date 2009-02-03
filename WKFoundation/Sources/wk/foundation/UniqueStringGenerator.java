//
//  UniqueStringGenerator.java
//

package wk.foundation;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.Vector;
public class UniqueStringGenerator {
    public static final String[] ALPHA_NUM =
            {"A","B","C","D","E","F","G","H","J","K","L","M","N","P",
            "Q","R","S","T","U","V","W","X","Y","Z",
            "1","2","3","4","5","6","7","8","9"};
    private List _parts = null;
    private List _buckets = null;
    private int _length;
    private BigInteger _range = null;
    public static final int DEFAULT_LENGTH = 25;

    private static UniqueStringGenerator _sharedInstance = new UniqueStringGenerator( UniqueStringGenerator.ALPHA_NUM,
                                                                               DEFAULT_LENGTH,
                                                                               97 );


    public UniqueStringGenerator(String[] parts, int length,
                                 int numberOfBuckets) {
        _length = length;
        // mixing the base set to increase entropy
        _parts = new Vector();
        List all = new Vector();
        for (int i = 0;i < parts.length;i++) {
            all.add(parts[i]);
        }
        while (all.size()>0) {
            int p = (int)(((double)all.size())*Math.random());
            _parts.add(all.get(p));
            all.remove(p);
        }
        // dividing all possible numbers into given number of buckets
        BigInteger numParts = BigInteger.valueOf(parts.length);
        _range = BigInteger.ONE;
        for (int i = 0;i<_length;i++) {
            _range = _range.multiply(numParts);
        }
        TreeSet starts = new TreeSet();
        Random rand = new Random();
        while (starts.size()<numberOfBuckets-1) {
            BigInteger bi = new BigInteger(_range.bitLength(), rand);
            if (bi.compareTo(_range)<=0) { // was ><=
                starts.add(bi);
            }
        }
        _buckets = new Vector();
        Bucket last = new Bucket(BigInteger.ZERO);
        _buckets.add(last);
        for (Iterator i = starts.iterator();i.hasNext();) {
            Bucket next = new Bucket((BigInteger)i.next());
            _buckets.add(next);
            last.setTo(next.getStart());
            last = next;
        }
        last.setTo(_range);
    }
    private class Bucket {
        private BigInteger _start;
        private BigInteger _next;
        private BigInteger _to;
        public Bucket(BigInteger start) {
            _start = start;
            _next = start;
        }
        public void setTo(BigInteger to) {
            _to = to;
        }
        public BigInteger getStart() {
            return _start;
        }
        public boolean hasNext() {
            return _next.compareTo(_to)<0;
        }
        public BigInteger next() {
            BigInteger ret = _next;
            _next = _next.add(BigInteger.ONE);
            return ret;
        }
        public String toString() {
            return "["+_start+", "+_next+", "+_to+"]:"+hasNext();
        }
    }

    /** @return the next unique string */
    public String nextUnique() {
        // getting next big integer
        BigInteger left = null;
        while (left==null) {
            Bucket b = (Bucket)
            _buckets.get((int)(Math.random()*_buckets.size()));
            if (b.hasNext()) {
                left = b.next();
            }
        }
        // converting it to a combination
        StringBuffer unique = new StringBuffer();
        BigInteger base = BigInteger.valueOf(_parts.size());
        for (int i = 0;i<_length;i++) {
            BigInteger[] resRem = left.divideAndRemainder(base);
            left = resRem[0];
            unique.append(_parts.get(resRem[1].intValue()));
        }
        return unique.toString();
    }

    /** @return the first n character of the nextUnique string. */
    public String nextUnique( int n ) {
        if ( n < 1 || n > DEFAULT_LENGTH ) {
            throw new IllegalArgumentException( "parameter 'n' must be greater than 0 and less than or equal to " + DEFAULT_LENGTH );
        }
        return nextUnique().substring(0, n);
    }

    public static UniqueStringGenerator sharedInstance() {
        return _sharedInstance;
    }

    public static void main(String[] args) {
        if (args.length!=3) {
            System.out.println("Usage: UniqueStringGenerator "+
                               "[string size] [# buckets] [# prints]");
            return;
        }
        int length = Integer.parseInt(args[0]);
        int numBuckets = Integer.parseInt(args[1]);
        int num = Integer.parseInt(args[2]);
        UniqueStringGenerator usg = new UniqueStringGenerator
            (UniqueStringGenerator.ALPHA_NUM, length, numBuckets);
        for (int i=0;i<num;i++) {
            System.out.println(usg.nextUnique());
        }
    }
}
