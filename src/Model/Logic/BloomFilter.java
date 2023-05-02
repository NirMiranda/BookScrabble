package Model.Logic;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.lang.Object;
import java.lang.Number;
import java.math.BigInteger;
import java.lang.Math;

import static java.util.Objects.hash;

public class BloomFilter {
    String []algs=null;
    MessageDigest md;
    BitSet bit;

    public BloomFilter(int number, String...algs) {
        this.algs = algs.clone();
        this.bit = new BitSet(number);
    }
    public int GetIndex(String s , String wordFunc) {
        try {
            md = MessageDigest.getInstance(wordFunc);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] bts = md.digest(s.getBytes());
        BigInteger num = new BigInteger(bts);
        int numAbs = Math.abs(num.intValue());
        int index = numAbs % this.bit.size();
        return index;
    }
    public void add(String s)
    {
        for (int i=0;i< algs.length;i++){
            int index=GetIndex(s,algs[i]);
            bit.set(index);
        }
    }
    public boolean contains(String s){
        for(int i=0;i<algs.length;i++){
            int index=GetIndex(s,algs[i]);
            if(bit.get(index)==false) return false;
        }
        return true;
}
    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder();
        String s;
        for (int i = 0; i <this.bit.length(); i++) {
            if (this.bit.get(i) == true)
                temp.insert(i,1);
            else
                temp.insert(i,0);
        }
        s= temp.toString();
        return s;
    }
}
