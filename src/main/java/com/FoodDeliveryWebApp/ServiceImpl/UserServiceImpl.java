package com.FoodDeliveryWebApp.ServiceImpl;

import com.FoodDeliveryWebApp.Entity.ForgotPasswordOtp;
import com.FoodDeliveryWebApp.Entity.TemporaryUser;
import com.FoodDeliveryWebApp.Entity.User;
import com.FoodDeliveryWebApp.Exception.UserNotFoundException;
import com.FoodDeliveryWebApp.Repository.ForgotPasswordOtpRepository;
import com.FoodDeliveryWebApp.Repository.TemporaryUserRepository;
import com.FoodDeliveryWebApp.Repository.UserRepository;
import com.FoodDeliveryWebApp.ServiceI.EmailService;
import com.FoodDeliveryWebApp.ServiceI.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.FoodDeliveryWebApp.CommanUtil.ValidationClass.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ForgotPasswordOtpRepository otpRepository;

    @Autowired
    private TemporaryUserRepository temporaryUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private static final int OTP_EXPIRY_MINUTES = 10;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    @Transactional
    public String registerTemporaryUser(User user) {
        validateUserData(user);
        if (!user.getConfirmPassword().equals(user.getPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        String otp = generateOtp();

        TemporaryUser tempUser = new TemporaryUser();
        tempUser.setName(user.getName());
        tempUser.setEmail(user.getEmail());
        tempUser.setGender(user.getGender());
        tempUser.setMobileNo(user.getMobileNo());
        tempUser.setAddress(user.getAddress());
        tempUser.setUsername(user.getUsername());
        tempUser.setPassword(user.getPassword());
        tempUser.setConfirmPassword(user.getConfirmPassword());
        tempUser.setOtp(otp);
        tempUser.setOtpExpiry(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES)); // OTP expires in 10 minutes
        tempUser.setProfilePicture(user.getProfilePicture());
        temporaryUserRepository.save(tempUser);

        emailService.sendEmail(user.getEmail(), "Your OTP Code to register", "Your OTP code to register is: " + otp);

        return "Temporary user registered. Please verify OTP sent to your email.";
    }

    @Override
    @Transactional
    public String verifyOtpToRegister(String email, String otp) {
        logger.info("Attempting to verify OTP: {}", otp);
        Optional<TemporaryUser> tempUserOpt = temporaryUserRepository.findByOtp(otp);
        if (!tempUserOpt.isPresent()) {
            logger.error("OTP not found: {}", otp);
            throw new IllegalArgumentException("Temporary user not found");
        }

        TemporaryUser tempUser = tempUserOpt.get();
        logger.info("OTP found for email: {}", tempUser.getEmail());
        if (tempUser.getOtp().equals(otp) && tempUser.getOtpExpiry().isAfter(LocalDateTime.now())) {
            logger.info("OTP is valid and not expired");
            User user = new User();
            user.setName(tempUser.getName());
            user.setEmail(tempUser.getEmail());
            user.setGender(tempUser.getGender());
            user.setMobileNo(tempUser.getMobileNo());
            user.setAddress(tempUser.getAddress());
            user.setUsername(tempUser.getUsername());
            user.setPassword(tempUser.getPassword());
            user.setConfirmPassword(tempUser.getConfirmPassword());
            user.setProfilePicture(tempUser.getProfilePicture());
            user.setVerified(true);

            userRepository.save(user);
            temporaryUserRepository.delete(tempUser);

            return "User verified and registered successfully.";
        } else {
            return "Invalid or expired OTP.";
        }
    }


    @Override
    public User loginUser(String username, String password) throws UserNotFoundException {
        return userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new UserNotFoundException("User " + username + " not found"));
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }

    @Override
    public User updateUser(User user) {
        validateUserData(user);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUserDetails(Long userId, User user) {
        logger.info("Updating user by id: {}, data: {}", userId, user);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        if (user.getName() != null) existingUser.setName(user.getName());
        if (user.getUsername() != null) existingUser.setUsername(user.getUsername());
        if (user.getEmail() != null) existingUser.setEmail(user.getEmail());
        if (user.getGender() != null) existingUser.setGender(user.getGender());
        if (user.getMobileNo() != null) existingUser.setMobileNo(user.getMobileNo());
        if (user.getAddress() != null) existingUser.setAddress(user.getAddress());
        if (user.getProfilePicture()!= null) existingUser.setProfilePicture(user.getProfilePicture());
        if (user.getPassword() != null) existingUser.setPassword(user.getPassword());

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public String deleteProfilePicture(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setProfilePicture(null); // Remove the profile picture
            userRepository.save(user);
            return "Profile picture deleted successfully.";
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public void deleteUser(Long userId) {
        logger.info("Deleting user by id: {}", userId);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        userRepository.deleteById(userId);
        logger.info("Successfully deleted user with id: {}", userId);
    }

    @Override
    @Transactional
    public void createPasswordResetOtpForUser(User user, String otp) {
        Optional<ForgotPasswordOtp> existingOtp = otpRepository.findByUser(user);

        ForgotPasswordOtp otpEntity = existingOtp.orElseGet(ForgotPasswordOtp::new);
        otpEntity.setOtp(otp);
        otpEntity.setUser(user);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setExpiryDate(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES)); // OTP expires in 10 minutes

        otpRepository.save(otpEntity);
    }

    private String generateOtp() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000)); // generate 6-digit OTP
    }

    @Override
    public String validatePasswordResetOtp(String otp) {
        Optional<ForgotPasswordOtp> passOtp = otpRepository.findByOtp(otp);
        if (passOtp.isPresent() && passOtp.get().getExpiryDate().isAfter(LocalDateTime.now())) {
            return null;
        } else {
            return "Invalid or expired OTP.";
        }
    }

    @Override
    public void changeUserPassword(User user, String newPassword) {
        user.setPassword(newPassword);
        user.setConfirmPassword(newPassword);
        userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional
    public String requestPasswordReset(String email) {
        User user = getUserByEmail(email);
        String otp = generateOtp();
        createPasswordResetOtpForUser(user, otp);
        return otp;
    }

    private void validateUserData(User user) {
        if (user.getUsername() == null || !USERNAME_PATTERN.matcher(user.getUsername()).matches()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (user.getEmail() == null || !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("Email is not valid.");
        }
        if (user.getGender() == null || !GENDER_PATTERN.matcher(user.getGender()).matches()) {
            throw new IllegalArgumentException("Gender is required.");
        }
        if (user.getMobileNo() == null || !PHONE_PATTERN.matcher(user.getMobileNo()).matches()) {
            throw new IllegalArgumentException("Mobile number should be 10 digits.");
        }
        if (user.getAddress() == null || !ADDRESS_PATTERN.matcher(user.getAddress().toString()).matches()) {
            throw new IllegalArgumentException("Address is required.");
        }
        if (user.getPassword() == null || !PASSWORD_PATTERN.matcher(user.getPassword()).matches()) {
            throw new IllegalArgumentException("Password should be 6 characters.");
        }
        if (user.getConfirmPassword() == null || !PASSWORD_PATTERN.matcher(user.getConfirmPassword()).matches()) {
            throw new IllegalArgumentException("Confirm Password should be 6 characters.");
        }
    }
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


}
