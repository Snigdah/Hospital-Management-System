package com.example.community.service.impl;

import com.example.community.dto.request.HospitalReviewCreateDTO;
import com.example.community.dto.request.UserIdDTO;
import com.example.community.dto.response.HospitalReviewDTO;
import com.example.community.dto.response.UserInfoDTO;
import com.example.community.exception.CustomeException;
import com.example.community.exception.ResourceNotFoundException;
import com.example.community.feingClient.UserServiceClient;
import com.example.community.model.HospitalReview;
import com.example.community.repository.HospitalReviewRepository;
import com.example.community.service.IHospitalReviewService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HospitalReviewService implements IHospitalReviewService {
    private final HospitalReviewRepository hospitalReviewRepository;
    private final UserServiceClient  userServiceClient;
    public HospitalReviewService(HospitalReviewRepository hospitalReviewRepository, UserServiceClient userServiceClient) {
        this.hospitalReviewRepository = hospitalReviewRepository;
        this.userServiceClient = userServiceClient;
    }

    // Create Review
    @Transactional
    @Override
    public void createHospitalReview(long userId, HospitalReviewCreateDTO dto) {
        //Validate review already exists
        checkForExistingReview(userId);
        // Map DTO to entity and save in database
        HospitalReview review = mapDtoToEntity(userId, dto);
        hospitalReviewRepository.save(review);
        log.info("Review created for user ID: {}", userId);
    }

    //Delete Review by Id
    @Transactional
    @Override
    public void deleteReview(long reviewId, long userId) {
        // Check if the review exists and belongs to the user
        hospitalReviewRepository.findById(reviewId).ifPresentOrElse(review -> {
            if (review.getUserId() != userId) {
                log.warn("User with ID: {} attempted to delete review with ID: {} belonging to another user", userId, reviewId);
                throw new CustomeException(HttpStatus.FORBIDDEN, "Cannot delete another user's review");
            }

            // Delete the review
            hospitalReviewRepository.deleteById(reviewId);
            log.info("Review with ID: {} deleted by user with ID: {}", reviewId, userId);
        }, () -> {
            log.warn("Attempt to delete non-existing review with ID: {}", reviewId);
            throw new ResourceNotFoundException("Review", "Id", reviewId);
        });
    }


    // Get All List of Reviews
    @Override
    public List<HospitalReviewDTO> getAllReviewsWithUserName() {
        log.info("Fetching all reviews with user names.");

        List<HospitalReview> reviews = hospitalReviewRepository.findAllByOrderByUserIdAsc();
        List<UserInfoDTO> users = getUserInfoForReviews(reviews);

        return mergeReviewsAndUserInfo(reviews, users);
    }

    // Fetch user info from Patient Microservice
    private List<UserInfoDTO> getUserInfoForReviews(List<HospitalReview> reviews) {
        List<UserIdDTO> userIdDTOs = extractUserIdsFromReviews(reviews);
        return userServiceClient.getUserInfo(userIdDTOs);
    }

    // Extract user from reviews
    private List<UserIdDTO> extractUserIdsFromReviews(List<HospitalReview> reviews) {
        log.info("Extracting user IDs from reviews.");
        return reviews.stream()
                .map(HospitalReview::getUserId)
                .distinct()
                .sorted()
                .map(UserIdDTO::new)
                .collect(Collectors.toList());
    }

    // Merge reviews and userinfo
    private List<HospitalReviewDTO> mergeReviewsAndUserInfo(List<HospitalReview> reviews, List<UserInfoDTO> users) {
        log.info("Merging reviews with user information.");
        List<HospitalReviewDTO> reviewDTOs = new ArrayList<>();

        for (int i = 0; i < reviews.size(); i++) {
            HospitalReview review = reviews.get(i);
            UserInfoDTO user = users.get(i);

            if (user != null) {
                HospitalReviewDTO dto = buildHospitalReviewDTO(review, user);
                reviewDTOs.add(dto);
            } else {
                log.warn("No user info found for review with userId: {}", review.getUserId());
            }
        }

        return reviewDTOs;
    }

    // Check if Already review exists or not
    private void checkForExistingReview(long userId) {
        if (hospitalReviewRepository.findByUserId(userId).isPresent()) {
            log.warn("Attempt to create duplicate review for user ID: {}", userId);
            throw new CustomeException(HttpStatus.BAD_REQUEST, "User has already submitted a review");
        }
    }

    // Map Entity to DTO
    private HospitalReviewDTO buildHospitalReviewDTO(HospitalReview review, UserInfoDTO user) {
        return HospitalReviewDTO.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .imgUrl(user.getImgUrl())
                .ratting(review.getRatting())
                .reviewMessage(review.getReviewMessage())
                .build();
    }

    // Map DTO to Entity
    private HospitalReview mapDtoToEntity(long userId, HospitalReviewCreateDTO dto) {
        HospitalReview review = new HospitalReview();
        review.setUserId(userId);
        review.setRatting(dto.getRatting());
        review.setReviewMessage(dto.getReviewMessage());
        return review;
    }
}
