package listafilmes.listafilmesapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Genero {

    ROMANCE(1, "romance"), 
    SUSPENSE(2, "suspense"), 
    TERROR(3, "terror"), 
    FICÇAO(4, "ficção"), 
    ACAO(5, "ação"), 
    ANIMACAO(6, "animação"), 
    AVENTURA(7, "aventura"), 
    COMEDIA(8, "comédia"), 
    CULT(9, "cult"), 
    DOCUMENTARIO(10, "documentario"), 
    DRAMA(11, "drama"), 
    FANTASIA(12, "fantasia"), 
    FAROESTE(13, "faroeste"), 
    MUSICAL(14, "musical"), 
    POLICIAL(15, "policial");
     
    private int numero;
    private String descricao; 
    
    public static int maxOp = 15;

}
