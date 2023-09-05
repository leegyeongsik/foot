package com.foot.dto.chats;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ChatMessageRequestDto implements Serializable {
    private Long channelId;
    private String writer;
    private String message;
    private String messageImg;
    private LocalDateTime timeStamp;
    private int IsAdminRead;
    private int IsUserRead;
    private Long totalRead;
    private Long adminTotalRead;


    public ChatMessageRequestDto() {
    }

    public ChatMessageRequestDto(Long channelId , String writer  , String message ) {
        this.channelId = channelId;
        this.writer = writer;
        this.message  = message;
    }

    public ChatMessageRequestDto(Long channelId , String writer  , String message ,String messageImg  ) {
        this.channelId = channelId;
        this.writer = writer;
        this.message  = message;
        this.messageImg =messageImg;
    }
}
