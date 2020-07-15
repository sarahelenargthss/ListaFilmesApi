package listafilmes.listafilmesapi.model;

import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioFilme {
    
    private Usuario usuario;
    private HashMap<Long, Filme> filmes;
    
}
