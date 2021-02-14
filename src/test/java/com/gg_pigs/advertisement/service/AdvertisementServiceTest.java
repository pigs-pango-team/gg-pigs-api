package com.gg_pigs.advertisement.service;

import com.gg_pigs.advertisement.entity.Advertisement;
import com.gg_pigs.advertisementType.entity.AdvertisementType;
import com.gg_pigs.user.entity.User;
import com.gg_pigs.advertisement.dto.CreateDtoAdvertisement;
import com.gg_pigs.advertisement.dto.RetrieveDtoAdvertisement;
import com.gg_pigs.advertisement.dto.UpdateDtoAdvertisement;
import com.gg_pigs.advertisement.repository.AdvertisementRepository;
import com.gg_pigs.advertisementType.repository.AdvertisementTypeRepository;
import com.gg_pigs.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

/**
 * [References]
 * 1. https://www.baeldung.com/spring-boot-testing#mocking-with-mockbean
 * 2. https://brunch.co.kr/@springboot/207
 * */

@SpringBootTest(
        classes = {
                AdvertisementService.class,
        }
)
class AdvertisementServiceTest {

    @Autowired AdvertisementService advertisementService;

    @MockBean UserRepository userRepository;
    @MockBean AdvertisementTypeRepository advertisementTypeRepository;
    @MockBean AdvertisementRepository advertisementRepository;

    @Mock User savedUser;
    @Mock AdvertisementType savedAdvertisementType;
    @Mock Advertisement savedAdvertisement;

    private Long advertisementId = 1L;
    private Long rowPosition = 1L;
    private Long columnPosition = 1L;
    private String advertisementType = "R1";

    @BeforeEach
    void setUp() {
        // Configuration of advertisement
        Mockito.when(savedAdvertisement.getId()).thenReturn(advertisementId);
        Mockito.when(savedAdvertisement.getAdvertisementType()).thenReturn(savedAdvertisementType);
        Mockito.when(savedAdvertisement.getStartedDate()).thenReturn(LocalDate.now());
        Mockito.when(savedAdvertisement.getFinishedDate()).thenReturn(LocalDate.now().plusMonths(1));

        // Configuration of userRepository
        Mockito.when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.ofNullable(savedUser));

        // Configuration of advertisementRepository
        Mockito.when(advertisementRepository.save(any(Advertisement.class))).thenReturn(savedAdvertisement);
        Mockito.when(advertisementRepository.findById(anyLong())).thenReturn(Optional.ofNullable(savedAdvertisement));

        // Configuration of advertisementTypeRepository
        Mockito.when(advertisementTypeRepository.findByType(anyString())).thenReturn(Optional.of(savedAdvertisementType));
    }

    @Test
    public void When_call_createOneAdvertisement_Then_return_savedAdvertisementId() throws Exception {
        // Given
        CreateDtoAdvertisement createDtoAdvertisement = Mockito.mock(CreateDtoAdvertisement.class);
        Mockito.when(createDtoAdvertisement.getAdvertisementType()).thenReturn(advertisementType);
        Mockito.when(createDtoAdvertisement.getRowPosition()).thenReturn(String.valueOf(rowPosition));
        Mockito.when(createDtoAdvertisement.getColumnPosition()).thenReturn(String.valueOf(columnPosition));
        Mockito.when(createDtoAdvertisement.getStartedDate()).thenReturn(String.valueOf(LocalDate.now()));
        Mockito.when(createDtoAdvertisement.getFinishedDate()).thenReturn(String.valueOf(LocalDate.now().plusMonths(1)));

        // When
        Long savedAdvertisementId = advertisementService.createOneAdvertisement(createDtoAdvertisement);

        // Then
        assertThat(savedAdvertisementId).isEqualTo(advertisementId);                                               // 1. createOneAdvertisement 함수의 반환 값은 savedAdvertisementId 이다.
        Mockito.verify(advertisementRepository, times(1)).save(any(Advertisement.class));   // 2. advertisementRepository 의 save 함수가 1회 호출된다.
    }

    @Test
    public void When_call_retrieveOneAdvertisement_Then_return_retrieveDtoAdvertisement() {
        // Given
        Long savedAdvertisementId = savedAdvertisement.getId();

        // When
        RetrieveDtoAdvertisement foundAdvertisement = advertisementService.retrieveOneAdvertisement(savedAdvertisementId);

        // Then
        assertThat(foundAdvertisement.getClass()).isEqualTo(RetrieveDtoAdvertisement.class);            // 1. retrieveOneAdvertisement 함수의 반환 클래스는 RetrieveDtoAdvertisement 클래스다.
        Mockito.verify(advertisementRepository, times(1)).findById(anyLong());   // 2. AdvertisementRepository 의 findById 함수가 1회 호출된다.
    }

    @Test
    public void When_call_retrieveAllAdvertisement_Then_return_list() {
        // Given // When
        HashMap<String, String> retrieveOptions = new HashMap<String, String>();
        List<Advertisement> advertisementList = advertisementService.retrieveAllAdvertisement(retrieveOptions);

        // Then
        assertThat(advertisementList.getClass()).isEqualTo(ArrayList.class);
        Mockito.verify(advertisementRepository, times(1)).findAllByPage(anyLong(), anyLong());
    }

    @Test
    public void When_call_retrieveAllAdvertisement_v2_Then_return_list() {
        // Given
        HashMap<String, String> retrieveCondition = new HashMap<>();
        retrieveCondition.put("page", null);
        retrieveCondition.put("userEmail", null);
        retrieveCondition.put("isFilteredDate", null);

        // When
        List<Advertisement> advertisementList = advertisementService.retrieveAllAdvertisement_v2(retrieveCondition);

        // Then
        assertThat(advertisementList.getClass()).isEqualTo(ArrayList.class);
        Mockito.verify(advertisementRepository, times(1)).findAllByCondition(any());
    }

    @Test
    public void When_call_updateOneAdvertisement_Then_return_updatedAdvertisementId() throws Exception {
        // Given
        UpdateDtoAdvertisement updateDtoAdvertisement = Mockito.mock(UpdateDtoAdvertisement.class);
        Mockito.when(updateDtoAdvertisement.getId()).thenReturn(advertisementId);

        // When
        Long updatedAdvertisementId = advertisementService.updateOneAdvertisement(updateDtoAdvertisement.getId(), updateDtoAdvertisement);

        // Then
        assertThat(updatedAdvertisementId).isEqualTo(updateDtoAdvertisement.getId());                                                  // 1. updateOneAdvertisement 함수의 반환 값은 updatedAdvertisementId 이다.
        Mockito.verify(savedAdvertisement, times(1)).changeAdvertisement(any(UpdateDtoAdvertisement.class));    // 2. Advertisement 의 changeAdvertisement 메소드가 1회 호출된다.
        Mockito.verify(advertisementRepository, times(1)).findById(anyLong());                                  // 3. AdvertisementRepository 의 findById 함수가 1회 호출된다.
    }

    @Test
    public void When_call_deleteOneAdvertisement_Then_return_void() {
        // Given // When
        advertisementService.deleteOneAdvertisement(savedAdvertisement.getId());

        // Then
        Mockito.verify(advertisementRepository, times(1)).deleteById(any(Long.class));  // 1. advertisementRepository 의 deleteById 함수가 1회 호출된다.
    }
}