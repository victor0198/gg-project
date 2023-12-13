package student.examples.uservice.api.business;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import student.examples.uservice.api.business.game.Game;
import student.examples.uservice.api.business.game.Rock;
import student.examples.uservice.api.business.game.Item;
import student.examples.uservice.api.business.game.Space;
import student.examples.uservice.api.business.service.GameService;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ComputeUserviceApplicationTests {

	@Autowired
	GameService gameService;

	static Game game;

	@BeforeAll
	static void beforeAll() {
		game = new Game(new Space());
	}

	@BeforeEach
	void setUp() {
		Set<Item> items = new HashSet<>();
		items.add(new Rock(
				1,
				0,
				0,
				10,
				0,
				10,
				1,
				1,
				1
		));
		game.getSpace().setItems(items);
	}

	@AfterAll
	static void afterAll() {

	}

	@Test
	void SimpleTranslationTest() {
		Item item = game.getSpace().getItems().stream().findFirst().get();
		for(int i=0;i<10;i++)
			item.update();

		assertTrue(item.getX() == 10 && item.getY() == 10);
	}

	@Test
	void SimpleRotationTest () {
		Item item = game.getSpace().getItems().stream().findFirst().get();
		for(int i=0;i<10;i++)
			item.update();

		assertTrue(item.getAng() == 10);
	}

}
