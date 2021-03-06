package com.adiaz.daos;

import com.adiaz.entities.Competition;
import com.adiaz.entities.Court;
import com.adiaz.entities.Match;
import com.adiaz.entities.Team;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by toni on 11/07/2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:web/WEB-INF/applicationContext-testing.xml")
@WebAppConfiguration("file:web")
public class MatchesDAOImplTest {

	private static final String ATLETICO_MADRID = "ATLETICO_MADRID";
	public static final String LEGANES = "LEGANES";
	public static final String COPA_PRIMAVERA = "COPA PRIMAVERA";
	public static final String PISTA_01 = "PISTA 01";
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	@Autowired
	MatchesDAO matchesDAO;

	@Autowired
	CompetitionsDAO competitionsDAO;

	@Autowired
	TeamDAO teamDAO;

	@Autowired
	CourtDAO courtDAO;

	private Ref<Competition> competitionRef;
	private Ref<Team> atleticoRef;
	private Ref<Team> leganesRef;
	private Ref<Court> courtRef;

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		ObjectifyService.register(Match.class);
		ObjectifyService.register(Competition.class);
		ObjectifyService.register(Team.class);
		ObjectifyService.register(Court.class);

		Team team = new Team();
		team.setName(ATLETICO_MADRID);
		atleticoRef = Ref.create(teamDAO.create(team));

		team = new Team();
		team.setName(LEGANES);
		leganesRef = Ref.create(teamDAO.create(team));

		Competition competition = new Competition();
		competition.setName(COPA_PRIMAVERA);
		Key<Competition> key = competitionsDAO.create(competition);
		competitionRef = Ref.create(key);

		Court court = new Court();
		court.setName(PISTA_01);
		courtRef = courtDAO.createReturnRef(court);
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	@Test
	public void create() throws Exception {
		createMatch();
		assertEquals(1, matchesDAO.findAll().size());
	}

	@Test
	public void update() throws Exception {
		Key<Match> key = createMatch();
		Match match = Ref.create(key).getValue();
		match.setTeamLocalRef(leganesRef);
		matchesDAO.update(match);
		assertEquals(leganesRef, matchesDAO.findById(match.getId()).getTeamLocalRef());
		assertEquals(LEGANES, matchesDAO.findById(match.getId()).getTeamLocalRef().get().getName());

	}

	@Test
	public void remove() throws Exception {
		Key<Match> key = createMatch();
		Match match = Ref.create(key).getValue();
		matchesDAO.remove(match);
		assertEquals(0, matchesDAO.findAll().size());
	}

	@Test
	public void findByCompetition() throws Exception {
		createMatch();
		createMatch();
		assertEquals(2, matchesDAO.findByCompetition(competitionRef.getKey().getId()).size());
	}

	@Test
	public void findAll() throws Exception {
		createMatch();
		createMatch();
		assertEquals(2, matchesDAO.findAll().size());
	}

	@Test
	public void findById() throws Exception {
		Key<Match> key = createMatch();
		Match match = Ref.create(key).getValue();
		assertEquals(match.getId(), matchesDAO.findById(match.getId()).getId());
	}

	@Test
	public void findByCourt() throws Exception {
		assertEquals(0, matchesDAO.findByCourt(courtRef.get().getId()).size());
		createMatch();
		createMatch();
		assertEquals(2, matchesDAO.findByCourt(courtRef.get().getId()).size());
	}

	@Test
	public void removeByCompetition() throws Exception {
		createMatch();
		createMatch();
		createMatch();
		List<Match> matchList = matchesDAO.findByCompetition(competitionRef.get().getId());
		assertEquals(3, matchList.size());
		matchesDAO.remove(matchList);
		matchList = matchesDAO.findByCompetition(competitionRef.get().getId());
		assertEquals(0, matchList.size());
	}

	private Key<Match> createMatch() throws Exception {
		Match match = new Match();
		match.setTeamLocalRef(atleticoRef);
		match.setCompetitionRef(competitionRef);
		match.setWorkingCopy(false);
		match.setCourtRef(courtRef);
		return matchesDAO.create(match);
	}
}