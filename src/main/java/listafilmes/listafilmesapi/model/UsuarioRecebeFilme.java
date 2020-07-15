package listafilmes.listafilmesapi.model;

import lombok.Getter;

@Getter
public class UsuarioRecebeFilme extends Usuario {
    
    private String nome_filme;
    private int genero;
    private String pais;
    private int duracao;
    private int nota;
    
    
    public Filme transformaFilme(long id){
        String strGenero = "";
        for(Genero g : Genero.values()){
            if (g.getNumero() == genero){
                strGenero = g.getDescricao();
            }
        }
        
         return new Filme(id, nome_filme, strGenero, pais, duracao, nota);
    }
    
    public boolean verificaPreenchimentoCamposFilme() {
        if(nome_filme == null || nome_filme.isEmpty() ){
            return false;
        }
        
        return true;
    }
    
    public boolean verificaGenero() {
        if(genero < 1 || genero > Genero.maxOp){
            return false;
        }
        
        return true;
    }
    
}
