package student.examples.uservice.api.business.service;

import org.springframework.stereotype.Service;
import student.examples.uservice.api.business.game.Game;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameService {
    private Set<Game> games;

    public GameService() {
        this.games = new HashSet<Game>();
    }

    // TODO: call with a timer (1s)
    public void update(){
        games = games
            .stream()
            .map(game -> {
                game.getSpace().setItems(
                    game
                            .getSpace()
                            .getItems()
                            .stream()
                            .map(item -> {
                                //compute
                                item.update();
                                return item;
                            })
                            .collect(Collectors.toSet())
                );
                return game;
            })
            .collect(Collectors.toSet());
    }

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }
}
