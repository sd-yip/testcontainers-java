package org.testcontainers.couchbase;

import org.junit.Test;
import org.testcontainers.utility.DockerImageName;

public class CouchbaseContainerTest {

    private static final DockerImageName COUCHBASE_IMAGE = DockerImageName.parse("couchbase/server:6.5.1");

    @Test
    public void shouldStopWithoutThrowingException() {
        CouchbaseContainer couchbaseContainer = new CouchbaseContainer(COUCHBASE_IMAGE);
        couchbaseContainer.start();
        couchbaseContainer.stop();
    }
}
