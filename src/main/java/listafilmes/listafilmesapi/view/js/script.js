$(document).ready(function () {
    changeFrame(0);
    // atualizaEmpregados();
});

function atualizaEmpregados() {
    $("#table tr").remove();

    var cabecalho = "<tr>";
    cabecalho += "<th>id</th>";
    cabecalho += "<th>nome</th>";
    cabecalho += "<th>gênero</th>";
    cabecalho += "<th>país</th>";
    cabecalho += "<th>duração</th>";
    cabecalho += "<th>nota</th>";
    cabecalho += "<th>ações</th>";
    cabecalho += "</tr>";

    document.getElementById("default_table").insertRow(-1).innerHTML = cabecalho;

    var xHttpRequest = new XMLHttpRequest();

    xHttpRequest.onreadystatechange = function () {
        try {
            if (xHttpRequest.readyState === 4) {
                if (xHttpRequest.status === 200) {
                    var filmes = JSON.parse(xHttpRequest.responseText).data;
                    for (var i = 0; i < filmes.length; i++) {
                        addFilmeLista(filmes[i], i + 1);
                    }
                } else {
                    alert(JSON.parse(xHttpRequest.responseText).data.mensagem);
                }
            }
        } catch (e) {
            alert('Ocorreu uma exceção: ' + e.description);
        }
    };

    var strUsuario = JSON.stringify(getUsuarioLogado());
    //validar retorno

    xHttpRequest.open('GET', '//http://localhost:8080/filmes/listar', true);
    xHttpRequest.setRequestHeader("Content-Type", "application/json");
    xHttpRequest.send(strUsuario);

}

function getUsuarioLogado() {
    // buscar usuario da sessão
    return sessionStorage.getItem("login_user");
}

function addFilmeLista(filme, numRow) {
    var id = String(filme.id);
    var duracao = parseInt(filme.duracao);
    if (isNaN(duracao)) {
        duracao = 0;
    }
    var nota = parseInt(filme.nota);
    if (isNaN(nota)) {
        nota = 0;
    }

    var element = "<tr>";
    element += "<td>" + parseInt(id) + "</td>";
    element += "<td>" + String(filme.nome) + "</td>";
    element += "<td>" + String(filme.genero) + "</td>";
    element += "<td>" + String(filme.pais) + "</td>";
    element += "<td> R$ " + duracao + "</td>";
    element += "<td> R$ " + nota + "</td>";
    element += "<td> <button class=\"colorful\" onclick=\"editarFilme(\'" + id + "\')\">Editar</button>";
    element += "<button class=\"colorful\" onclick=\"excluirFilme(\'" + id + "\', " + numRow + ")\">Excluir</button> </td>";
    element += "</tr>";

    document.getElementById("default_table").insertRow(-1).innerHTML = element;
}

function editarFilme(id) {
    var xHttpRequest = new XMLHttpRequest();

    xHttpRequest.onreadystatechange = function () {
        try {
            if (xHttpRequest.readyState === 4) {
                if (xHttpRequest.status === 200) {
                    var filme = JSON.parse(xHttpRequest.responseText).data;
                    changeFrame(1);
                    formCadastro = false;
                    $("#nome").val(filme.nome);
                    $("#gen").val(filme.genero);
                    $("#pais").val(filme.pais);
                    $("#duracao").val(filme.duracao);
                    $("#nota").val(filme.nota);
                    idFilme = String(filme.id);
                } else {
                    alert(JSON.parse(xHttpRequest.responseText).data.mensagem);
                }
            }
        } catch (e) {
            alert('Ocorreu uma exceção: ' + e.description);
        }
    };

    var strUsuario = JSON.stringify(getUsuarioLogado());

    xHttpRequest.open('GET', '//http://localhost:8080/filmes/' + id, true);
    xHttpRequest.setRequestHeader("Content-Type", "application/json");
    xHttpRequest.send(strUsuario);
}

function excluirFilme(id, numRow) {
    $("#modal").css("display", "flex");

    $(".opExclusao").click(function () {
        if (this.id == "sim") {
            var xHttpRequest = new XMLHttpRequest();

            xHttpRequest.onreadystatechange = function () {
                try {
                    if (xHttpRequest.readyState === 4) {
                        if (xHttpRequest.status === 200) {
                            $("#table tr:eq(" + numRow + ")").remove();
                        } else {
                            alert(JSON.parse(xHttpRequest.responseText).data.mensagem);
                        }
                    }
                } catch (e) {
                    alert('Ocorreu uma exceção: ' + e.description);
                }
            };

            var strUsuario = JSON.stringify(getUsuarioLogado());

            xHttpRequest.open('DELETE', '//http://localhost:8080/filmes/remover/' + id, true);
            xHttpRequest.setRequestHeader("Content-Type", "application/json");
            xHttpRequest.send(strUsuario);
        }
        $("#modal").css("display", "none");
    });
}

function changeFrame(op) {
    if (op == 0) {
        // mostrar tabela
        if (!$('#table').is(':visible')) {
            $("#table").toggle();
            atualizaEmpregados();
        }
        $("#form").hide();
    } else {
        // mostrar formulário
        if (!$('#form').is(':visible')) {
            $("#form").toggle();
        }
        $("#table").hide();
    }
}

function onSave() {
    var usuario = getUsuarioLogado();

    var nome = $("#nome").val();
    var genero = $("#gen").val();
    var pais = $("#pais").val();
    var duracao = parseInt($("#duracao").val());
    var nota = parseInt($("#nota").val());
    if (isNaN(duracao)) {
        alert("Digite uma duracao válida! (em minutos)");
        return;
    }
    var avatar = $("#avatar").val();

    var strFilme = "{";
    strFilme += "\"id\": " + usuario.id + ",";
    strFilme += "\"nome\": \"" + usuario.nome + "\",";
    strFilme += "\"senha\": \"" + usuario.senha + "\",";
    strFilme += "\"nome_filme\": \"" + nome + "\",";
    strFilme += "\"genero\": \"" + genero + "\",";
    strFilme += "\"pais\": \"" + pais + "\",";
    strFilme += "\"duracao\": " + duracao + ",";
    strFilme += "\"nota\": " + nota;
    strFilme += "}";

    var xHttpRequest = new XMLHttpRequest();

    xHttpRequest.onreadystatechange = function () {
        try {
            if (xHttpRequest.readyState === 4) {
                if (xHttpRequest.status === 200) {
                    $("#form .field").val('');
                    changeFrame(0);
                } else {
                    alert("Ocorreu um erro para salvar o empregado!");
                }
            }
        } catch (e) {
            alert('Ocorreu uma exceção: ' + e.description);
        }
    };

    if (formCadastro) { // salva
        xHttpRequest.open('POST', '//http://localhost:8080/filmes/criar', true);
        xHttpRequest.setRequestHeader("Content-Type", "application/json");
        xHttpRequest.send(strFilme);
    } else { //atualiza
        xHttpRequest.open('PUT', '//http://localhost:8080/filmes/atualizar/' + idFilme, true);
        xHttpRequest.setRequestHeader("Content-Type", "application/json");
        xHttpRequest.send(strFilme);
    }
}

function buscaUsuario(usuario) {
    var xHttpRequest = new XMLHttpRequest();

    xHttpRequest.onreadystatechange = function () {
        try {
            if (xHttpRequest.readyState === 4) {
                if (xHttpRequest.status === 200) {
                    var usuarios = JSON.parse(xHttpRequest.responseText).data;
                    for (var i = 0; i < usuarios.length; i++) {
                        if (usuarios[i].nome == usuario) {
                            return usuario[i].id;
                        }
                    }
                    return -1;
                }
            }
        } catch (e) {
            alert('Ocorreu uma exceção: ' + e.description);
        }
    };

    var strUsuario = JSON.stringify(buscarUsuario());

    xHttpRequest.open('GET', '//http://localhost:8080/usuarios/listar', true);
    xHttpRequest.setRequestHeader("Content-Type", "application/json");
    xHttpRequest.send();
}

function onFinalizar() {
    var usuario;
    var nome = $("#usuario").val();
    var senha = $("#senha").val();

    if (formLogin == 1) {
        var id_login = buscaUsuario(nome);
        if (id_login == -1) {
            alert("Usuário não encontrado!");
        }
        var strUsuario = "{";
        strUsuario += "\"id\": " + id_login + ",";
        strUsuario += "\"nome\": \"" + nome + "\",";
        strUsuario += "\"senha\": \"" + senha + "\"";
        strUsuario += "}";
        usuario = JSON.parse(strUsuario);
    } else {
        usuario = getUsuarioLogado();
    }

    var strUsuario = "{";
    strUsuario += "\"nome\": \"" + usuario.nome + "\",";
    strUsuario += "\"senha\": \"" + usuario.senha + "\",";
    if (formLogin < 2) {
        strUsuario += "\"novo_nome\": \"" + nome + "\",";
        strUsuario += "\"nova_senha\": \"" + senha + "\",";
    }
    strUsuario += "}";

    var xHttpRequest = new XMLHttpRequest();

    xHttpRequest.onreadystatechange = function () {
        try {
            if (xHttpRequest.readyState === 4) {
                if (xHttpRequest.status === 200) {
                    sessionStorage.setItem("login_user", usuario);
                    window.location.href = "index.html";

                } else if (formLogin == 0 && xHttpRequest.status === 401) {
                    alert("Senha incompatível com o usuário!");
                } else {
                    alert("Ocorreu um erro para salvar o empregado!");
                }
            }
        } catch (e) {
            alert('Ocorreu uma exceção: ' + e.description);
        }
    };

    xHttpRequest.open('PUT', '//http://localhost:8080/usuarios/atualizar/' + usuario.id, true);
    xHttpRequest.setRequestHeader("Content-Type", "application/json");
    xHttpRequest.send(strUsuario);
}

function sair() {
    sessionStorage.setItem("login_user", usuario);
}

function tela_login(modo) {
    window.location.href = "login.html";
    formLogin = modo;
}

var formCadastro = true;
var formLogin = 0;
var idFilme = "";