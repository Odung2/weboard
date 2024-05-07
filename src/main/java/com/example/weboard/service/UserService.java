package com.example.weboard.service;

import com.example.weboard.dto.FrkConstants;
import com.example.weboard.exception.PasswordRegexException;
import com.example.weboard.mapper.UserMapper;
import com.example.weboard.dto.UserDTO;
import com.example.weboard.param.SignupParam;
import com.example.weboard.param.UpdateUserParam;
import com.example.weboard.response.MyInfoRes;
import com.example.weboard.response.PublicUserInfoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    /**
     * 사용자 ID를 통해 비밀번호를 조회합니다.
     * @param id 사용자 ID
     * @return 해당 사용자의 비밀번호
     */
    public String getPasswordById(int id) {
        getUser(id);
        return userMapper.getPasswordById(id);
    }

    /**
     * 사용자의 로그인 실패 횟수를 증가시키고 최신 실패 횟수를 반환합니다.
     * @param param 사용자 정보
     * @return 업데이트된 로그인 실패 횟수
     */
    public int addLoginFailCount(UserDTO param){
        userMapper.addLoginFailCount(param.getId());
        UserDTO user = userMapper.getUserByIdOrUserId(param);
        return user.getLoginFailCount();
    }

    /**
     * 사용자의 로그인 실패 횟수와 잠금 상태를 초기화합니다.
     * @param param 사용자 정보
     * @return 초기화 후의 로그인 실패 횟수
     */
    public int resetLoginFailCountAndLoginLocked(UserDTO param){
        int id = param.getId();
        userMapper.resetLoginFailCount(id);
        userMapper.resetLoginLocked(id);
        UserDTO user = userMapper.getUserByIdOrUserId(param);
        return user.getLoginFailCount();
    }

    /**
     * 사용자의 잠금 상태를 업데이트합니다.
     * @param id 사용자 ID
     * @param isLocked 잠금 상태 (1: 잠금, 0: 잠금 해제)
     * @return 업데이트 결과
     */
    public int lockUnlockUser(int id, int isLocked){
        return userMapper.lockUnlockUser(id, isLocked);
    }

    /**
     * 사용자의 로그인 잠금 시간을 업데이트합니다.
     * @param user 사용자 정보
     * @return 업데이트 결과
     */
    public int updateLoginLock(UserDTO user){
        user.setLoginLockedAt(LocalDateTime.now());
        return userMapper.updateLoginLocked(user);
    }

    /**
     * 새로운 사용자를 데이터베이스에 삽입합니다.
     *
     * @param signupParam 사용자 정보
     * @return 삽입된 사용자 정보
     * @throws Exception 비밀번호 검증 실패 시 예외 발생
     */
    public String insertUser(SignupParam signupParam) throws NoSuchAlgorithmException {
        String plainPassword = signupParam.getPassword();
        checkNewPwValid(plainPassword);
        String sha256Password = plainToSha256(plainPassword);

        try {
            getUser(signupParam.getUserId());
        } catch (NotFoundException e) {
            UserDTO user = new UserDTO();
            user.setUserId(signupParam.getUserId());
            user.setNickname(signupParam.getNickname());
            user.setPassword(sha256Password);

            userMapper.insert(user);
            return user.getNickname();
        }
            throw new DuplicateKeyException("중복된 아이디입니다. 다른 아이디를 입력해주세요.");

    }

    /**
     * 주어진 ID의 사용자 정보를 업데이트합니다.
     *
     * @param updateUserParam 사용자 정보
     * @param id              사용자 ID
     * @return 업데이트된 사용자 정보
     */
    public String updateUser(UpdateUserParam updateUserParam, int id) throws NoSuchAlgorithmException {
        getUser(id);

        String plainPassword = updateUserParam.getPassword();
        String sha256Password = "";
        if(plainPassword!=null) {
            checkNewPwValid(plainPassword);
            sha256Password += plainToSha256(plainPassword);
        } else {
            sha256Password = null;
        }

        UserDTO user = new UserDTO();
        user.setId(id);
        user.setNickname(updateUserParam.getNickname());
        user.setPassword(sha256Password);
        user.setUpdatedAt(LocalDateTime.now()); // 상속받은 필드는 builder 패턴 사용이 어려움...
        //FIXME auditListener로 고쳐야 함
        user.setUpdatedBy(id);

        userMapper.update(user);
        return user.getNickname();
    }

    /**
     * 사용자를 데이터베이스에서 삭제합니다.
     * @param id 삭제할 사용자 ID
     * @return 삭제 결과
     */
    public int deleteUser(int id){
        // 삭제하려는 유저가 존재하는지 확인
        getUser(id);
        return userMapper.delete(id);
    }

    /**
     * 평문을 SHA-256 해시로 변환합니다.
     * @param plaintext 평문 비밀번호
     * @return 해시된 비밀번호
     */
    public String plainToSha256(String plaintext) throws NoSuchAlgorithmException {
        MessageDigest mdSHA256 = null;
        try {
            mdSHA256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException(e);
        }
        byte[] sha256Password = mdSHA256.digest(plaintext.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : sha256Password) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 비밀번호 regex 만족하는지 확인, 불만족시 PasswordRegexException
     * @param password 검사할 비밀번호
     * @return 검사 결과
     * @throws Exception 비밀번호 길이 또는 형식 불일치로 인한 예외 발생
     */
    public boolean checkNewPwValid(String password) {

        if(password.length() < 12 && Pattern.matches(FrkConstants.passwordRegexUnder12, password)) return true;
        if(Pattern.matches(FrkConstants.passwordRegex12orMore, password)) return true;

        throw new PasswordRegexException("비밀번호는 12자 미만의 경우 영문 대문자, 소문자, 숫자, 특수문자의 조합으로, 12자 이상인 경우 영문, 숫자, 특수문자의 조합으로 입력해주세요.");
    }

    public PublicUserInfoRes getPublicUser(int id){
        return new PublicUserInfoRes(getUser(id));
    }

    public MyInfoRes getPrivateUser(int id){
        return new MyInfoRes(getUser(id));
    }

    /**
     * DB에 저장된 유저 정보가 존재하는지 확인하고, 유저 정보를 반환
     * @param id 또는 userId
     * @return user
     * @throws NotFoundException // 유저 정보가 존재하지 않음.
     */

    public UserDTO getUser(int id) {
        UserDTO user = userMapper.getUserByIdOrUserId(id);
        if (user == null) {
            throw new NotFoundException("사용자를 찾을 수 없습니다.");
        }
        return user;
    }

    public UserDTO getUser(String userId) {
        UserDTO userparam = new UserDTO();
        userparam.setUserId(userId);

        UserDTO user = userMapper.getUserByIdOrUserId(userparam);
        if (user == null) {
            throw new NotFoundException("사용자를 찾을 수 없습니다.");
        }
        return user;
    }

//    /**
//     * user 정보 조회
//     * @param id
//     * @return 조회된 사용자 정보
//     */
//    public UserDTO getUser(int id){
//        return validateUser(id);
//    }
//
//    /**
//     * user 정보 조회
//     * @param userId
//     * @return
//     */
//    public UserDTO getUser(String userId){
//        return validateUser(userId);
//    }

}
