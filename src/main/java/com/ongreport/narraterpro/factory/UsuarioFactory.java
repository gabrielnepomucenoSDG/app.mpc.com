package com.ongreport.narraterpro.factory;

import com.ongreport.narraterpro.entity.Usuario;
import com.ongreport.narraterpro.entity.Cidade; // Importe Cidade

public interface UsuarioFactory {
    Usuario criarUsuario(String nome, String email, String senha, Cidade cidade); // Adicione Cidade
}