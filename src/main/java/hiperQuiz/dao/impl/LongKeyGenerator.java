package hiperQuiz.dao.impl;

import hiperQuiz.dao.KeyGenerator;

import java.util.concurrent.atomic.AtomicLong;

public class LongKeyGenerator implements KeyGenerator<Long> {

    private AtomicLong sequence = new AtomicLong();

    public LongKeyGenerator() {
    }

    public LongKeyGenerator(AtomicLong sequence) {
        this.sequence = sequence;
    }

    @Override
    public Long getNextId() {
        return sequence.incrementAndGet();
    }
}
