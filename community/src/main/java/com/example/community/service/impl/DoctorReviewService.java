package com.example.community.service.impl;

import com.example.community.dto.request.DoctorReviewCreateDTO;
import com.example.community.dto.request.UserIdDTO;
import com.example.community.dto.response.DoctorReviewDTO;
import com.example.community.dto.response.UserInfoDTO;
import com.example.community.exception.CustomeException;
import com.example.community.feingClient.DoctorServiceClient;
import com.example.community.feingClient.UserServiceClient;
import com.example.community.model.DoctorReview;
import com.example.community.repository.DoctorReviewRepository;
import com.example.community.service.IDoctorReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DoctorReviewService implements IDoctorReviewService {
    private final DoctorReviewRepository doctorReviewRepository;
    private final DoctorServiceClient doctorServiceClient;
    private final UserServiceClient userServiceClient;

    public DoctorReviewService(DoctorReviewRepository doctorReviewRepository, DoctorServiceClient doctorServiceClient, UserServiceClient userServiceClient) {
        this.doctorReviewRepository = doctorReviewRepository;
        this.doctorServiceClient = doctorServiceClient;
        this.userServiceClient = userServiceClient;
    }

    // Create Doctor Review
    @Override
    public void createDoctorReview(DoctorReviewCreateDTO createDTO, long userId) {
        log.info("Creating doctor review for user ID: {} and doctor ID: {}", userId, createDTO.getDoctorId());

        //Validate Doctor
        validateDoctorExist(createDTO.getDoctorId());

        // Validate Review existing and Convert DTO to Entity
        ensureReviewDoesNotExist(userId, createDTO.getDoctorId());
        DoctorReview review = buildDoctorReview(createDTO, userId);

        doctorReviewRepository.save(review);
        log.info("Review saved for user ID: {} and doctor ID: {}", userId, createDTO.getDoctorId());
    }

    // Delete Review for specific doctor
    @Override
    public void deleteDoctorReview(long userId, long doctorId) {
        log.info("Attempting to delete review for doctor ID: {} by user ID: {}", doctorId, userId);

        //Validate Doctor
        validateDoctorExist(doctorId);

        DoctorReview review = doctorReviewRepository.findByUserIdAndDoctorId(userId, doctorId)
                .orElseThrow(() -> {
                    log.warn("Review not found for doctor ID: {} and user ID: {}", doctorId, userId);
                    return new CustomeException(HttpStatus.BAD_REQUEST, "You never review this doctor");
                });

        doctorReviewRepository.deleteById(review.getId());
        log.info("Review for doctor ID: {} deleted by user ID: {}", doctorId, userId);
    }

    // Get All review for specific doctor
    @Override
    public List<DoctorReviewDTO> getAllReviewForSpecificDoctor(long doctorId) {
        log.warn("Fetch all reviews with specific doctor");

        //Validate Doctor
        validateDoctorExist(doctorId);

        List<DoctorReview> reviews = doctorReviewRepository.findAllByDoctorIdOrderByUserIdAsc(doctorId);
        List<UserInfoDTO> users = getUserInfoForReviews(reviews);

        return mergeReviewsAndUserInfo(reviews, users);
    }

    // Validate doctor exists or not
    private void validateDoctorExist(long doctorId) {
        log.info("Validate Doctor exists of not with user ID: {}", doctorId);

        if(!doctorServiceClient.doctorExists(doctorId)){
            log.warn("Doctor does not exist for Doctor ID: {}", doctorId);
            throw new CustomeException(HttpStatus.NOT_FOUND, "Doctor does not exist");
        }
    }

    // Validate Already reviewed or not
    private void ensureReviewDoesNotExist(long userId, long doctorId) {
        if (doctorReviewRepository.findByUserIdAndDoctorId(userId, doctorId).isPresent()) {
            log.warn("Attempt to create duplicate review for user ID: {} and doctor ID: {}", userId, doctorId);
            throw new CustomeException(HttpStatus.BAD_REQUEST, "User has already submitted a review");
        }
    }

    // Merge reviews and userinfo
    private List<DoctorReviewDTO> mergeReviewsAndUserInfo(List<DoctorReview> reviews, List<UserInfoDTO> users) {
        log.info("Merging reviews with user information.");
        List<DoctorReviewDTO> reviewDTOs = new ArrayList<>();

        for (int i = 0; i < reviews.size(); i++) {
            DoctorReview review = reviews.get(i);
            UserInfoDTO user = users.get(i);

            if (user != null) {
                DoctorReviewDTO dto = buildHospitalReviewDTO(review, user);
                reviewDTOs.add(dto);
            } else {
                log.warn("No user info found for review with userId: {}", review.getUserId());
            }
        }

        return reviewDTOs;
    }

    // Fetch user info from Patient Microservice
    private List<UserInfoDTO> getUserInfoForReviews(List<DoctorReview> reviews) {
        List<UserIdDTO> userIdDTOs = extractUserIdsFromReviews(reviews);
        return userServiceClient.getUserInfo(userIdDTOs);
    }

    // Map Entity to DTO
    private DoctorReviewDTO buildHospitalReviewDTO(DoctorReview review, UserInfoDTO user) {
        return DoctorReviewDTO.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .imgUrl(user.getImgUrl())
                .reviewMessage(review.getReviewMessage())
                .build();
    }

    // Extract user from reviews
    private List<UserIdDTO> extractUserIdsFromReviews(List<DoctorReview> reviews) {
        log.info("Extracting user IDs from reviews.");
        return reviews.stream()
                .map(DoctorReview::getUserId)
                .distinct()
                .sorted()
                .map(UserIdDTO::new)
                .collect(Collectors.toList());
    }

    // Map DTO to Entity
    private DoctorReview buildDoctorReview(DoctorReviewCreateDTO createDTO, long userId) {
        DoctorReview review = new DoctorReview();
        review.setUserId(userId);
        review.setDoctorId(createDTO.getDoctorId());
        review.setReviewMessage(createDTO.getReviewMessage());

        return review;
    }
}
