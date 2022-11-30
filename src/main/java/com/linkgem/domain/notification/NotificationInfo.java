package com.linkgem.domain.notification;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

public class NotificationInfo {

    private NotificationInfo() {
    }

    @Getter
    public static class ButtonMain {

        private NotificationButtonAction buttonAction;

        private String buttonText;

        private String buttonValue;

        public ButtonMain(NotificationButton notificationButton) {
            this.buttonAction = notificationButton.getButtonAction();
            this.buttonText = notificationButton.getButtonText();
            this.buttonValue = notificationButton.getButtonValue();
        }
    }

    @Getter
    public static class Main {
        private Long id;

        private NotificationType type;

        private String content;

        private ButtonMain button;

        private boolean isRead;

        private LocalDateTime createDate;

        @Builder
        public Main(Long id, NotificationType type, String content,
            NotificationButton button, boolean isRead, LocalDateTime createDate) {
            this.id = id;
            this.type = type;
            this.content = content;
            this.isRead = isRead;
            this.button = new ButtonMain(button);
            this.createDate = createDate;
        }

        public static Main of(Notification notification) {
            return Main.builder()
                .id(notification.getId())
                .type(notification.getType())
                .content(notification.getContent())
                .isRead(notification.isRead())
                .button(notification.getButton())
                .createDate(notification.getCreateDate())
                .build();
        }
    }
}
