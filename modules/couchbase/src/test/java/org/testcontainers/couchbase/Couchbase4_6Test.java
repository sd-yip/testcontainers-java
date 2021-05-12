package org.testcontainers.couchbase;

import org.junit.Rule;

public class Couchbase4_6Test extends BaseCouchbaseContainerTest {

    @Rule
    public CouchbaseContainer container = initCouchbaseContainer("couchbase/server:4.6.5");

    @Override
    public CouchbaseContainer getCouchbaseContainer() {
        return container;
    }
}
