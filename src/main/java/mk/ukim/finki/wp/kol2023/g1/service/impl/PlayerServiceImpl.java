package mk.ukim.finki.wp.kol2023.g1.service.impl;

import mk.ukim.finki.wp.kol2023.g1.model.Player;
import mk.ukim.finki.wp.kol2023.g1.model.PlayerPosition;
import mk.ukim.finki.wp.kol2023.g1.model.Team;
import mk.ukim.finki.wp.kol2023.g1.model.exceptions.InvalidPlayerIdException;
import mk.ukim.finki.wp.kol2023.g1.model.exceptions.InvalidTeamIdException;
import mk.ukim.finki.wp.kol2023.g1.repository.PlayerRepository;
import mk.ukim.finki.wp.kol2023.g1.repository.TeamRepository;
import mk.ukim.finki.wp.kol2023.g1.service.PlayerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public List<Player> listAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Player findById(Long id) {
        return playerRepository.findById(id).orElseThrow(InvalidPlayerIdException::new);
    }

    @Override
    public Player create(String name, String bio, Double pointsPerGame, PlayerPosition position, Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(InvalidTeamIdException::new);
        Player player = new Player(name, bio, pointsPerGame, position, team);
        return playerRepository.save(player);
    }

    @Override
    public Player update(Long id, String name, String bio, Double pointsPerGame, PlayerPosition position, Long teamId) {
        Player player = this.findById(id);
        Team team = teamRepository.findById(teamId).orElseThrow(InvalidTeamIdException::new);

        player.setName(name);
        player.setBio(bio);
        player.setPointsPerGame(pointsPerGame);
        player.setPosition(position);
        player.setTeam(team);
        return playerRepository.save(player);
    }

    @Override
    public Player delete(Long id) {
        Player player = this.findById(id);

        playerRepository.delete(player);
        return player;
    }

    @Override
    public Player vote(Long id) {
        Player player = this.findById(id);
        player.setVotes(player.getVotes()+1);

        return playerRepository.save(player);
    }

    @Override
    public List<Player> listPlayersWithPointsLessThanAndPosition(Double pointsPerGame, PlayerPosition position) {
        if (position != null && pointsPerGame != null){
            return playerRepository.findAllByPointsPerGameLessThanAndPosition(pointsPerGame, position);
        }
        if(position != null){
            return playerRepository.findAllByPosition(position);
//            return playerRepository.findAllByPositionMatchesRegex(position.toString());
        }
        if(pointsPerGame != null){
            return playerRepository.findAllByPointsPerGameLessThan(pointsPerGame);
        }
        else {
            return playerRepository.findAll();
        }
    }
}
