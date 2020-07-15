package listafilmes.listafilmesapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filme {
    
    private long id;
    private String nome;
    private String genero;
    private String pais;
    private int duracao;
    private int nota;
    
}
