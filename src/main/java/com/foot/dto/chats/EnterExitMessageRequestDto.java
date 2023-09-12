package com.foot.dto.chats;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class EnterExitMessageRequestDto {
    Long channelIDs;
    String userRole;
}
