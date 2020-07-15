package listafilmes.listafilmesapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizaUsuario extends Usuario {

    private String novo_nome;
    private String nova_senha;
    
    @Override
    public boolean verificaPreenchimentoCampos() {
        if(!super.verificaPreenchimentoCampos() || 
                (novo_nome == null || novo_nome.isEmpty() 
                 || nova_senha == null || nova_senha.isEmpty()) ){
                return false;
        }
        
        return true;
    }

}