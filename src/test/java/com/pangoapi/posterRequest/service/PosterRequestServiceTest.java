package com.pangoapi.posterRequest.service;

import com.pangoapi.poster.entity.Poster;
import com.pangoapi.poster.repository.PosterRepository;
import com.pangoapi.posterRequest.dto.CreateDtoPosterRequest;
import com.pangoapi.posterRequest.entity.PosterRequest;
import com.pangoapi.posterRequest.repository.PosterRequestRepository;
import com.pangoapi.posterType.entity.PosterType;
import com.pangoapi.posterType.repository.PosterTypeRepository;
import com.pangoapi.user.repository.UserRepository;
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
import java.util.Map;
import java.util.Optional;

import static com.pangoapi._common.CommonDefinition.POSTER_LAYOUT_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@SpringBootTest(
        classes = {
                PosterRequestService.class
        }
)
class PosterRequestServiceTest {

    @Autowired PosterRequestService posterRequestService;

    @MockBean UserRepository userRepository;
    @MockBean PosterRepository posterRepository;
    @MockBean PosterTypeRepository posterTypeRepository;
    @MockBean PosterRequestRepository posterRequestRepository;

    @Mock CreateDtoPosterRequest createDtoPosterRequest;
    @Mock PosterRequest posterRequest;
    @Mock PosterType posterType;

    final int POSSIBLE_SEAT = 1;
    final int IMPOSSIBLE_SEAT = -1;

    @BeforeEach
    public void setUp() {
        // Configuration of PosterTypeRepository
        Mockito.when(posterTypeRepository.findPosterTypeByType(any())).thenReturn(Optional.of(posterType));

        // Configuration of PosterRequestRepository
        Mockito.when(posterRequestRepository.save(any())).thenReturn(posterRequest);

        // Configuration of PosterRequest
        Mockito.when(posterRequest.getId()).thenReturn(1L);
    }

    /**
     * Test (case1): createOnePosterRequest()
     * */
    @Test
    public void When_call_createOnePosterRequest_Then_call_save() throws Exception {
        // Given
        Mockito.when(createDtoPosterRequest.getPosterType()).thenReturn("R1");
        Mockito.when(createDtoPosterRequest.getRowPosition()).thenReturn("1");
        Mockito.when(createDtoPosterRequest.getColumnPosition()).thenReturn("1");
        Mockito.when(createDtoPosterRequest.getStartedDate()).thenReturn(String.valueOf(LocalDate.now()));
        Mockito.when(createDtoPosterRequest.getFinishedDate()).thenReturn(String.valueOf(LocalDate.now().plusMonths(1)));

        // When
        posterRequestService.createPosterRequest(createDtoPosterRequest);

        // Then
        Mockito.verify(posterRequestRepository, times(1)).save(any(PosterRequest.class));
    }

    /**
     * Test (case2): createOnePosterRequest()
     * */
    @Test
    public void When_call_createOnePosterRequest_Then_check_possibleSeats() throws Exception {
        // Given
        Mockito.when(createDtoPosterRequest.getPosterType()).thenReturn("R1");
        Mockito.when(createDtoPosterRequest.getRowPosition()).thenReturn("1");
        Mockito.when(createDtoPosterRequest.getColumnPosition()).thenReturn("1");
        Mockito.when(createDtoPosterRequest.getStartedDate()).thenReturn(String.valueOf(LocalDate.now()));
        Mockito.when(createDtoPosterRequest.getFinishedDate()).thenReturn(String.valueOf(LocalDate.now().plusMonths(1)));

        // When
        posterRequestService.createPosterRequest(createDtoPosterRequest);

        // Then
        Mockito.verify(posterRequestRepository, times(1)).save(any(PosterRequest.class));
    }

    /**
     * Test: retrieveAllPosterRequest_v2()
     * */
    @Test
    public void When_call_retrieveAllPosterRequest_v2_Then_return_list() {
        // Given
        HashMap<String, String> retrieveCondition = new HashMap<>();
        retrieveCondition.put("page", null);
        retrieveCondition.put("userEmail", null);
        retrieveCondition.put("isFilteredDate", null);

        // When
        List<Poster> posterList = posterRequestService.retrieveAllPosterRequests_v2(retrieveCondition);

        // Then
        assertThat(posterList.getClass()).isEqualTo(ArrayList.class);
        Mockito.verify(posterRequestRepository, times(1)).findAllByCondition(any());
    }

    /**
     * Test: getPossibleSeatsAsList()
     * */
    @Test
    public void When_call_getPossibleSeatsAsList_Then_return_possibleSeatsAsList() throws Exception {
        // Given
        int[][] allPossibleSeats2D = new int[POSTER_LAYOUT_SIZE + 1][POSTER_LAYOUT_SIZE + 1];
        for(int i = 1; i <= POSTER_LAYOUT_SIZE; i++) {
            for(int j = 1; j <= POSTER_LAYOUT_SIZE; j++) {
                allPossibleSeats2D[i][j] = IMPOSSIBLE_SEAT;
            }
        }

        int possibleRowIndex = 1; int possibleColumnIndex = 1;
        allPossibleSeats2D[possibleRowIndex][possibleColumnIndex] = POSSIBLE_SEAT;

        // When
        List<String[]> allPossibleSeatsAsList = posterRequestService.getPossibleSeatsAsList(allPossibleSeats2D);

        // Then
        assertThat(allPossibleSeatsAsList.get(0).length).isEqualTo(2);
    }

    /**
     * Test: isPossibleSeat()
     * */
    @Test
    public void When_call_isPossibleSeat_Then_check_possible_to_apply() {
        // Given
        int[][] allPossibleSeats2D = new int[POSTER_LAYOUT_SIZE + 1][POSTER_LAYOUT_SIZE + 1];
        for(int i = 1; i <= POSTER_LAYOUT_SIZE; i++) {
            for(int j = 1; j <= POSTER_LAYOUT_SIZE; j++) {
                allPossibleSeats2D[i][j] = IMPOSSIBLE_SEAT;
            }
        }

        int possibleRowIndex = 1; int possibleColumnIndex = 1;
        allPossibleSeats2D[possibleRowIndex][possibleColumnIndex] = POSSIBLE_SEAT;

        List<String[]> allPossibleSeatsAsList = posterRequestService.getPossibleSeatsAsList(allPossibleSeats2D);

        // When
        boolean isPossible = posterRequestService.isPossibleSeat(allPossibleSeatsAsList, 1L, 1L, "R1");
        boolean isImpossible1 = posterRequestService.isPossibleSeat(allPossibleSeatsAsList, 1L, 2L, "R2");
        boolean isImpossible2 = posterRequestService.isPossibleSeat(allPossibleSeatsAsList, 1L, 1L, "R2");

        // Then
        assertThat(isPossible).isEqualTo(true);
        assertThat(isImpossible1).isEqualTo(false);
        assertThat(isImpossible2).isEqualTo(false);
    }

    /**
     * Test (case1): calculatePossibleSeats()
     * */
    @Test
    public void When_call_calculatePossibleSeats_Then_calculate_possibleSeats_case1() {
        // Given
        int[][] allSeats = new int[POSTER_LAYOUT_SIZE + 1][POSTER_LAYOUT_SIZE + 1];
        int impossibleRowIndex = 1; int impossibleColumnIndex = 1;
        List<Map<String, String>> impossibleSeats = new ArrayList<>();
        impossibleSeats.add(new HashMap<String, String>() {{
            put("posterType", "R1");
            put("rowPosition", String.valueOf(impossibleRowIndex));
            put("columnPosition", String.valueOf(impossibleColumnIndex));
        }});

        // When
        boolean resultOfCalculation = posterRequestService.calculatePossibleSeats(allSeats, impossibleSeats);

        // Then
        assertThat(resultOfCalculation).isEqualTo(true);
        assertThat(allSeats[impossibleRowIndex][impossibleColumnIndex]).isEqualTo(-1);


        impossibleSeats.add(new HashMap<String, String>() {{
            put("posterType", "R6");
            put("rowPosition", String.valueOf(impossibleRowIndex + 1));
            put("columnPosition", String.valueOf(impossibleColumnIndex + 1));
        }});
        boolean resultOfCalculation2 = posterRequestService.calculatePossibleSeats(allSeats, impossibleSeats);
        assertThat(resultOfCalculation2).isEqualTo(false);
    }

    /**
     * Test (case2): calculatePossibleSeats()
     * */
    @Test
    public void When_call_calculatePossibleSeats_Then_calculate_possibleSeats_case2() {
        // Given
        int[][] allSeats = new int[POSTER_LAYOUT_SIZE + 1][POSTER_LAYOUT_SIZE + 1];
        int impossibleRowIndex = 1; int impossibleColumnIndex = 1;
        List<Map<String, String>> impossibleSeats = new ArrayList<>();
        impossibleSeats.add(new HashMap<String, String>() {{
            put("posterType", "R6");
            put("rowPosition", String.valueOf(impossibleRowIndex + 1));
            put("columnPosition", String.valueOf(impossibleColumnIndex ));
        }});

        // When
        boolean resultOfCalculation = posterRequestService.calculatePossibleSeats(allSeats, impossibleSeats);

        // Then
        assertThat(resultOfCalculation).isEqualTo(false);
        assertThat(allSeats[impossibleRowIndex + 1][impossibleColumnIndex]).isEqualTo(0);
    }
}