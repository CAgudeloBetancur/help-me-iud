package co.edu.iudigital.helpmeiud.auth;

import co.edu.iudigital.helpmeiud.dtos.usuarios.UsuarioResponseDto;
import co.edu.iudigital.helpmeiud.exceptions.RestException;
import co.edu.iudigital.helpmeiud.models.Usuario;
import co.edu.iudigital.helpmeiud.services.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Adicionar info del usuario al token
 * o cualquier otra
 * luego se registra en AuthorizationServerConfig
 * @author JULIOCESARMARTINEZ
 *
 */
@Component
public class TokenMoreInfo implements TokenEnhancer {

    @Autowired
    private IUsuarioService usuarioService;

    @Override
    public OAuth2AccessToken enhance(
            OAuth2AccessToken accessToken,
            OAuth2Authentication authentication) {
        try {
            UsuarioResponseDto usuario = usuarioService.findByUsername(authentication.getName());
            Map<String, Object> info = new HashMap<>();
            info.put("id_usuario", ""+usuario.getId());
            info.put("nombre", usuario.getNombre());
            info.put("image", usuario.getImage());
            info.put("fecha_nacimiento", ""+usuario.getFechaNacimiento());
            ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(info);
            return accessToken;
        } catch (Exception e) {
            e.printStackTrace();
            return accessToken; // No se si funcione, puede generar errores.
        }
    }

}