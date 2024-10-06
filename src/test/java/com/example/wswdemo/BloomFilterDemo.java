package com.example.wswdemo;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.bloomfilter.BitSetBloomFilter;
import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Test;

import java.util.BitSet;

public class BloomFilterDemo {

    /**
     * 位数组的大小
     */
    private static final int DEFAULT_SIZE = 2 << 24;

    /**
     * 通过这个数组可以创建 6 个不同的哈希函数
     */
    private static final int[] SEEDS = new int[]{3, 13, 46, 71, 91, 134};


    private BitSet bits = new BitSet(DEFAULT_SIZE);

    /**
     * 存放包含 hash 函数的类的数组
     */
    private SimpleHash[] func = new SimpleHash[SEEDS.length];

    /**
     * 初始化多个包含 hash 函数的类的数组，每个类中的 hash 函数都不一样
     */
    public BloomFilterDemo() {
        // 初始化多个不同的 Hash 函数
        for (int i = 0; i < SEEDS.length; i++) {
            func[i] = new SimpleHash(DEFAULT_SIZE, SEEDS[i]);
        }
    }


    /**
     * 添加元素到位数组
     */
    public void add(Object value) {
        for (SimpleHash f : func) {
            bits.set(f.hash(value), true);
        }
    }

    /**
     * 判断指定元素是否存在于位数组
     */
    public boolean contains(Object value) {
        boolean ret = true;
        for (SimpleHash f : func) {
            ret = ret && bits.get(f.hash(value));
        }
        return ret;
    }

    /**
     * 静态内部类。用于 hash 操作！
     */
    public static class SimpleHash {

        private int cap;
        private int seed;

        public SimpleHash(int cap, int seed) {
            this.cap = cap;
            this.seed = seed;
        }

        /**
         * 计算 hash 值
         */
        public int hash(Object value) {
            int h;
            return (value == null) ? 0 : Math.abs((cap - 1) & seed * ((h = value.hashCode()) ^ (h >>> 16)));
        }

    }

    @Test
    void testBloom() {
        String value1 = "测试";
        String value2 = "测";
        BloomFilterDemo filter = new BloomFilterDemo();
        System.out.println(filter.contains(value1));
        System.out.println(filter.contains(value2));
        filter.add(value1);
        filter.add(value2);
        System.out.println(filter.contains(value1));
        System.out.println(filter.contains(value2));
    }

    @Test
    void testHuToolBloom() {
        //BitMapBloomFilter bitMapBloomFilter = new BitMapBloomFilter(10);
        BitSetBloomFilter bitMapBloomFilter = new BitSetBloomFilter(10000, 20000, 5);
        System.out.println(bitMapBloomFilter.contains("123"));
        System.out.println(bitMapBloomFilter.contains("456"));

        bitMapBloomFilter.add("123");
        bitMapBloomFilter.add("456");

        System.out.println(bitMapBloomFilter.contains("123"));
        System.out.println(bitMapBloomFilter.contains("456"));
    }

    @Test
    void idtest() {
        long id = IdUtil.getSnowflake(1, 1).nextId();
        System.out.println(id);
    }
}


