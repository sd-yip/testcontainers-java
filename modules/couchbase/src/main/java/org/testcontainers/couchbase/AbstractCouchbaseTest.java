package org.testcontainers.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.bucket.BucketType;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.DefaultBucketSettings;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.consistency.ScanConsistency;
import org.junit.After;

import java.util.concurrent.TimeUnit;

/**
 * Basic class that can be used for couchbase tests. It will clear the database after every test.
 */
public abstract class AbstractCouchbaseTest {

    public static final String TEST_BUCKET = "test";

    public static final String DEFAULT_PASSWORD = "password";

    Bucket bucket;

    @After
    public void clear() {
        try {
            if (getCouchbaseContainer().isIndex() && getCouchbaseContainer().isQuery() && getCouchbaseContainer().isPrimaryIndex()) {
                getBucket().query(
                    N1qlQuery.simple(String.format("DELETE FROM `%s`", getBucket().name()),
                        N1qlParams.build().consistency(ScanConsistency.STATEMENT_PLUS)));
            } else {
                getBucket().bucketManager().flush();
            }
        } finally {
            bucket.close(60, TimeUnit.SECONDS);
            bucket = null;
        }
    }

    protected abstract CouchbaseContainer getCouchbaseContainer();

    protected static CouchbaseContainer initCouchbaseContainer(String imageName) {
        CouchbaseContainer couchbaseContainer = (imageName == null) ? new CouchbaseContainer() : new CouchbaseContainer(imageName);
        couchbaseContainer.withNewBucket(getDefaultBucketSettings());
        return couchbaseContainer;
    }

    protected static BucketSettings getDefaultBucketSettings() {
        return DefaultBucketSettings.builder()
            .enableFlush(true)
            .name(TEST_BUCKET)
            .password(DEFAULT_PASSWORD)
            .quota(100)
            .replicas(0)
            .type(BucketType.COUCHBASE)
            .build();
    }

    protected synchronized Bucket getBucket() {
        if (bucket == null) {
            bucket = openBucket(TEST_BUCKET, DEFAULT_PASSWORD);
        }
        return bucket;
    }

    private Bucket openBucket(String bucketName, String password) {
        CouchbaseCluster cluster = getCouchbaseContainer().getCouchbaseCluster();
        return cluster.openBucket(bucketName, password);
    }
}
