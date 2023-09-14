package com.foot.service;

import com.foot.dto.chats.ChatMessageRequestDto;
import com.foot.entity.Channel;
import com.foot.entity.ChatLog;
import com.foot.entity.User;
import com.foot.repository.ChannelRepository;
import com.foot.repository.chat.ChatLogRepository;
import com.foot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j

public class MessageService {
    private final ChatLogRepository chatLogRepository;
    private final S3UploadService s3UploadService;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public ChatMessageRequestDto messageImg(ChatMessageRequestDto message) throws IOException {
        String[] strings = message.getMessageImg().split(","); // ","을 기준으로 바이트 코드를 나눠준다
        String base64Image = strings[1];
        String extension = ""; // if 문을 통해 확장자명을 정해줌
        if (strings[0].equals("data:image/jpeg;base64")) {
            extension = "jpeg";
        } else if (strings[0].equals("data:image/png;base64")){
            extension = "png";
        } else {
            extension = "jpg";
        }

        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        File tempFile = File.createTempFile("image", "." + extension); // createTempFile을 통해 임시 파일을 생성해준다. (임시파일은 지워줘야함)
        try (OutputStream outputStream = new FileOutputStream(tempFile)) {
            outputStream.write(imageBytes); // OutputStream outputStream = new FileOutputStream(tempFile)을 통해 생성한 outputStream 객체에 imageBytes를 작성해준다.
        }

        String originalName = message.getWriter(); // uuid를 통해 파일명이 겹치지 않게 해준다
        String awsS3ImageUrl = s3UploadService.uploadMsgImage(tempFile,originalName);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile); // 파일 삭제시 전부 아웃풋 닫아줘야함 (방금 생성한 임시 파일을 지워주는 과정
            fileOutputStream.close(); // 아웃풋 닫아주기
            if (tempFile.delete()) {
//                log.info("File delete success"); // tempFile.delete()를 통해 삭제
            } else {
//                log.info("File delete fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        message.setMessageImg(awsS3ImageUrl);
        ChatLog chatLog=save(message);

        return setMessage(chatLog , message);
    }
    public ChatMessageRequestDto setMessage(ChatLog chatLog , ChatMessageRequestDto message){
        message.setTimeStamp(chatLog.getCreatedAt());
        message.setIsAdminRead(chatLog.getAdminRead());
        message.setIsUserRead(chatLog.getUserRead());

        if(chatLog.getAdminRead() == 1){ // 어드민유저가 채팅을 안읽었을때
            Long readCnt=getAdminCnt(message.getChannelId());
            message.setTotalRead(readCnt);

            Long TotalCnt = getAdminTotalCnt();
            message.setAdminTotalRead(TotalCnt);

        } else if (chatLog.getUserRead() == 1) { // 유저가 채팅을 안읽었을때
            Long readCnt=getUserCnt(message.getChannelId());
            message.setTotalRead(readCnt);
        } else if (chatLog.getAdminRead() == 0 && chatLog.getUserRead() == 0) { // 둘다 채팅방에 접속해있을때
            message.setTotalRead(0L);
        }
        return message;
    }

    public ChatLog save(ChatMessageRequestDto message) {
        User user=userRepository.findByName(message.getWriter()).get();
        Channel channel = channelRepository.findById(message.getChannelId()).get();
        if(channel.getEnterAdmin() != 0 && channel.getEnterUser() !=0){ // 둘다 0이 아니라면 두명다 접속해있다는거 변화 없어도됨
            ChatLog chatLog = ChatLog.builder()
                    .user(user)
                    .channel(channel)
                    .message(message.getMessage())
                    .messageImg(message.getMessageImg())
                    .adminRead(0)
                    .userRead(0)
                    .build();
            chatLogRepository.save(chatLog);
            return chatLog;
        } else if (channel.getEnterAdmin() == 0 && channel.getEnterUser() !=0) { // 어드민은 접속해 있지 않고 유저만 접속해 있는 경우 어드민에 토탈 전해줘서 변화시켜줌
            ChatLog chatLog = ChatLog.builder()
                    .user(user)
                    .channel(channel)
                    .message(message.getMessage())
                    .messageImg(message.getMessageImg())
                    .adminRead(1)
                    .userRead(0)
                    .build();
            chatLogRepository.save(chatLog);
            return chatLog;
        } else if (channel.getEnterAdmin() != 0 && channel.getEnterUser() ==0) { // 어드민은 접속해있는데 유저가 접속해있지 않은경우
            ChatLog chatLog = ChatLog.builder()
                    .user(user)
                    .channel(channel)
                    .message(message.getMessage())
                    .messageImg(message.getMessageImg())
                    .adminRead(0)
                    .userRead(1)
                    .build();
            chatLogRepository.save(chatLog);
            return chatLog;
        } else {
            System.out.println("존재할수없음");
        }
        return null;
    }
    public Long getAdminCnt(Long channelId) {
        Long readCnt = chatLogRepository.getMessageLeadCount(channelId);
        return readCnt;
    }
    public Long getAdminTotalCnt(){
        Long TotalCnt = chatLogRepository.getMessageTotalLeadCount();
        return TotalCnt;
    }
    public Long getUserCnt(Long channelId) {
        Long readCnt = chatLogRepository.getMessageUserLeadCount(channelId);
        return readCnt;
    }

}