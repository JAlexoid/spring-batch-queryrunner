package eu.activelogic.spring.batch.common;

import org.hibernate.*;



import org.junit.*;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class HQLExecutorTaskletTest {

	private static final String QUERY = "QUERY";
	
	HQLExecutorTasklet tasklet;
	
	Session session;
	SessionFactory sf;
	Query query;
	
	@Before
	public void setUp() throws Exception {
		session = mock(Session.class);
		sf = mock(SessionFactory.class);
		query = mock(Query.class);
		
		
		tasklet = new HQLExecutorTasklet();
		tasklet.setSessionFactory(sf);
	}

	@Test
	public final void testBuildQuery() {
		when(sf.getCurrentSession()).thenReturn(session);
		when(session.createQuery(QUERY)).thenReturn(query);
		tasklet.setNamedQuery(QUERY);
		tasklet.setHqlQuery(QUERY);
		
		Query q = tasklet.buildQuery();
		assertNotNull(q);
	}
	
	@Test
	public final void testBuildHQLQuery() {
		when(sf.getCurrentSession()).thenReturn(session);
		when(session.createQuery(QUERY)).thenReturn(query);
		tasklet.setHqlQuery(QUERY);
		
		Query q = tasklet.buildQuery();
		assertNotNull(q);
	}

	@Test
	public final void testBuildNamedQuery() {
		when(sf.getCurrentSession()).thenReturn(session);
		when(session.getNamedQuery(QUERY)).thenReturn(query);
		tasklet.setNamedQuery(QUERY);
		
		Query q = tasklet.buildQuery();
		assertNotNull(q);
	}

	@Test
	public final void testBuildNamedQueryFail() {
		when(sf.getCurrentSession()).thenReturn(session);
		
		Query q = tasklet.buildQuery();
		assertNull(q);
	}

}
