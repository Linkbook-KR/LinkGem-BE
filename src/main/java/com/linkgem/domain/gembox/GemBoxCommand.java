package com.linkgem.domain.gembox;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class GemBoxCommand {

    private GemBoxCommand() {
    }

    @Getter
    public static class Create {
        private String name;
        private List<Long> linkIds;
        private Long userId;
        private Boolean isDefault;

        public GemBox toEntity() {
            return GemBox.builder()
                .name(this.name)
                .userId(this.userId)
                .isDefault(this.isDefault)
                .build();
        }

        public static Create createDefault(Long userId) {
            return new Create(GemBox.DEFAULT_GEMBOX_NAME, null, userId, true);
        }

        public static Create createMergeGembox(String name, Long userId) {
            return new Create(name, null, userId, false);
        }

        @Builder
        private Create(String name, List<Long> linkIds, Long userId, boolean isDefault) {
            this.name = name;
            this.linkIds = linkIds;
            this.userId = userId;
            this.isDefault = isDefault;
        }
    }

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Update {
        private Long id;
        private String name;
        private Long userId;
    }

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Delete {
        private List<Long> ids;
        private Long userId;

        public static Delete of(List<Long> ids, Long userId) {
            return new Delete(ids, userId);
        }
    }

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class PutLinksToGembox {
        private Long userId;
        private Long gemBoxId;
        private List<Long> linkIds;
    }

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Merge {
        private Long userId;
        private Long targetId;
        private Long sourceId;

        public static Merge of(Long userId, Long targetId, Long sourceId) {
            return new Merge(userId, targetId, sourceId);
        }
    }

    @Getter
    public static class MergeMulti {
        private String name;
        private List<Long> gemboxIds;
        private Long userId;

        public GemBox toEntity() {
            return GemBox.builder()
                .name(this.name)
                .userId(this.userId)
                .build();
        }

        @Builder
        private MergeMulti(String name, List<Long> gemboxIds, Long userId) {
            this.name = name;
            this.gemboxIds = gemboxIds;
            this.userId = userId;
        }
    }
}
