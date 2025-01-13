package com.example.recommendation.service.impl;

import com.example.recommendation.dto.request.HealthInfoDTO;
import com.example.recommendation.dto.response.ProgressDTO;
import com.example.recommendation.dto.response.RecommendationDTO;
import com.example.recommendation.entity.Progress;
import com.example.recommendation.entity.Recommendation;
import com.example.recommendation.exception.CustomeException;
import com.example.recommendation.exception.ResourceNotFoundException;
import com.example.recommendation.repository.ProgressRepository;
import com.example.recommendation.repository.RecommendationRepository;
import com.example.recommendation.service.IProgressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProgressService implements IProgressService {
    private final ProgressRepository progressRepository;
    private final RecommendationRepository recommendationRepository;

    public ProgressService(ProgressRepository progressRepository, RecommendationRepository recommendationRepository) {
        this.progressRepository = progressRepository;
        this.recommendationRepository = recommendationRepository;
    }

    /**
     * Creates a new progress record based on health information.
     * This method takes health information data transfer object (DTO) as an input
     * and builds a new Progress entity from it. The current date is automatically
     * assigned to the Progress entity. The new entity is then saved in the database
     * using the ProgressRepository.
     *
     * @param healthInfoDTO The health information DTO containing user's health data like BMI, BMR, and sugar level.
     */
    public void createProgress(HealthInfoDTO healthInfoDTO){
        Progress process = Progress
                .builder()
                .userId(healthInfoDTO.getUserId())
                .bmi(healthInfoDTO.getBmi())
                .bmr(healthInfoDTO.getBmr())
                .sugarLevel(healthInfoDTO.getSugarLevel())
                .date(LocalDate.now())
                .build();
        progressRepository.save(process);
    }

    /**
     * Retrieves the last five progress records of a user and converts them to DTOs.
     *
     * @param userId The ID of the user.
     * @return A list of ProgressDTO, up to five entries.
     */
    public List<ProgressDTO> getLastFiveProgressRecords(long userId) {
        log.info("Fetching last 5 progress records for user: {}", userId);
        List<Progress> records = progressRepository.findTop5ByUserIdOrderByIdDesc(userId);

        return records.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves a recommendation for a given user ID.
     *
     * @param userId The ID of the user.
     * @return An Optional containing the RecommendationDTO if found, or an empty Optional otherwise.
     */
    public RecommendationDTO getRecommendationByUserId(long userId) {
        log.info("Fetching recommendation for user ID: {}", userId);
        Recommendation recommendation = recommendationRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomeException(HttpStatus.NOT_FOUND, "Please create health profile for get recommendation"));

        return convertToDTO(recommendation);
    }

    // Convert Entity to DTO
    private RecommendationDTO convertToDTO(Recommendation recommendation) {
        RecommendationDTO dto = new RecommendationDTO();
        dto.setId(recommendation.getId());
        dto.setUserId(recommendation.getUserId());
        dto.setDailyCalorieIntake(recommendation.getDailyCalorieIntake());
        dto.setProteinIntake(recommendation.getProteinIntake());
        dto.setRecommendedWeight(recommendation.getRecommendedWeight());
        dto.setFatIntake(recommendation.getFatIntake());
        dto.setPreferredBMR(recommendation.getPreferredBMR());
        dto.setPreferredBMI(recommendation.getPreferredBMI());
        dto.setDiabetesRisk(recommendation.getDiabetesRisk());
        dto.setRiskLevel(recommendation.getRiskLevel());
        return dto;
    }

    // Convert Entity to DTO
    private ProgressDTO convertToDTO(Progress progress) {
        ProgressDTO dto = new ProgressDTO();
        dto.setId(progress.getId());
        dto.setUserId(progress.getUserId());
        dto.setBmi(progress.getBmi());
        dto.setBmr(progress.getBmr());
        dto.setSugarLevel(progress.getSugarLevel());
        dto.setDate(progress.getDate());
        return dto;
    }

}
