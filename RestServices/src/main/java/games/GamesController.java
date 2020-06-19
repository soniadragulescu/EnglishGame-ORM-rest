package games;

import model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repos.GameRepo;

@CrossOrigin
@RestController
@RequestMapping("/games/")
public class GamesController {
    private static final String template="TemplatePaper";

    @Autowired
    private GameRepo gameRepo;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id){
        Game game=gameRepo.findOne(id);
        if(game == null){
            return new ResponseEntity<String>("Game not found!", HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(game, HttpStatus.OK);
        }
    }

}

