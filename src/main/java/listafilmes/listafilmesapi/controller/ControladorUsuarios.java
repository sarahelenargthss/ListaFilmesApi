package listafilmes.listafilmesapi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import listafilmes.listafilmesapi.model.Filme;
import listafilmes.listafilmesapi.model.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControladorUsuarios {
    
    private HashMap<Long, Usuario> usuarios;
    private HashMap<Long, Filme> filmes;
    private int proxId = 4;

    public ControladorUsuarios() {
        usuarios = new HashMap<>();
        filmes = new HashMap<>();
    }

    //http://localhost:8080/usuarios/listar
    @RequestMapping(value = "/usuarios/listar", method = RequestMethod.GET)
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return new ResponseEntity<List<Usuario>>(new ArrayList<Usuario>(usuarios.values()), HttpStatus.OK);
    }

    //http://localhost:8080/usuarios/1
    @RequestMapping(value = "/usuarios/{id}", method = RequestMethod.GET)
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable("id") Integer id) {
        Usuario usuario = usuarios.get(id);

        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
    }

    //http://localhost:8080/usuarios/remover/1
    @RequestMapping(value = "/usuarios/remover/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removerUsuario(@PathVariable("id") int id) {
        Usuario usuario = usuarios.remove(id);

        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);

    }

    //http://localhost:8080/usuarios/atualizar/1
    @RequestMapping(value = "/usuarios/atualizar/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<Usuario> atualizarUsuario(@RequestBody Usuario usuario, @PathVariable("id") long id) {

        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        usuario.setId(id);
        usuarios.put(id, usuario);
        return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
    }

    //http://localhost:8080/usuarios/criar
    @RequestMapping(value = "/usuarios/criar", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
        
        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        usuario.setId(proxId);
        proxId++;

        usuarios.put(usuario.getId(), usuario);

        return new ResponseEntity<Usuario>(usuarios.get(usuario.getId()), HttpStatus.OK);
    }
    
}