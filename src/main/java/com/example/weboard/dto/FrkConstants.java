package com.example.weboard.dto;

public class FrkConstants {
    public static final int CD_OK = 0;
    public static final int CD_NOK = 300;
    public static final String FAIL = "Fail";
    public static final String SUCCESS = "SUCCESS";
    /**
     * 비밀번호 조건은 다음과 같음
     * 1. 8자 이상 16자 이하
     * 2. 숫자 포함
     * 3. 특수문자 포함
     * 4. 12자 미만인 경우 영문 대/소문자 둘 다 포함
     * 5. 12자 이상인 경우 영문 대소문자 구분없이 포함
     */
    public static final String passwordRegexUnder12 = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\-_+<>@#$%&\\\\=\\(\\'\\\"]).{8,11}$";
    public static final String passwordRegex12orMore = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\-_+<>@#$%&\\\\=\\(\\'\\\"]).{12,16}$";

    public static final int lockUser = 1;
    public static final int unlockUser = 0;
    public static final String getUser = "성공적으로 사용자 정보를 가져왔습니다.";
    public static final String insertUser = "성공적으로 사용자 정보를 작성했습니다.";
    public static final String updateUser = "성공적으로 사용자 정보를 수정했습니다.";
    public static final String deleteUser = "성공적으로 사용자 정보를 삭제했습니다.";
    public static final String successLogin = "성공적으로 로그인하였습니다.";

    public static final String getAllPost = "성공적으로 게시물들을 가져왔습니다.";
    public static final String getPostView = "성공적으로 게시물과 댓글을 가져왔습니다.";
    public static final String insertPost = "성공적으로 게시물을 작성했습니다.";
    public static final String updatePost = "성공적으로 게시물을 수정했습니다.";
    public static final String deletePost = "성공적으로 게시물을 삭제했습니다.";

    public static final String getComments = "성공적으로 댓글을 가져왔습니다.";
    public static final String insertComment = "성공적으로 댓글을 작성했습니다.";
    public static final String updateComment = "성공적으로 댓글을 수정했습니다.";
    public static final String deleteComment = "성공적으로 댓글을 삭제했습니다.";


}