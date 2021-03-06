package listafilmes.listafilmesapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private long id;
    private String nome;
    private String senha;
    
    public boolean verificaPreenchimentoCampos() {
        if(nome == null || nome.isEmpty() 
                || senha == null || senha.isEmpty()){
            return false;
        }
        
        return true;
    }

}