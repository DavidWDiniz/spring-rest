package me.davidwdiniz.spring6restmvc.repositories;

import jakarta.validation.ConstraintViolationException;
import me.davidwdiniz.spring6restmvc.entities.Beer;
import me.davidwdiniz.spring6restmvc.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BeerRepositoryTest {
    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("My beer")
                        .beerStyle(BeerStyle.ALE)
                        .upc("123456")
                        .price(new BigDecimal("11.99"))
                .build());

        beerRepository.flush();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testSaveBeerToLongName() {

        assertThrows(ConstraintViolationException.class, () -> {
            beerRepository.save(Beer.builder()
                    .beerName("My beer 12345678910 12345678910 12345678910 12345678910 12345678910")
                    .beerStyle(BeerStyle.ALE)
                    .upc("123456")
                    .price(new BigDecimal("11.99"))
                    .build());

            beerRepository.flush();
        });

    }
}