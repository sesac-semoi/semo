package com.semoi.semo.user.service;

import com.semoi.semo.applyForm.repository.PositionRepository;
import com.semoi.semo.campus.domain.Campus;
import com.semoi.semo.campus.domain.Course;
import com.semoi.semo.campus.repository.CourseRepository;
import com.semoi.semo.campus.service.CampusService;
import com.semoi.semo.global.exception.ErrorCode;
import com.semoi.semo.global.exception.SemoException;
import com.semoi.semo.user.domain.User;
import com.semoi.semo.user.dto.request.UserInfoRequestDto;
import com.semoi.semo.user.dto.response.UserResponseDto;
import com.semoi.semo.applyForm.entity.Position;
import com.semoi.semo.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PositionRepository positionRepository;
    private final CampusService campusService;

    @Override
    public UserResponseDto updateUser(String loginEmail, UserInfoRequestDto userInfoRequestDto) {
        User user = getUserByLoginEmailOrElseThrow(loginEmail);

        Campus campus = campusService.getCampusOrElseThrow(userInfoRequestDto.campusName());

        String courseName = userInfoRequestDto.courseName();
        Course course = courseRepository.findByName(courseName)
                .orElse(null);

        if (course == null) {
            course = Course.of(courseName, campus);
            courseRepository.save(course);
        }

        Position position = positionRepository.findByName(userInfoRequestDto.position())
                        .orElseThrow(() -> new SemoException(ErrorCode.POSITION_NOT_FOUND));

        user.updateInfo(
                userInfoRequestDto.username(),
                userInfoRequestDto.userEmail(),
                position,
                course
        );

        User saveUser = userRepository.save(user);

        return UserResponseDto.toDto(saveUser);
    }

    @Override
    public User getUserByUserIdOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    @Override
    public User getUserByLoginEmailOrElseThrow(String loginEmail) {
        return userRepository.findByLoginEmail(loginEmail)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    @Override
    public void resetUserScore() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            user.resetCurrentScore();
            userRepository.save(user);
        }
    }

    @Override
    public Boolean getCheckNewUser(String loginEmail) {

        User user = getUserByLoginEmailOrElseThrow(loginEmail);

        if (user.getPosition() == null || user.getCourse() == null) {
            return true;
        }

        return true;
    }
}