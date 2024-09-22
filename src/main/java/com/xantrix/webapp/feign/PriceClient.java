package com.xantrix.webapp.feign;

import com.xantrix.webapp.dtos.PrezzoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "PriceArtService")
public interface PriceClient {

    @GetMapping(value = "/api/prezzi/{codart}")
    public Double getDefPriceArt(@RequestHeader("Authorization") String authHeader, @PathVariable("codart") String codArt);

    @GetMapping(value = "/api/prezzi/{codart}/{idlist}")
    public Double getPriceArt(@RequestHeader("Authorization") String authHeader, @PathVariable("codart") String codArt,@PathVariable("idlist") String idList);

    @GetMapping(value = "/api/prezzi/info/{codart}")
    public PrezzoDto getDefPriceArt2(@RequestHeader("Authorization") String authHeader, @PathVariable("codart") String codArt);

    @GetMapping(value = "/api/prezzi/info/{codart}/{idlist}")
    public PrezzoDto getPriceArt2(@RequestHeader("Authorization") String authHeader, @PathVariable("codart") String codArt,@PathVariable("idlist") String idList);

}
