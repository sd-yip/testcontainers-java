package org.testcontainers.couchbase5;

import org.junit.Rule;

public class Couchbase5_5Test extends BaseCouchbaseContainerTest {

    @Rule
    public CouchbaseContainer container = initCouchbaseContainer("couchbase:5.5.1");

    @Override
    public CouchbaseContainer getCouchbaseContainer() {
        return container;
    }
}
