package com.grupop.petru.controladores;

import org.omg.CORBA.UserException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.grupop.petru.entidades.Imagen;
import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.servicios.ImagenServicio;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
    private ImagenServicio imagenServicio;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id, ModelMap model) {
        Imagen imagen = imagenServicio.getOne(id);

        byte[] image = imagen.getContenido();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }
}
