package com.linkgem.domain.gem.controller;

import com.linkgem.domain.common.Pages;
import com.linkgem.domain.common.UserAuthenticationProvider;
import com.linkgem.domain.common.controller.CommonResponse;
import com.linkgem.domain.gem.dto.LinkCommand;
import com.linkgem.domain.gem.dto.LinkInfo;
import com.linkgem.domain.gem.dto.LinkRequest;
import com.linkgem.domain.gem.dto.LinkResponse;
import com.linkgem.domain.gem.service.LinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "링크")
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/links")
@RestController
public class LinkOldApiController {

    private final LinkService linkService;

    @ApiOperation(value = "링크 생성", notes = "링크를 생성한다")
    @PostMapping
    public CommonResponse<LinkResponse.CreateLinkResponse> createLink(
        HttpServletRequest httpServletRequest,
        @RequestBody @Valid LinkRequest.CreateLinkRequest request
    ) {
        Long userId = UserAuthenticationProvider.provider(httpServletRequest);
        LinkCommand.Create createCommand = request.to(userId);

        LinkInfo.Create create = linkService.create(createCommand);

        return CommonResponse.of(LinkResponse.CreateLinkResponse.of(create));
    }

    @ApiOperation(value = "링크 상세 조회", notes = "링크를 상세하게 조회한다")
    @GetMapping(value = "/{id}")
    public CommonResponse<LinkResponse.SearchDetailLinkResponse> findById(
        HttpServletRequest httpServletRequest,
        @PathVariable(value = "id") Long id
    ) {
        Long userId = UserAuthenticationProvider.provider(httpServletRequest);
        LinkInfo.Detail detailLink = linkService.findById(id, userId);
        LinkResponse.SearchDetailLinkResponse linkResponse = LinkResponse.SearchDetailLinkResponse.of(detailLink);
        return CommonResponse.of(linkResponse);
    }

    @ApiOperation(value = "링크 목록 조회", notes = "링크 목록을 조회한다")
    @GetMapping
    public CommonResponse<Pages<LinkResponse.SearchLinkResponse>> findAll(
        HttpServletRequest httpServletRequest,
        @PageableDefault(page = 0, size = 10) Pageable pageable,
        @Valid LinkRequest.SearchLinksRequest request
    ) {
        Long userId = UserAuthenticationProvider.provider(httpServletRequest);
        LinkCommand.SearchLinks searchLinks = request.to(userId);

        Page<LinkInfo.Search> infos = linkService.findAll(searchLinks, pageable);
        Page<LinkResponse.SearchLinkResponse> responses = infos.map(LinkResponse.SearchLinkResponse::of);

        return CommonResponse.of(
            Pages.<LinkResponse.SearchLinkResponse>builder()
                .contents(responses.getContent())
                .totalCount(responses.getTotalElements())
                .size(pageable.getPageSize())
                .page(pageable.getPageNumber())
                .build()
        );
    }

    @ApiOperation(value = "링크 삭제", notes = "링크를 삭제한다")
    @DeleteMapping
    public CommonResponse<LinkResponse.DeleteLinkResponse> deleteLinks(
        HttpServletRequest httpServletRequest,
        @RequestBody @Valid LinkRequest.DeleteLinkRequest request
    ) {
        Long userId = UserAuthenticationProvider.provider(httpServletRequest);
        LinkCommand.Delete deleteCommand = request.to(userId);
        List<Long> deleteIds = linkService.deletes(deleteCommand);

        return CommonResponse.of(new LinkResponse.DeleteLinkResponse(deleteIds));

    }

    @ApiOperation(value = "링크 수정", notes = "링크를 수정한다")
    @PatchMapping(value = "/{id}")
    public CommonResponse<LinkResponse.Main> updateLink(
        HttpServletRequest httpServletRequest,
        @PathVariable Long id,
        @RequestBody @Valid LinkRequest.UpdateLinkRequest request
    ) {

        Long userId = UserAuthenticationProvider.provider(httpServletRequest);
        LinkCommand.Update updateCommand = request.to(id, userId);
        LinkInfo.Main updateInfo = linkService.update(updateCommand);

        return CommonResponse.of(LinkResponse.Main.of(updateInfo));
    }
}
