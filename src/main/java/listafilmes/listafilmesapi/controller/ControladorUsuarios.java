package listafilmes.listafilmesapi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import listafilmes.listafilmesapi.model.AtualizaUsuario;
import listafilmes.listafilmesapi.model.Erro;
import listafilmes.listafilmesapi.model.Filme;
import listafilmes.listafilmesapi.model.RetornaGenero;
import listafilmes.listafilmesapi.model.UsuarioRecebeFilme;
import listafilmes.listafilmesapi.model.Usuario;
import listafilmes.listafilmesapi.model.UsuarioFilme;
import listafilmes.listafilmesapi.model.UsuarioLista;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControladorUsuarios {
    
    private HashMap<Long, UsuarioFilme> usuarios;
    private HashMap<Long, UsuarioLista> usuariosLista;
    private long idUsuarioSeguinte = 0;
    private long idFilmeSeguinte = 0;
    private final String caminhoUsuarios = "/usuarios";
    private final String caminhoFilmes = "/filmes";

    public ControladorUsuarios() {
        usuarios = new HashMap<>();
        usuariosLista = new HashMap<>();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //       USUARIOS
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    //http://localhost:8080/usuarios/listar
    @RequestMapping(value = caminhoUsuarios + "/listar", method = RequestMethod.GET)
    public ResponseEntity<List<UsuarioLista>> listarUsuarios() {
        return new ResponseEntity<List<UsuarioLista>>(new ArrayList<>(usuariosLista.values()), HttpStatus.OK);
    }

    //http://localhost:8080/usuarios/1
    @RequestMapping(value = caminhoUsuarios + "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> buscarUsuario(@PathVariable("id") long id) {
        UsuarioLista usuarioLista = usuariosLista.get(id);

        if (usuarioLista == null) {
            return new ResponseEntity<>(new Erro("Usuário não encontrado!"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(usuarioLista, HttpStatus.OK);
    }

    //http://localhost:8080/usuarios/remover/1
    @RequestMapping(value = caminhoUsuarios + "/remover/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removerUsuario(@RequestBody Usuario usuario, @PathVariable("id") long id) {
        if(usuario == null || !usuario.verificaPreenchimentoCampos()){
            return new ResponseEntity<>(new Erro("Campo nome e senha são requeridos!"), HttpStatus.BAD_REQUEST);
        }
        
        Usuario usuarioExclui = usuarios.get(id).getUsuario();

        if (usuarioExclui == null) {
            return new ResponseEntity<>(new Erro("Usuário não encontrado!"), HttpStatus.NOT_FOUND);
        }
        
        if(!usuario.getNome().equals(usuarioExclui.getNome()) || !usuario.getSenha().equals(usuarioExclui.getSenha())){
            return new ResponseEntity<>(new Erro("Nome ou senha incompatível com o usuário a ser excluído!"), HttpStatus.UNAUTHORIZED);
        }
        
        UsuarioLista usuarioLista = usuariosLista.remove(id);
        usuarios.remove(id);        
        
        return new ResponseEntity<>(usuarioLista, HttpStatus.OK);
    }

    //http://localhost:8080/usuarios/atualizar/1
    @RequestMapping(value = caminhoUsuarios + "/atualizar/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<?> atualizarUsuario(@RequestBody AtualizaUsuario usuarioAtualiza, @PathVariable("id") long id) {
        if (usuariosLista.get(id) == null) {
            return new ResponseEntity<>(new Erro("Usuário não encontrado!"), HttpStatus.NOT_FOUND);
        }
        
        if(usuarioAtualiza == null || !usuarioAtualiza.verificaPreenchimentoCampos()){
            return new ResponseEntity<>(new Erro("Campo nome, senha, novo_nome e nova_senha são requeridos!"), HttpStatus.BAD_REQUEST);
        }
        UsuarioFilme uf = usuarios.get(id);
        
        if(!usuarioAtualiza.getNome().equals(uf.getUsuario().getNome()) || !usuarioAtualiza.getSenha().equals(uf.getUsuario().getSenha())){
            return new ResponseEntity<>(new Erro("Nome ou senha incompatível com o usuário a ser atualizado!"), HttpStatus.UNAUTHORIZED);
        }
        
        usuarioAtualiza.setId(id);
        UsuarioLista usuarioLista = new UsuarioLista(id, usuarioAtualiza.getNome());
        
        uf.setUsuario(usuarioAtualiza);
        usuarios.put(id, uf);
        
        usuariosLista.put(id, usuarioLista);
        
        return new ResponseEntity<>(usuarioLista, HttpStatus.OK);
    }

    //http://localhost:8080/usuarios/criar
    @RequestMapping(value = caminhoUsuarios + "/criar", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
        
        if (usuario == null) {
            return new ResponseEntity<>(new Erro("Usuário não encontrado!"), HttpStatus.NOT_FOUND);
        }
        
        if(usuario.getNome() == null || usuario.getNome().isEmpty() 
                || usuario.getSenha() == null || usuario.getSenha().isEmpty()){
            return new ResponseEntity<>(new Erro("Campo nome e senha são requeridos!"), HttpStatus.BAD_REQUEST);
        }
        
        for (UsuarioLista u : new ArrayList<>(usuariosLista.values())){
            if(usuario.getNome().equals(u.getNome())){
                return new ResponseEntity<>(new Erro("Nome de usuário já em uso!"), HttpStatus.NOT_ACCEPTABLE);
            }
        }
        
        usuario.setId(this.idUsuarioSeguinte);
        UsuarioLista usuarioLista = new UsuarioLista(this.idUsuarioSeguinte, usuario.getNome());

        usuarios.put(this.idUsuarioSeguinte, new UsuarioFilme(usuario, new HashMap<>()));
        usuariosLista.put(this.idUsuarioSeguinte, usuarioLista);
        
        this.idUsuarioSeguinte++;

        return new ResponseEntity<>(usuarioLista, HttpStatus.CREATED);
    }
    
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //       FILMES
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    //http://localhost:8080/filmes/listar
    @RequestMapping(value = caminhoFilmes + "/listar", method = RequestMethod.GET)
    public ResponseEntity<?> listarFilmes (@RequestBody Usuario usuario) {
        if(usuario == null || !usuario.verificaPreenchimentoCampos()){
            return new ResponseEntity<>(new Erro("Campo id, nome e senha são requeridos!"), HttpStatus.BAD_REQUEST);
        }
        
        UsuarioFilme usuarioFilme = usuarios.get(usuario.getId());
        
        if(usuarioFilme == null || !usuario.getNome().equals(usuarioFilme.getUsuario().getNome()) || !usuario.getSenha().equals(usuarioFilme.getUsuario().getSenha())){
            return new ResponseEntity<>(new Erro("Nome ou senha incompatível com o usuário cujos filmes seriam consultados!"), HttpStatus.UNAUTHORIZED);
        }
        
        return new ResponseEntity<>(new ArrayList<>(usuarioFilme.getFilmes().values()), HttpStatus.OK);
    }

    //http://localhost:8080/filmes/1
    @RequestMapping(value = caminhoFilmes + "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> buscarFilme (@RequestBody Usuario usuario, @PathVariable("id") long id) {
        if(usuario == null || !usuario.verificaPreenchimentoCampos()){
            return new ResponseEntity<>(new Erro("Campo id, nome e senha são requeridos!"), HttpStatus.BAD_REQUEST);
        }
        
        UsuarioFilme usuarioFilme = usuarios.get(usuario.getId());
        
        if(usuarioFilme == null || !usuario.getNome().equals(usuarioFilme.getUsuario().getNome()) || !usuario.getSenha().equals(usuarioFilme.getUsuario().getSenha())){
            return new ResponseEntity<>(new Erro("Nome ou senha incompatível com o usuário cujo filme seria consultado!"), HttpStatus.UNAUTHORIZED);
        }
        
        Filme filme = usuarioFilme.getFilmes().get(id);
        if(filme == null){
            return new ResponseEntity<>(new Erro("Filme não encontrado!"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(filme, HttpStatus.OK);
    }

    //http://localhost:8080/filmes/remover/1
    @RequestMapping(value = caminhoFilmes + "/remover/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removerFilme (@RequestBody Usuario usuario, @PathVariable("id") long id) {
        if(usuario == null || !usuario.verificaPreenchimentoCampos()){
            return new ResponseEntity<>(new Erro("Campo id, nome e senha são requeridos!"), HttpStatus.BAD_REQUEST);
        }
        
        UsuarioFilme usuarioFilme = usuarios.get(usuario.getId());
        
        if(usuarioFilme == null || !usuario.getNome().equals(usuarioFilme.getUsuario().getNome()) || !usuario.getSenha().equals(usuarioFilme.getUsuario().getSenha())){
            return new ResponseEntity<>(new Erro("Nome ou senha incompatível com o usuário cujo filme seria removido!"), HttpStatus.UNAUTHORIZED);
        }
        
        HashMap<Long, Filme> filmes = usuarioFilme.getFilmes();
        Filme filme = filmes.remove(id);

        if (filme == null) {
            return new ResponseEntity<>(new Erro("Filme não encontrado!"), HttpStatus.NOT_FOUND);
        }
        
        usuarioFilme.setFilmes(filmes);
        usuarios.put(id, usuarioFilme);
        
        return new ResponseEntity<>(filme, HttpStatus.OK);
    }

    //http://localhost:8080/filmes/atualizar/1
    @RequestMapping(value = caminhoFilmes + "/atualizar/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<?> atualizarFilme (@RequestBody UsuarioRecebeFilme recFilme, @PathVariable("id") long id) {
        if(recFilme == null || !recFilme.verificaPreenchimentoCampos()){
            return new ResponseEntity<>(new Erro("Campo id, nome e senha são requeridos!"), HttpStatus.BAD_REQUEST);
        }
        
        UsuarioFilme usuarioFilme = usuarios.get(recFilme.getId());
        
        if(usuarioFilme == null || !recFilme.getNome().equals(usuarioFilme.getUsuario().getNome()) || !recFilme.getSenha().equals(usuarioFilme.getUsuario().getSenha())){
            return new ResponseEntity<>(new Erro("Nome ou senha incompatível com o usuário cujo filme seria atualizado!"), HttpStatus.UNAUTHORIZED);
        }
        
        if (usuarioFilme.getFilmes().get(id) == null) {
            return new ResponseEntity<>(new Erro("Filme não encontrado!"), HttpStatus.NOT_FOUND);
        }
        
        if(!recFilme.verificaGenero() || !recFilme.verificaPreenchimentoCamposFilme()){
            return new ResponseEntity<>(new Erro("Campo nome ou gênero está inválido!"), HttpStatus.NOT_ACCEPTABLE);
        }
        
        return new ResponseEntity<>(recFilme.transformaFilme(id), HttpStatus.OK);
    }

    //http://localhost:8080/filmes/criar
    @RequestMapping(value = caminhoFilmes + "/criar", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> criarFilme (@RequestBody UsuarioRecebeFilme recFilme) {
        if(recFilme == null || !recFilme.verificaPreenchimentoCampos()){
            return new ResponseEntity<>(new Erro("Campo id, nome e senha são requeridos!"), HttpStatus.BAD_REQUEST);
        }
        
        UsuarioFilme usuarioFilme = usuarios.get(recFilme.getId());
        
        if(usuarioFilme == null || !recFilme.getNome().equals(usuarioFilme.getUsuario().getNome()) || !recFilme.getSenha().equals(usuarioFilme.getUsuario().getSenha())){
            return new ResponseEntity<>(new Erro("Nome ou senha incompatível com o usuário cujo filme seria criado!"), HttpStatus.UNAUTHORIZED);
        }
        
        if(!recFilme.verificaGenero() || !recFilme.verificaPreenchimentoCamposFilme()){
            return new ResponseEntity<>(new Erro("Campo nome ou gênero está inválido!"), HttpStatus.NOT_ACCEPTABLE);
        }
        
        Filme filme = recFilme.transformaFilme(idFilmeSeguinte);
        HashMap<Long, Filme> filmes = usuarioFilme.getFilmes();

        filmes.put(idFilmeSeguinte, filme);
        
        usuarioFilme.setFilmes(filmes);
        usuarios.put(recFilme.getId(), usuarioFilme);
                
        this.idFilmeSeguinte++;

        return new ResponseEntity<>(filme, HttpStatus.CREATED);
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //       GÊNERO
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    //http://localhost:8080/generos
    @RequestMapping(value = "/generos", method = RequestMethod.GET)
    public ResponseEntity<List<RetornaGenero>> listarGeneros() {
        return new ResponseEntity<List<RetornaGenero>>(RetornaGenero.montaListaGenero(), HttpStatus.OK);
    }
}