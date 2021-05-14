package org.testcontainers.couchbase;

import com.couchbase.client.java.document.RawJsonDocument;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.view.DefaultView;
import com.couchbase.client.java.view.DesignDocument;
import com.couchbase.client.java.view.View;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.Description;
import org.testcontainers.containers.FailureDetectingExternalResource;
import org.testcontainers.testsupport.Flaky;
import org.testcontainers.testsupport.FlakyTestJUnit4RetryRule;

import java.util.List;

public abstract class BaseCouchbaseContainerTest extends AbstractCouchbaseTest {

    private static final String VIEW_NAME = "testview";
    private static final String VIEW_FUNCTION = "function (doc, meta) {\n" +
        "  emit(meta.id, null);\n" +
        "}";


    private static final String ID = "toto";

    private static final String DOCUMENT = "{\"name\":\"toto\"}";

    @Rule
    public RuleChain ruleChain = RuleChain.emptyRuleChain()
        .around(new FlakyTestJUnit4RetryRule())
        .around(new FailureDetectingExternalResource() {
            @Override
            protected void starting(Description description) {
                getCouchbaseContainer().start();
            }

            @Override
            protected void failed(Throwable e, Description description) {
                getCouchbaseContainer().stop();
            }
        });

    @Test
    @Flaky(githubIssueUrl = "https://github.com/testcontainers/testcontainers-java/issues/1453", reviewDate = "2020-03-01")
    public void shouldInsertDocument() {
        RawJsonDocument expected = RawJsonDocument.create(ID, DOCUMENT);
        getBucket().upsert(expected);
        RawJsonDocument result = getBucket().get(ID, RawJsonDocument.class);
        Assert.assertEquals(expected.content(), result.content());
    }

    @Test
    @Flaky(githubIssueUrl = "https://github.com/testcontainers/testcontainers-java/issues/1453", reviewDate = "2020-03-01")
    public void shouldExecuteN1ql() {
        getBucket().query(N1qlQuery.simple("INSERT INTO " + TEST_BUCKET + " (KEY, VALUE) VALUES ('" + ID + "', " + DOCUMENT + ")"));

        N1qlQueryResult query = getBucket().query(N1qlQuery.simple("SELECT * FROM " + TEST_BUCKET + " USE KEYS '" + ID + "'"));
        Assert.assertTrue(query.parseSuccess());
        Assert.assertTrue(query.finalSuccess());
        List<N1qlQueryRow> n1qlQueryRows = query.allRows();
        Assert.assertEquals(1, n1qlQueryRows.size());
        Assert.assertEquals(DOCUMENT, n1qlQueryRows.get(0).value().get(TEST_BUCKET).toString());
    }

    @Test
    @Flaky(githubIssueUrl = "https://github.com/testcontainers/testcontainers-java/issues/1453", reviewDate = "2020-03-01")
    public void shouldCreateView() {
        View view = DefaultView.create(VIEW_NAME, VIEW_FUNCTION);
        DesignDocument document = DesignDocument.create(VIEW_NAME, Lists.newArrayList(view));
        getBucket().bucketManager().insertDesignDocument(document);
        DesignDocument result = getBucket().bucketManager().getDesignDocument(VIEW_NAME);
        Assert.assertEquals(1, result.views().size());
        View resultView = result.views().get(0);
        Assert.assertEquals(VIEW_NAME, resultView.name());
    }
}
