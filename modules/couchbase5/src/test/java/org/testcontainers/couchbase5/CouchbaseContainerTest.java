package org.testcontainers.couchbase;

import org.junit.Test;
import org.testcontainers.utility.DockerImageName;

public class CouchbaseContainerTest {

    private static final DockerImageName COUCHBASE_IMAGE_ENTERPRISE =
        DockerImageName.parse("couchbase:enterprise-6.6.2");
    private static final DockerImageName COUCHBASE_IMAGE_COMMUNITY =
        DockerImageName.parse("couchbase:community-6.6.0");

    @Test
    public void shouldStopWithoutThrowingExceptionForEnterpriseContainer() {
        CouchbaseContainer couchbaseContainer = new CouchbaseContainer(COUCHBASE_IMAGE_ENTERPRISE);
        couchbaseContainer.start();
        couchbaseContainer.stop();
    }

    @Test
    public void shouldStopWithoutThrowingExceptionForCommunityContainer() {
        CouchbaseContainer couchbaseContainer = new CouchbaseContainer(COUCHBASE_IMAGE_COMMUNITY)
            .withCommunityMode(true);
        couchbaseContainer.start();
        couchbaseContainer.stop();
    }
}
