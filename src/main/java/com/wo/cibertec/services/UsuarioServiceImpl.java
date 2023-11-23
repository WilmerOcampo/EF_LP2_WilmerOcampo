package com.wo.cibertec.services;

import com.wo.cibertec.entities.Usuario;
import com.wo.cibertec.repositories.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements IUsuarioService {
    private final IUsuarioRepository usuarioRepos;

    @Autowired
    public UsuarioServiceImpl(IUsuarioRepository usuarioRepos) {
        this.usuarioRepos = usuarioRepos;
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepos.save(usuario);
    }
}
