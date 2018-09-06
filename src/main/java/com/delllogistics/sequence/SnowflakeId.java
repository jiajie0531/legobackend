package com.delllogistics.sequence;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * Created by xzm on 2018-2-2.
 */
public class SnowflakeId implements IdentifierGenerator {

    private Sequence sequence;

    public SnowflakeId() {
        sequence=new Sequence(0,0);
    }

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        return sequence.nextId();
    }
}
