package listafilmes.listafilmesapi.model;

import java.util.ArrayList;

public class RetornaGenero {
    
    private int id;
    private String nome; 
    
    RetornaGenero(Genero g){
        id = g.getNumero();
        nome = g.getDescricao();
    }
    
    public static ArrayList<RetornaGenero> montaListaGenero(){
        ArrayList<RetornaGenero> lista = new ArrayList<>();
        for(Genero g : Genero.values()){
            lista.add(new RetornaGenero(g));
        }
        return lista;
    }
    
}
