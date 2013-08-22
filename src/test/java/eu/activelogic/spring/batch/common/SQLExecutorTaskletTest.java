package eu.activelogic.spring.batch.common;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;


public class SQLExecutorTaskletTest {

	private static final String QUERY = "QUERY";
	
	SQLExecutorTasklet tasklet;
	
	Session session;
	SessionFactory sf;
	SQLQuery query;
	
	@Before
	public void setUp() throws Exception {
		session = mock(Session.class);
		sf = mock(SessionFactory.class);
		query = mock(SQLQuery.class);
		
		
		tasklet = new SQLExecutorTasklet();
		tasklet.setSessionFactory(sf);
		tasklet.setSelect(false);
		tasklet.setSqlQuery(QUERY);
	}

	@Test
	public final void testGetSession() {
		when(sf.getCurrentSession()).thenReturn(session);

		Session sess = tasklet.getSession();

		assertSame(sess, session);
	}

	@Test
	public final void testBuildQuery() {
		when(sf.getCurrentSession()).thenReturn(session);
		when(session.createSQLQuery(QUERY)).thenReturn(this.query);
		

		Query q = tasklet.buildQuery();

		assertSame(q, this.query);
	}

	@Test
	public final void testExecute() throws Exception {
		when(sf.getCurrentSession()).thenReturn(session);
		when(session.createSQLQuery(QUERY)).thenReturn(this.query);
		
		when(query.executeUpdate()).thenReturn(10);
		

		tasklet.execute(null, null);
		verify(query).executeUpdate();
	}
	
	@Test
	public final void testExecuteSelect() throws Exception {
		when(sf.getCurrentSession()).thenReturn(session);
		when(session.createSQLQuery(QUERY)).thenReturn(this.query);
		when(query.list()).thenReturn(Arrays.asList("One","Two","Three"));
		tasklet.setSelect(true);
		
		tasklet.execute(null, null);
		verify(query).list();
	}
	
	@Test(expected=RuntimeException.class)
	public final void testExecuteException() throws Exception {
		when(sf.getCurrentSession()).thenThrow(new RuntimeException());

		tasklet.execute(null, null);
	}
	
	@Test(expected=RuntimeException.class)
	public final void testExecuteQueryException() throws Exception {
		when(sf.getCurrentSession()).thenReturn(session);
		when(session.createSQLQuery(QUERY)).thenReturn(this.query);
		when(query.executeUpdate()).thenThrow(new RuntimeException());
		

		tasklet.execute(null, null);
	}
	

}
