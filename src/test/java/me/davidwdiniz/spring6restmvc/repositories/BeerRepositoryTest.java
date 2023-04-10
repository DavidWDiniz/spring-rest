package me.davidwdiniz.spring6restmvc.repositories;

import jakarta.validation.ConstraintViolationException;
import me.davidwdiniz.spring6restmvc.bootstrap.BootstrapData;
import me.davidwdiniz.spring6restmvc.entities.Beer;
import me.davidwdiniz.spring6restmvc.model.BeerStyle;
import me.davidwdiniz.spring6restmvc.services.BeerCsvServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({BootstrapData.class, BeerCsvServiceImpl.class})
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

    @Test
    void testSaveBeerListByName() {
        Page<Beer> list = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%", null);
        assertThat(list.getContent().size()).isEqualTo(336);
    }

    @Test
    void testSaveBeerListByStyle() {
        Page<Beer> list = beerRepository.findAllByBeerStyle(BeerStyle.IPA, null);
        assertThat(list.getContent().size()).isEqualTo(548);
    }

    @Test
    void testSaveBeerListByNameAndStyle() {
        Page<Beer> list = beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%black%", BeerStyle.IPA, null);
        assertThat(list.getContent().size()).isEqualTo(4);
    }
}