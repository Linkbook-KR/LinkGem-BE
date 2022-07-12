package com.linkgem.infrastructure.link.opengraph;

import com.linkgem.domain.link.opengraph.OpenGraph;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class OpenGraphCaller {

    private final String ogTagSelectQuery = "meta[property ^= og], meta[name ^= og]";
    private final String ogTagProperty = "property";
    private final String ogTagName = "name";

    private final String contentAttribute = "content";

    private final String imageOgTag = "og:image";
    private final String titleOgTag = "og:title";
    private final String descriptionOgTag = "og:description";

    public Optional<Result> call(String url) {
        Optional<Result> defaultOpenGraph = Optional.empty();

        if (!StringUtils.hasText(url)) {
            return defaultOpenGraph;
        }

        try {
            //og 태그만 추출한다
            Elements openGraphElements = Jsoup
                .connect(url)
                .get()
                .select(ogTagSelectQuery);

            return createOpenGraph(openGraphElements);

        } catch (Exception e) {
            log.error("open graph reading errors : {}", e.toString());
            return defaultOpenGraph;
        }
    }

    private Optional<Result> createOpenGraph(Elements openGraphElements) {

        String imageUrl = "";
        String title = "";
        String description = "";

        for (Element openGraphElement : openGraphElements) {

            //attribute 값이 property 또는 name인지 확인한다
            final String attribute = this.findAttribute(openGraphElement);
            final String contentKey = openGraphElement.attr(attribute);
            final String contentValue = openGraphElement.attr(contentAttribute);

            switch (contentKey) {
                case imageOgTag:
                    imageUrl = contentValue;
                    break;
                case titleOgTag:
                    title = contentValue;
                    break;
                case descriptionOgTag:
                    description = contentValue;
                    break;
            }
        }
        return Optional.ofNullable(Result.builder()
            .title(title)
            .description(description)
            .imageUrl(imageUrl)
            .build());
    }

    private String findAttribute(Element openGraphElement) {
        return openGraphElement.hasAttr(ogTagProperty) ? ogTagProperty : ogTagName;
    }

    @Getter
    public static class Result {
        private String imageUrl;
        private String title;
        private String description;

        @Builder
        public Result(String imageUrl, String title, String description) {
            this.imageUrl = imageUrl;
            this.title = title;
            this.description = description;
        }

        public OpenGraph to() {
            return OpenGraph.builder()
                .imageUrl(this.imageUrl)
                .title(this.title)
                .description(this.description)
                .build();
        }
    }
}
