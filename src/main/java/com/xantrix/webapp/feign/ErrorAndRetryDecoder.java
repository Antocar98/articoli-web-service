package com.xantrix.webapp.feign;

import com.xantrix.webapp.exceptions.NotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

@Component
@Log
public class ErrorAndRetryDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new ErrorDecoder.Default();
    @Override
    public Exception decode(String s, Response response) {
        if(response.status() >= 404 && response.status() < 499) {
            switch (response.status())
            {
                case 400:
                    log.warning("Codice Stato " +response.status() + "Metodo = " + s);

                case 404:
                {
                    log.warning("Errore nel Feign Client inviando una richiesta HTTP. Codice stato: " + response.status() + " Metodo = " + s);
                    return new NotFoundException("Prezzo Articolo non trovato!");
                }

                default:
                    return new Exception(response.reason());
            }
        } else if (response.status() >= 500) {
            log.warning("Codice Stato "+ response.status() + ", Metodo = " + s);

        }

        return defaultErrorDecoder.decode(s, response);
    }
}
