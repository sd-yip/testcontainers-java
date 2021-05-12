package org.testcontainers.couchbase5;

import org.junit.Rule;

public class Couchbase4_6Test extends BaseCouchbaseContainerTest {

    @Rule
    public CouchbaseContainer container = initCouchbaseContainer("couchbase:4.6.5");

    @Override
    public CouchbaseContainer getCouchbaseContainer() {
        return container;
    }
}
